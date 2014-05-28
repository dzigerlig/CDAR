package ch.cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.exceptions.LockingException;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.bll.manager.consumer.ProjectNodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.pl.controller.StatusHelper;

/**
 * The Class ProjectNodeController.
 */
@Path("ptrees/{ptreeid}/nodes")
public class ProjectNodeController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The pnm. */
	private ProjectNodeManager pnm = new ProjectNodeManager();

	/**
	 * Gets the nodes.
	 *
	 * @param treeId the tree id
	 * @return the nodes
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnm.getProjectNodes(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the node.
	 *
	 * @param treeId the tree id
	 * @param uid the uid
	 * @param projectNode the project node
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(pnm.addProjectNode(uid,
					projectNode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
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
			return StatusHelper.getStatusOk(pnm.getProjectNode(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update node.
	 *
	 * @param treeId the tree id
	 * @param uid the uid
	 * @param nodeId the node id
	 * @param node the node
	 * @return the response
	 */
	@POST
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, @PathParam("nodeid") int nodeId,
			ProjectNode node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			node.setId(nodeId);
			return StatusHelper.getStatusOk(pnm.updateProjectNode(uid, node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the node wiki entry.
	 *
	 * @param nodeId the node id
	 * @return the node wiki entry
	 */
	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper
					.getStatusOk(mwm.getProjectNodeWikiEntry(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update node wiki entry.
	 *
	 * @param uid the uid
	 * @param wikiEntry the wiki entry
	 * @return the response
	 */
	@POST
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNodeWikiEntry(@HeaderParam("uid") int uid,
			WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm.saveProjectNodeWikiEntry(uid,
					wikiEntry));
		} catch (Exception ex) {
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
			return StatusHelper.getStatusOk(pnm.drillUp(uid, nodeId));
		} catch (Exception ex) {
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
			@PathParam("ptreeid") int treeId, @PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.drillDown(uid, treeId, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Rename node.
	 *
	 * @param treeId the tree id
	 * @param uid the uid
	 * @param projectNode the project node
	 * @return the response
	 */
	@POST
	@Path("{nodeid}/rename")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renameNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusOk(pnm.renameNode(projectNode));
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
	 * @param treeId the tree id
	 * @param uid the uid
	 * @param projectNode the project node
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			pnm.deleteProjectNode(projectNode.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
