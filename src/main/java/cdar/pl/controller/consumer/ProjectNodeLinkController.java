package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.NodeLink;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/links")
public class ProjectNodeLinkController {
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeLink(@PathParam ("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.getProjectNodeLinks(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(NodeLink projectNodeLink) {
		try {
			return StatusHelper.getStatusCreated(pnlm.addProjectNodeLink(projectNodeLink));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("nodes/{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNodeLink(@PathParam ("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.drillUp(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("nodes/{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNodeLink(@PathParam ("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnlm.drillDown(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(NodeLink nodeLink) {
		try {
			pnlm.deleteProjectNodeLink(nodeLink.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
