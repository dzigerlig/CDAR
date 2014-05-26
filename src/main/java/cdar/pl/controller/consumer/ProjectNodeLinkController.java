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

@Path("ptrees/{ptreeid}/links")
public class ProjectNodeLinkController {
	private final boolean ISPRODUCER = false;
	private LockingManager lm = new LockingManager();
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeLink(@PathParam("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.getProjectNodeLinks(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

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
