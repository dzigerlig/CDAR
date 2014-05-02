package cdar.pl.controller.consumer;

import java.util.Set;

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
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.manager.consumer.ProjectTreeManager;

@Path("ptrees")
public class ProjectTreeController {
	private ProjectTreeManager ptm = new ProjectTreeManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreesByUid(@HeaderParam("uid") int uid) {
		return ptm.getProjectTrees(uid);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectTree(String treeTitle, @HeaderParam("uid") int uid) {
		return ptm.addProjectTree(uid, treeTitle);
	}
	
	@GET
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreeById(@PathParam("ptreeid") int ptreeid, @HeaderParam("uid") int uid) {
		return ptm.getProjectTree(ptreeid);
	}
	
	@POST
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProjectTree(Tree tree) {
		return ptm.updateProjectTree(tree);
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTreeById(int ptreeid) {
		return new ptm.deleteProjectTree(ptreeid);
	}
	
	@GET
	@Path("{ptreeid}/ktrees/{ktreeid}/copy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTreeToProjectTree(@PathParam("ktreeid") int ktreeid, @PathParam("ptreeid") int ptreeid) {
		ptm.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
		return 1;
	}
}
