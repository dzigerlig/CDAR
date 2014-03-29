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

import cdar.bll.model.DictionaryModel;
import cdar.bll.model.TreeModel;
import cdar.bll.model.NodeLinkModel;
import cdar.bll.model.NodeModel;
import cdar.bll.producer.Dictionary;
import cdar.bll.producer.KnowledgeNode;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Tree;

@Path("{uid}/ktree")
public class TreeController {
	private TreeModel ktm = new TreeModel();
	private NodeModel nM= new NodeModel();
	private NodeLinkModel lM= new NodeLinkModel();
	private DictionaryModel dM = new DictionaryModel();
	
	//Dynamic Tree
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Tree> getKnowledgeTreesByUid(@PathParam("uid") int uid) {
		return ktm.getKnowledgeTreesByUid(uid);
	}
	
	@GET
	@Path("delete/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public int deleteTreeById(@PathParam("uid") int uid, @PathParam("ktreeid") int ktreeid) {
		return ktm.removeKnowledgeTreeById(uid, ktreeid);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer addKnowledgeTree(String treeName, @PathParam("uid") int uid) {
		return ktm.addKnowledgeTreeByUid(uid, treeName);
	}
	
	@GET
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tree getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		return ktm.getKnowledgeTreeById(ktreeid);
	}
	
	//Static Tree
	@GET
	@Path("/dictionaries/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Dictionary> getDictionaries(@PathParam("ktreeid") int ktreeid) {
		return dM.getDictionaries();
	}
	
	@POST
	@Path("dictionaries/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Dictionary addDictionary(Dictionary d) {
		return dM.addDictionary(d);
	}
	
	@POST
	@Path("dictionaries/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeDictionary(int id) {
		dM.removeDictionaryById(id);
		return Response.status(200).build();
	}
	
	
	//Nodes
	@GET
	@Path("/nodes/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> getNodes(@PathParam("ktreeid") int ktreeid) {
		return nM.getNodes();
	}
	
	@POST
	@Path("nodes/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node addNode(Node n) {
		System.out.println(n.getTitle()+": "+n.getRefTreeId());
		return nM.addNode(n);
	}
	
	@POST
	@Path("nodes/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNode(int id) {
		nM.removeNodeById(id);
		return Response.status(200).build();
	}
	
	//Links
	@GET
	@Path("links/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> getLinks(@PathParam("ktreeid") int ktreeid) {
		return lM.getLinks();
	}		
	
	@POST
	@Path("links/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addLink(NodeLink nl) {		 
		return lM.addLink(nl);
	}

	@POST
	@Path("links/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeLink(int id) {
		lM.removeLinkById(id);
		return Response.status(200).build();
	}
}
