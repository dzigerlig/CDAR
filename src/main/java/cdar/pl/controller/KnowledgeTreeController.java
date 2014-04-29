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

import cdar.bll.CDAR_Boolean;
import cdar.bll.producer.Tree;
import cdar.bll.producer.managers.TreeManager;
import cdar.bll.producer.managers.XmlTreeManager;

@Path("ktree")
public class KnowledgeTreeController {
	private TreeManager ktm = new TreeManager();
	private XmlTreeManager xtm = new XmlTreeManager();

	// Dynamic Tree
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTreesByUid(@HeaderParam("uid") int uid) {
		try {
			return Response.ok(ktm.getTrees(uid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTreeById(int ktreeid) {
		try {
			return Response.ok(new CDAR_Boolean(ktm.deleteTree(ktreeid)),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("tree/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTree(String treeTitle, @HeaderParam("uid") int uid) {
		try {
			return Response.ok(ktm.addTree(uid, treeTitle),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("tree/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response renameKnowledgeTree(Tree tree) {
		try {
			return Response
					.ok(ktm.updateTree(tree), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
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

	
	// TREE XML
	@GET
	// Changed
	@Path("{ktreeid}/simpleexport")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeTreeSimpleXml(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response.ok(xtm.getXmlTrees(ktreeid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("{ktreeid}/simpleexport/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteKnowledgeTreeSimpleXml(int id) {
		try {
			return Response.ok(new CDAR_Boolean(xtm.deleteXmlTree(id)),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	// Changed
	@Path("{ktreeid}/singleexport/set")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setKnowledgeTreeSimpleXml(int id) {
		try {
			xtm.cleanTree(id);
			return Response.ok(new CDAR_Boolean(xtm.setXmlTree(id)),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	// Changed
	@Path("{ktreeid}/simpleexport/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTreeSimpleXml(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int ktrid) {
		try {
			return Response.ok(xtm.addXmlTree(uid, ktrid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
