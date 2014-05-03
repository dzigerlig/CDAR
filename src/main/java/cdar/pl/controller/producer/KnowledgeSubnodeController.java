package cdar.pl.controller.producer;

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
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/nodes/{nodeid}/subnodes")
public class KnowledgeSubnodeController {
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager lm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnodesFromNode(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(Subnode subnode) {
		try {
			return StatusHelper.getStatusCreated(sm.addSubnode(subnode));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnode(@PathParam("subnodeid") int subnodeId,Subnode subnode) {
		subnode.setId(subnodeId);
		try {
			return StatusHelper.getStatusOk(sm.updateSubnode(subnode));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode(@PathParam("subnodeid") int subnodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnode(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(Subnode subnode) {
		try {
			sm.renameSubnode(subnode);
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(lm
							.getNodeLinksBySubnode(subnode.getId()), "update"));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}


	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(Subnode subnode) {
		try {
			List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(subnode.getId());
			sm.deleteSubnode(subnode.getId());
			return StatusHelper.getStatusOk(
					new ChangesWrapper<NodeLink>(nodelinks, "delete"));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{subnodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.zoomUp(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{subnodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.zoomDown(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{subnodeid}/wiki")
	public Response getKnowledgeSubnodeWikiEntry(@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.getKnowledgeSubnodeWikiEntry(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{subnodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeSubnodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeSubnodeWikiEntry(uid, wikiEntry));
		} catch (UnknownUserException uue) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
