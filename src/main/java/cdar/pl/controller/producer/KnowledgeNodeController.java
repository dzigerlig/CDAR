package cdar.pl.controller.producer;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Node;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/nodes")
public class KnowledgeNodeController {
	private NodeManager nm = new NodeManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response
					.ok(nm.getNodes(ktreeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@HeaderParam("uid") int uid, Node node) {
		try {
			if (node.getTitle() == null) {
				return Response
						.status(Response.Status.CREATED)
						.entity(nm.addNode(uid, node.getTreeId(), "new Node",
								node.getDirectoryId())).build();
			} else {
				return Response
						.status(Response.Status.CREATED)
						.entity(nm.addNode(uid, node.getTreeId(), node.getTitle(),
								node.getDirectoryId()))
						.build();
			}
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeid) {
		try {
			return Response.ok(nm.getNode(nodeid), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(Node node) {
		try {
			return Response.ok(nm.updateNode(node), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(Node node) {
		try {
			return Response.ok(nm.deleteNode(node.getId()), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpNode(@PathParam("nodeid") int nodeId) {
		try {
			return Response.ok(nm.zoomUp(nodeId), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownNode(@PathParam("nodeid") int nodeId) {
		try {
			return Response.ok(nm.zoomDown(nodeId), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return Response.ok(mwm.getKnowledgeNodeWikiEntry(nodeId), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postKnowledgeNodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return Response.ok(mwm.saveKnowledgeNodeWikiEntry(uid, wikiEntry), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
