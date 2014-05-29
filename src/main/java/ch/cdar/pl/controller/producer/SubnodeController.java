package ch.cdar.pl.controller.producer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.ChangesWrapper;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.exceptions.LockingException;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.bll.manager.producer.NodeLinkManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.pl.controller.StatusHelper;

/**
 * The Class SubnodeController.
 */
@Path("ktrees/{ktreeid}/nodes/{nodeid}/subnodes")
public class SubnodeController {
	/** The isproducer. */
	private final boolean ISPRODUCER = true;
	
	/** The locking manager. */
	private LockingManager lm = new LockingManager();
	
	/** The subnode manager. */
	private SubnodeManager sm = new SubnodeManager();
	
	/** The node link manager. */
	private NodeLinkManager nlm = new NodeLinkManager();

	/**
	 * Gets the subnodes.
	 *
	 * @param nodeId the node id
	 * @return the subnodes
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnodesFromNode(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param subnode the subnode
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(sm.addSubnode(uid, treeId,
					subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param subnodeId the subnode id
	 * @param subnode the subnode
	 * @return the response
	 */
	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId,
			@PathParam("subnodeid") int subnodeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			subnode.setId(subnodeId);
			return StatusHelper.getStatusOk(sm.updateSubnode(subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode
	 */
	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode(@PathParam("subnodeid") int subnodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnode(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Rename subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param subnode the subnode
	 * @return the response
	 */
	@POST
	@Path("{subnodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			sm.renameSubnode(subnode);
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(nlm
					.getNodeLinksBySubnode(subnode.getId()), "update"));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param subnode the subnode
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			List<NodeLink> nodelinks = nlm.getNodeLinksBySubnode(subnode
					.getId());
			sm.deleteSubnode(subnode.getId());
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(
					nodelinks, "delete"));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill up subnode.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpSubnode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.drillUp(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill down subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.drillDown(uid, treeId, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the knowledge subnode wiki entry.
	 *
	 * @param subnodeId the subnode id
	 * @return the knowledge subnode wiki entry
	 */
	@GET
	@Path("{subnodeid}/wiki")
	public Response getKnowledgeSubnodeWikiEntry(
			@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.getKnowledgeSubnodeWikiEntry(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update knowledge subnode wiki entry.
	 *
	 * @param uid the uid
	 * @param wikiEntry the wiki entry
	 * @return the response
	 */
	@POST
	@Path("{subnodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeSubnodeWikiEntry(
			@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeSubnodeWikiEntry(
					uid, wikiEntry));
		} catch (UnknownUserException | EntityException ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
