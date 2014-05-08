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

import cdar.bll.entity.Tree;
import cdar.bll.entity.consumer.CreationTree;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{treeid}/simpleexport")
public class ProjectTreeSimpleExportController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreesByUid(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getProjectTrees(uid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectTree(CreationTree tree, @HeaderParam("uid") int uid) {
		try {
			int knowledgeTreeId = tree.getCopyTreeId();
			tree.setUserId(uid);
			Tree newTree = ptm.addProjectTree(tree);
			ptm.addKnowledgeTreeToProjectTree(knowledgeTreeId, newTree.getId());
			return StatusHelper.getStatusCreated(newTree);
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreeById(@PathParam("ptreeid") int ptreeid, @HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getProjectTree(ptreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProjectTree(@PathParam("ptreeid") int treeId,Tree tree) {
		try {	
			tree.setId(treeId);
			return StatusHelper.getStatusOk(ptm.updateProjectTree(tree));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTree(Tree tree) {
		try {
			ptm.deleteProjectTree(tree.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
