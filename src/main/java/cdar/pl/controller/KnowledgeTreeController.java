package cdar.pl.controller;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.model.knowledgeproducer.KnowledgeNode;
import cdar.bll.model.knowledgeproducer.KnowledgeTreeModel;
import cdar.bll.model.knowledgeproducer.NodeLinkModel;
import cdar.bll.model.knowledgeproducer.Node;
import cdar.bll.model.knowledgeproducer.NodeLink;
import cdar.bll.model.knowledgeproducer.NodeModel;
import cdar.bll.model.knowledgeproducer.Tree;

@Path("{uid}")
public class KnowledgeTreeController {
	private KnowledgeTreeModel ktm = new KnowledgeTreeModel();
	private NodeModel nM= new NodeModel();
	private NodeLinkModel lM= new NodeLinkModel();
	
	@GET
	@Path("ktree")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Tree> getKnowledgeTreesByUid(@PathParam("uid") int uid) {
		return ktm.getKnowledgeTreesByUid(uid);
	}
	
	@GET
	@Path("ktree/delete/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteTreeById(@PathParam("uid") int uid, @PathParam("ktreeid") int ktreeid) {
		return ktm.removeKnowledgeTreeById(uid, ktreeid);
	}
	
	@POST
	@Path("ktree")
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer addKnowledgeTree(String treeName, @PathParam("uid") int uid) {
		return ktm.addKnowledgeTreeByUid(uid, treeName);
	}
	
	@GET
	@Path("ktree/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tree getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		return ktm.getKnowledgeTreeById(ktreeid);
	}
	
	//Nodes
	@GET
	@Path("ktree/nodes/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> getNodes(@PathParam("ktreeid") int ktreeid) {
		return nM.getNodes();
	}
	
	@POST
	@Path("ktree/nodes/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node addNode(Node n) {
		System.out.println(n.getTitle()+": "+n.getRefTreeId());
		return nM.addNode(n);
	}
	
	@POST
	@Path("ktree/nodes/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNode(int id) {
		nM.removeNodeById(id);
		return Response.status(200).build();
	}
	
	//Links
	@GET
	@Path("ktree/links/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> getLinks(@PathParam("ktreeid") int ktreeid) {
		return lM.getLinks();
	}		
	
	@POST
	@Path("ktree/links/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addLink(NodeLink nl) {		 
		return lM.addLink(nl);
	}

	@POST
	@Path("ktree/links/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeLink(int id) {
		lM.removeLinkById(id);
		return Response.status(200).build();
	}
}
