package cdar.pl.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.model.knowledgeproducer.Node;
import cdar.bll.model.knowledgeproducer.NodeModel;

@Path("nodes")
public class NodeController{
	private NodeModel nodeModel = new NodeModel();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Node> getNodes() {
		return nodeModel.getNodes();
	}
	
	@GET
	@Path("/addNode")
	@Produces(MediaType.APPLICATION_JSON)
	public Node addNode() {
		return nodeModel.addNode();
	}
	
	@POST
	@Path("/removeNode")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNode(int id) {
		nodeModel.removeNodeById(id);
		return  Response.status(200).build();
	}
}
