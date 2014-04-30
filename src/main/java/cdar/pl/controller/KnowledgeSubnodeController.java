package cdar.pl.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.CDAR_BooleanChanges;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.managers.NodeLinkManager;
import cdar.bll.producer.managers.SubnodeManager;

@Path("ktree/{ktreeid}/subnodes")
public class KnowledgeSubnodeController {
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager lm = new NodeLinkManager();

	// Subnodes
	@GET
	// Changed
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodesByTree(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response.ok(sm.getSubnodesFromTree(ktreeid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(sm.getSubnodesFromNode(nodeid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(Subnode subnode) {
		try {
			sm.renameSubnode(subnode);
			return Response
					.ok(new CDAR_BooleanChanges<NodeLink>(lm
							.getNodeLinksBySubnode(subnode.getId()), "update"),
							MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("moveup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveSubnodeUp(Subnode subnode) {
		try {
			return Response.ok(sm.moveSubnodeUp(subnode),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("movedown")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveSubnodeDown(Subnode subnode) {
		try {
			return Response.ok(sm.moveSubnodeDown(subnode),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(Subnode sn) {
		try {
			return Response.ok(sm.addSubnode(sn.getKnid(), sn.getTitle()),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(int id) {
		try {
			List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(id);

			return Response.ok(
					new CDAR_BooleanChanges<NodeLink>(nodelinks, "delete"), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}/zoomUp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpSubnode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(sm.zoomUp(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}/zoomDown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownSubnode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(sm.zoomDown(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
