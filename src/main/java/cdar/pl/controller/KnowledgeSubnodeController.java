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

import cdar.bll.ChangesWrapper;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.managers.NodeLinkManager;
import cdar.bll.producer.managers.SubnodeManager;

@Path("ktree/{ktreeid}")
public class KnowledgeSubnodeController {
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager lm = new NodeLinkManager();

	// Subnodes
	@GET
	@Path("subnodes")
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
	@Path("/nodes/{nodeid}/subnodes")
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
	@Path("/nodes/{nodeid}/subnodes/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(Subnode subnode) {
		try {
			sm.renameSubnode(subnode);
			return Response
					.ok(new ChangesWrapper<NodeLink>(lm
							.getNodeLinksBySubnode(subnode.getId()), "update"),
							MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("/nodes/{nodeid}/subnodes/moveup")
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
	@Path("/nodes/{nodeid}/subnodes/movedown")
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
	@Path("/nodes/{nodeid}/subnodes/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(Subnode sn) {
		try {
			return Response.status(Response.Status.CREATED).entity(sm.addSubnode(sn.getKnid(), sn.getTitle())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("/nodes/{nodeid}/subnodes/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(int id) {
		try {
			List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(id);

			return Response.ok(
					new ChangesWrapper<NodeLink>(nodelinks, "delete"), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("nodes/{nodeid}/subnodes/zoomUp")
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
	@Path("nodes/{nodeid}/subnodes/zoomDown")
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
