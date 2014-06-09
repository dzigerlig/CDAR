package ch.cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.exception.LockingException;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.pl.StatusHelper;

/**
 * The Class NodeController.
 */
@Path("ktrees/{ktreeid}/nodes")
public class NodeController {
	/** The isproducer. */
	private final boolean ISPRODUCER = true;
	
	/** The node manager. */
	private NodeManager nm = new NodeManager();
	
	/** The locking manager. */
	private LockingManager lm = new LockingManager();

	/**
	 * Gets the nodes.
	 *
	 * @param ktreeid the ktreeid
	 * @return the nodes
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ktreeid") int ktreeid) {
		try {
			return StatusHelper.getStatusOk(nm.getNodes(ktreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the node.
	 *
	 * @param treeId the tree id
	 * @param uid the uid
	 * @param node the node
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("ktreeid") int treeId,
			@HeaderParam("uid") int uid, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(nm.addNode(uid, node, null));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the node.
	 *
	 * @param nodeId the node id
	 * @return the node
	 */
	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.getNode(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update node.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @param node the node
	 * @return the response
	 */
	@POST
	@Path("{nodeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId,
			Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			node.setId(nodeId);
			return StatusHelper.getStatusOk(nm.updateNode(node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Rename node.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param node the node
	 * @return the response
	 */
	@POST
	@Path("{nodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusOk(nm.renameNode(node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	/**
	 * Copy node.
	 *
	 * @param treeId the tree id
	 * @param nodeid the node id
	 * @param uid the uid
	 * @param Node the project node
	 * @return the response
	 */
	@POST
	@Path("{nodeid}/copy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response copyNode(@PathParam("ktreeid") int treeId,@PathParam("nodeid") int nodeid,
			@HeaderParam("uid") int uid, Node node) {
		node.setId(nodeid);
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusOk(nm.copyNode(uid, node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete node.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param node the node
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			nm.deleteNode(node.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill up node.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillUp(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill down node.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillDown(uid, treeId, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the knowledge node wiki entry.
	 *
	 * @param nodeId the node id
	 * @return the knowledge node wiki entry
	 */
	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.getKnowledgeNodeWikiEntry(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update knowledge node wiki entry.
	 *
	 * @param uid the uid
	 * @param wikiEntry the wiki entry
	 * @return the response
	 */
	@POST
	@Path("{nodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeNodeWikiEntry(@HeaderParam("uid") int uid,
			WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeNodeWikiEntry(uid,
					wikiEntry));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
