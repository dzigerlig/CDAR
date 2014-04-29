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
import cdar.bll.producer.Node;
import cdar.bll.producer.managers.NodeManager;

@Path("ktree/{ktreeid}/nodes")
public class KnowledgeNodeController {
	private NodeManager nm = new NodeManager();

	// Nodes
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ktreeid") int ktreeid) {
		System.out.println("asdf");
		try {
			return Response
					.ok(nm.getNodes(ktreeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(nm.getNode(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("uid") int uid, Node n) {
		try {
			if (n.getTitle() == null) {
				return Response.ok(
						nm.addNode(uid, n.getKtrid(), "new Node", n.getDid()),
						MediaType.APPLICATION_JSON).build();
			} else {
				return Response
						.ok(nm.addNode(uid, n.getKtrid(), n.getTitle(),
								n.getDid()), MediaType.APPLICATION_JSON)
						.build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	// Changed
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(int id) {
		try {
			return Response.ok(new CDAR_Boolean(nm.deleteNode(id)),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed TODO rename
	@Path("drop")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dropNode(int id) {
		try {
			return Response.ok(nm.dropNode(id), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameNode(Node n) {
		try {
			return Response.ok(nm.renameNode(n), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed TODO rename
	@Path("undrop")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response undropNode(int id) {
		try {
			return Response.ok(nm.undropNode(id), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveNode(Node n) {
		try {
			return Response.ok(nm.moveNode(n), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}/zoomUp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpNode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(nm.zoomUp(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{nodeid}/zoomDown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownNode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(nm.zoomDown(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}
