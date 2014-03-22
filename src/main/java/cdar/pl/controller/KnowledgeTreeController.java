package cdar.pl.controller;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.model.knowledgeproducer.KnowledgeTreeModel;
import cdar.bll.model.knowledgeproducer.Tree;

@Path("ktree")
public class KnowledgeTreeController {
	private KnowledgeTreeModel ktm = new KnowledgeTreeModel();
	
	@GET
	@Path("{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Tree> getKnowledgeTreesByUid(@PathParam("uid") int uid) {
		return ktm.getKnowledgeTreesByUid(uid);
	}
	
	@GET
	@Path("delete/{uid}/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteTreeById(@PathParam("uid") int uid, @PathParam("ktreeid") int ktreeid) {
		return ktm.removeKnowledgeTreeById(uid, ktreeid);
	}
	
	@POST
	@Path("{uid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer addKnowledgeTree(String treeName, @PathParam("uid") int uid) {
		return ktm.addKnowledgeTreeByUid(uid, treeName);
	}
	
	@GET
	@Path("{uid}/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tree getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		return ktm.getKnowledgeTreeById(ktreeid);
	}
}
