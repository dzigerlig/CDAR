package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.consumer.models.ProjectNodeModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;

@Path("{uid}/wiki")
public class WikiController {
	private MediaWikiModel mwm = new MediaWikiModel();
	
	@GET
	@Path("/consumer/node/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getProjectNodeWikiEntry(@PathParam("nodeid") int nodeid) {
		return mwm.getProjectNodeWikiEntry(nodeid);
	}
	
	@GET
	@Path("/producer/node/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeid) {
		return mwm.getKnowledgeNodeWikiEntry(nodeid);
	}
	
	@GET
	@Path("/producer/subnode/{subnodeid}")
	public WikiEntry getKnowledgeSubnodeWikiEntry(@PathParam("subnodeid") int subnodeid) {
		return mwm.getKnowledgeSubnodeWikiEntry(subnodeid);
	}
	
	@POST
	@Path("/producer/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry postKnowledgeNodeWikiEntry(WikiEntry wikiEntry) {
		return mwm.saveKnowledgeNodeWikiEntry(wikiEntry);
	}
	
	@POST
	@Path("/producer/subnode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry postKnowledgeSubnodeWikiEntry(WikiEntry wikiEntry) {
		return mwm.saveKnowledgeSubnodeWikiEntry(wikiEntry);
	}
}
