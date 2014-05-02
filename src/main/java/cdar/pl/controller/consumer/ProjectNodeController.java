package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/nodes")
public class ProjectNodeController {
	private ProjectNodeManager pnm = new ProjectNodeManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam ("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnm.getProjectNodes(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(ProjectNode node) {
		try {
			return StatusHelper.getStatusCreated(pnm.addProjectNode(node.getTreeId(), node.getTitle(), node.getDirectoryId()));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam ("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.getProjectNode(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNode(ProjectNode node) {
		try {
			return StatusHelper.getStatusOk(pnm.updateProjectNode(node));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.getProjectNodeWikiEntry(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.saveProjectNodeWikiEntry(uid, wikiEntry));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpNode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.zoomUp(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownNode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.zoomDown(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNode(ProjectNode node) {
		try {
			return StatusHelper.getStatusOk(pnm.deleteProjectNode(node.getId()));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
