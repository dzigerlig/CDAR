/*
 * 
 */
package cdar.pl.controller.consumer;

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

import cdar.bll.entity.ChangesWrapper;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.WikiEntry;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.pl.controller.StatusHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ProjectSubnodeController.
 */
@Path("ptrees/{ptreeid}/nodes/{nodeid}/subnodes")
public class ProjectSubnodeController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The psm. */
	ProjectSubnodeManager psm = new ProjectSubnodeManager();

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
			return StatusHelper.getStatusOk(psm
					.getProjectSubnodesFromProjectNode(nodeId));
		} catch (Exception ex) {
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
			@PathParam("ptreeid") int treeId, ProjectSubnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(psm.addProjectSubnode(uid,
					subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
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
			return StatusHelper.getStatusOk(psm.getProjectSubnode(subnodeId));
		} catch (Exception ex) {
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
			@PathParam("ptreeid") int treeId,
			@PathParam("subnodeid") int subnodeId, ProjectSubnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			subnode.setId(subnodeId);
			return StatusHelper.getStatusOk(psm.updateProjectSubnode(subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	/**
	 * Rename subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param projectSubnode the project subnode
	 * @return the response
	 */
	@POST
	@Path("{subnodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, ProjectSubnode projectSubnode) {
		try {
			ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
			lm.lock(ISPRODUCER, treeId, uid);
			psm.renameSubnode(projectSubnode);
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(pnlm.getProjectNodeLinksBySubnode(projectSubnode.getId()), "update"));
		}catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the subnode wiki entry.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode wiki entry
	 */
	@GET
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodeWikiEntry(@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.getKnowledgeProjectSubnodeWikiEntry(subnodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update subnode wiki entry.
	 *
	 * @param uid the uid
	 * @param wikiEntry the wiki entry
	 * @return the response
	 */
	@POST
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnodeWikiEntry(@HeaderParam("uid") int uid,
			WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.saveKnowledgeProjectSubnodeWikiEntry(uid, wikiEntry));
		} catch (Exception ex) {
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
			return StatusHelper.getStatusOk(psm.drillUp(uid, nodeId));
		} catch (Exception ex) {
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
	public Response drillDownSubnode(@HeaderParam("uid") int uid,@PathParam("ptreeid") int treeId,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(psm.drillDown(uid, treeId, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete subnode.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param projectSubnode the project subnode
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, ProjectSubnode projectSubnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
			List<NodeLink> nodelinks = pnlm
					.getProjectNodeLinksBySubnode(projectSubnode.getId());
			psm.deleteProjectSubnode(projectSubnode.getId());
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(
					nodelinks, "delete"));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
