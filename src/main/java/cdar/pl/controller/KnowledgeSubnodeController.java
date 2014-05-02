package cdar.pl.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.ChangesWrapper;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.dal.exceptions.UnknownUserException;

@Path("ktrees/{ktreeid}/nodes/{nodeid}/subnodes")
public class KnowledgeSubnodeController {
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager lm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeId) {
		try {
			return Response.ok(sm.getSubnodesFromNode(nodeId), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(Subnode subnode) {
		try {
			return Response.status(Response.Status.CREATED)
					.entity(sm.addSubnode(subnode.getNodeId(), subnode.getTitle())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editSubnode(Subnode subnode) {
		try {
			return Response.ok(sm.updateSubnode(subnode), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode(@PathParam("subnodeid") int subnodeId) {
		try {
			return Response.ok(sm.getSubnode(subnodeId), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(Subnode subnode) {
		try {
			sm.renameSubnode(subnode);
			return Response
					.ok(new ChangesWrapper<NodeLink>(lm
							.getNodeLinksBySubnode(subnode.getId()), "update"),
							MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}


	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(Subnode subnode) {
		try {
			List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(subnode.getId());
			sm.deleteSubnode(subnode.getId());
			return Response.ok(
					new ChangesWrapper<NodeLink>(nodelinks, "delete"),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("{subnodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return Response.ok(sm.zoomUp(nodeId), MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("{subnodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return Response.ok(sm.zoomDown(nodeId), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("{subnodeid}/wiki")
	public Response getKnowledgeSubnodeWikiEntry(@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return Response.ok(mwm.getKnowledgeSubnodeWikiEntry(subnodeId), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("{subnodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postKnowledgeSubnodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return Response.ok(mwm.saveKnowledgeSubnodeWikiEntry(uid, wikiEntry), MediaType.APPLICATION_JSON).build();
		} catch (UnknownUserException uue) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
