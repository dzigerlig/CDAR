package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.consumer.ProjectTreeModel;
import cdar.bll.producer.NodeModel;
import cdar.bll.producer.SubnodeModel;
import cdar.bll.producer.TreeModel;
import cdar.bll.wiki.WikiEntry;

@Path("{uid}/wiki")
public class WikiController {
	@GET
	@Path("/consumer/node/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getProjectNodeWikiEntry(@PathParam("nodeid") int nodeid) {
		ProjectTreeModel ptm = new ProjectTreeModel();
		return new WikiEntry(ptm.getProjectNodeById(nodeid));
	}
	
	@GET
	@Path("/producer/node/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeid) {
		NodeModel nm = new NodeModel();
		return new WikiEntry(nm.getNode(nodeid));
	}
	
	@GET
	@Path("/producer/subnode/{subnodeid}")
	public WikiEntry getKnowledgeSubnodeWikiEntry(@PathParam("subnodeid") int subnodeid) {
		SubnodeModel sm = new SubnodeModel();
		return new WikiEntry(sm.getSubnode(subnodeid));
	}
	
	@POST
	@Path("/producer/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry postKnowledgeNodeWikiEntry(WikiEntry wikiEntry) {
		return wikiEntry.saveEntry();
	}
	
	@POST
	@Path("/producer/subnode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry postKnowldgeSubnodeWikiEntry(WikiEntry wikiEntry) {
		return wikiEntry.saveEntry();
	}
}
