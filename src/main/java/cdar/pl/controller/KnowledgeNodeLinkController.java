package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.CDAR_Boolean;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.managers.NodeLinkManager;

@Path("ktree/{ktreeid}/links")
public class KnowledgeNodeLinkController {
	private NodeLinkManager lm = new NodeLinkManager();

	// Links
	@GET
	// Changed
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
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(NodeLink nl) {
		try {
			return Response.ok(
					lm.addNodeLink(nl.getKtrid(), nl.getSourceId(),
							nl.getTargetId(), nl.getKsnid()),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(int id) {
		try {
			return Response.ok(new CDAR_Boolean(lm.deleteNodeLink(id)),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(NodeLink nl) {
		try {
			return Response.ok(lm.updateNodeLink(nl),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}/zoomUp")
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
	@Path("{nodeid}/zoomDown")
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
