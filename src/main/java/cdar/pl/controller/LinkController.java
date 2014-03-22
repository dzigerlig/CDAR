package cdar.pl.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.model.knowledgeproducer.LinkModel;
import cdar.bll.model.knowledgeproducer.Node;
import cdar.bll.model.knowledgeproducer.NodeLink;

@Path("links")
public class LinkController {
	private LinkModel linkModel = new LinkModel();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<NodeLink> getNodes() {
		return linkModel.getLinks();
	}
	
	@POST
	@Path("/addLink")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addLink(NodeLink nl) {
		 
		// System.out.println(targetId);
		//System.out.println(ln.getSourceId());
		return linkModel.addLink(nl);
	}

	@POST
	@Path("/removeLink")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeLink(int id) {
		linkModel.removeLinkById(id);
		return Response.status(200).build();
	}
}
