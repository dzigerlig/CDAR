package cdar.pl.controller;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.consumer.managers.ProjectNodeManager;
import cdar.bll.consumer.managers.ProjectTreeManager;

@Path("ptree")
public class ProjectTreeController {
	private ProjectTreeManager ptm = new ProjectTreeManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<ProjectTree> getProjectTreesByUid(@HeaderParam("uid") int uid) {
		return ptm.getProjectTrees(uid);
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteTreeById(int ptreeid) {
		return new ptm.deleteProjectTree(ptreeid);
	}
	
	@GET
	@Path("copy/{ptreeid}/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int addKnowledgeTreeToProjectTree(@PathParam("ktreeid") int ktreeid, @PathParam("ptreeid") int ptreeid) {
		ptm.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
		return 1;
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProjectTree addProjectTree(String treeTitle, @HeaderParam("uid") int uid) {
		return ptm.addProjectTree(uid, treeTitle);
	}
	
	@GET
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectTree getProjectTreeById(@PathParam("ptreeid") int ptreeid, @HeaderParam("uid") int uid) {
		return ptm.getProjectTree(ptreeid);
	}
	
	@GET
	@Path("nodes/{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<ProjectNode> getNodes(@PathParam("ptreeid") int ptreeid) {
		ProjectNodeManager pnm = new ProjectNodeManager();
		return pnm.getProjectNodes(ptreeid);
	}
}
