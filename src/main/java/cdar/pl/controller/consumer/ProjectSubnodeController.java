package cdar.pl.controller.consumer;

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
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/nodes/{nodeid}/subnodes")
public class ProjectSubnodeController {
	ProjectSubnodeManager psm = new ProjectSubnodeManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(psm.getProjectSubnodesFromProjectNode(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(ProjectSubnode subnode) {
		try {
			return StatusHelper.getStatusCreated(psm.addProjectSubnode(subnode.getNodeId(), subnode.getTitle()));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode(@PathParam("subnodeid") int subnodeId) {
		try {
			return StatusHelper.getStatusOk(psm.getProjectSubnode(subnodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnode(ProjectSubnode subnode) {
		try {
			return StatusHelper.getStatusOk(psm.updateProjectSubnode(subnode));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodeWikiEntry(@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.getKnowledgeProjectSubnodeWikiEntry(subnodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeProjectSubnodeWikiEntry(uid, wikiEntry));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(psm.zoomUp(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownSubnode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(psm.zoomDown(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("rename")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renameSubnode(ProjectSubnode projectSubnode) {
		try {
			ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
			psm.updateProjectSubnode(projectSubnode);
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(pnlm.getProjectNodeLinksBySubnode(projectSubnode.getId()), "update"));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubnode(ProjectSubnode projectSubnode) {
		try {
			ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
			List<NodeLink> nodelinks = pnlm.getProjectNodeLinksBySubnode(projectSubnode.getId());
			psm.deleteProjectSubnode(projectSubnode.getId());
			return StatusHelper.getStatusOk(
					new ChangesWrapper<NodeLink>(nodelinks, "delete"));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
