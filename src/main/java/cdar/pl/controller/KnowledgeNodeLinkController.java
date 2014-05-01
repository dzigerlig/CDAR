package cdar.pl.controller;

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

@Path("ktree/{ktreeid}/links")
public class KnowledgeNodeLinkController {
	private NodeLinkManager lm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLinks(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response.ok(lm.getNodeLinks(ktreeid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(NodeLink nl) {
		try {
			return Response.status(Response.Status.CREATED).entity(
					lm.addNodeLink(nl.getTreeId(), nl.getSourceId(),
							nl.getTargetId(), nl.getSubnodeId())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(NodeLink nodeLink) {
		try {
			return Response.ok(lm.deleteNodeLink(nodeLink.getId()),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("{linkid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(NodeLink nl) {
		try {
			return Response.ok(lm.updateNodeLink(nl),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("nodeid/{nodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpLink(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(lm.zoomUp(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("nodeid/{nodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownLink(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(lm.zoomDown(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
