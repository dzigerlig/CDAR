package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Tree;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TreeManager;

@Path("ktrees")
public class KnowledgeTreeController {
	private TreeManager ktm = new TreeManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTrees(@HeaderParam("uid") int uid) {
		try {
			return Response.ok(ktm.getTrees(uid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTree(Tree tree) {
		try {
			return Response.ok(ktm.deleteTree(tree.getId()),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTree(Tree tree, @HeaderParam("uid") int uid) {
		try {
			return Response.status(Response.Status.CREATED).entity(ktm.addTree(uid, tree.getTitle())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response
					.ok(ktm.getTree(ktreeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeTree(Tree tree) {
		try {
			return Response.ok(ktm.updateTree(tree), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("subnodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodesByTree(@PathParam("ktreeid") int treeId) {
		try {
			SubnodeManager sm = new SubnodeManager();
			return Response.ok(sm.getSubnodesFromTree(treeId),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
