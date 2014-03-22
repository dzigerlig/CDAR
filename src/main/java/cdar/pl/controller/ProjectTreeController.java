package cdar.pl.controller;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.model.knowledgeconsumer.ProjectNode;
import cdar.bll.model.knowledgeconsumer.ProjectTree;
import cdar.bll.model.knowledgeconsumer.ProjectTreeModel;

@Path("ptree")
public class ProjectTreeController {
	private ProjectTreeModel ptm = new ProjectTreeModel();
	
	@GET
	@Path("{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<ProjectTree> getProjectTreesByUid(@PathParam("uid") int uid) {
		return ptm.getProjectTreesByUid(uid);
	}
	
	@GET
	@Path("delete/{uid}/{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteTreeById(@PathParam("uid") int uid, @PathParam("ptreeid") int ptreeid) {
		return ptm.removeProjectTreeById(uid, ptreeid);
	}
	
	@GET
	@Path("copy/{uid}/{ptreeid}/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int addKnowledgeTreeToProjectTree(@PathParam("ktreeid") int ktreeid, @PathParam("ptreeid") int ptreeid) {
		ptm.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
		return 1;
	}
	
	@POST
	@Path("{uid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer addProjectTree(String treeName, @PathParam("uid") int uid) {
		return ptm.addProjectTreeByUid(uid, treeName);
	}
	
	@GET
	@Path("{uid}/{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectTree getProjectTreeById(@PathParam("ptreeid") int ptreeid, @PathParam("uid") int uid) {
		return ptm.getProjectTreeById(ptreeid);
	}
	
	@GET
	@Path("nodes/{uid}/{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<ProjectNode> getNodes(@PathParam("ptreeid") int ptreeid) {
		return ptm.getProjectNodes(ptreeid);
	}
}
