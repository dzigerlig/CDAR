/*
 * 
 */
package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.NodeLink;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.pl.controller.StatusHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class ProjectNodeLinkController.
 */
@Path("ptrees/{ptreeid}/links")
public class ProjectNodeLinkController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The pnlm. */
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();

	/**
	 * Gets the node link.
	 *
	 * @param treeId the tree id
	 * @return the node link
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeLink(@PathParam("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.getProjectNodeLinks(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the node link.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param projectNodeLink the project node link
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, NodeLink projectNodeLink) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(pnlm
					.addProjectNodeLink(projectNodeLink));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update node link.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeLink the node link
	 * @return the response
	 */
	@POST
	@Path("{linkid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, NodeLink nodeLink) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusOk(pnlm.updateNodeLink(nodeLink));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill up node link.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("nodes/{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNodeLink(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.drillUp(uid, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Drill down node link.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the response
	 */
	@GET
	@Path("nodes/{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNodeLink(@HeaderParam("uid") int uid,@PathParam("ptreeid") int treeId,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.drillDown(uid, treeId, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete node link.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeLink the node link
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, NodeLink nodeLink) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			pnlm.deleteProjectNodeLink(nodeLink.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
