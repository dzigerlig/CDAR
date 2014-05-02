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
	public Response addNodeLink(NodeLink nl) {
		try {
			return StatusHelper.getStatusCreated(lm.addNodeLink(nl.getTreeId(), nl.getSourceId(), nl.getTargetId(), nl.getSubnodeId()));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(NodeLink nodeLink) {
		try {
			return StatusHelper.getStatusOk(lm.deleteNodeLink(nodeLink.getId()));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{linkid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(NodeLink nl) {
		try {
			return StatusHelper.getStatusOk(lm.updateNodeLink(nl));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodeid/{nodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpLink(@PathParam("nodeid") int nodeid) {
		try {
			return StatusHelper.getStatusOk(lm.zoomUp(nodeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodeid/{nodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownLink(@PathParam("nodeid") int nodeid) {
		try {
			return StatusHelper.getStatusOk(lm.zoomDown(nodeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
