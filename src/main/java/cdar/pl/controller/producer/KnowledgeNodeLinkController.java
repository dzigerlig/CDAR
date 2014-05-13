package cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.NodeLink;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/links")
public class KnowledgeNodeLinkController {
	private NodeLinkManager lm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLinks(@PathParam("ktreeid") int ktreeid) {
		try {
			return StatusHelper.getStatusOk(lm.getNodeLinks(ktreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	// Changed
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(NodeLink nodeLink) {
		try {
			return StatusHelper.getStatusCreated(lm.addNodeLink(nodeLink));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(NodeLink nodeLink) {
		try {
			lm.deleteNodeLink(nodeLink.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{linkid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(NodeLink nodeLink) {
		try {
			return StatusHelper.getStatusOk(lm.updateNodeLink(nodeLink));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodes/{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpLink(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(lm.drillUp(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodes/{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownLink(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(lm.drillDown(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
