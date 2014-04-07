package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.model.TreeModel;
import cdar.bll.model.ProjectTreeModel;
import cdar.bll.model.wiki.WikiEntry;

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
		TreeModel ktm = new TreeModel();
		return new WikiEntry(ktm.getNodeById(nodeid));
	}
	
	@POST
	@Path("/producer/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry postKnowledgeNodeWikiEntry(WikiEntry wikiEntry) {
		return wikiEntry.saveEntry();
	}
}
