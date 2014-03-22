package cdar.pl.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cdar.bll.model.knowledgeconsumer.ProjectTreeModel;
import cdar.bll.model.knowledgeproducer.KnowledgeTreeModel;
import cdar.bll.model.wiki.WikiEntry;

@Path("wiki")
public class WikiController {
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public WikiEntryOld getWiki() {
//		return new WikiEntryOld("Test");
//	}
//	
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Integer editWiki(String text) {
//		try {
//			Wiki c = new Wiki();
//			System.out.println(text);
//			c.login("admin", "password");
//			c.edit("Test", text, "");
//			return 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}
	
	@GET
	@Path("/consumer/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getProjectNodeWikiEntry(@PathParam("nodeid") int nodeid) {
		ProjectTreeModel ptm = new ProjectTreeModel();
		System.out.println(nodeid);
		System.out.println(ptm.getProjectNodeById(nodeid).getTitle());
		return new WikiEntry(ptm.getProjectNodeById(nodeid));
	}
	
	@GET
	@Path("/producer/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public WikiEntry getKnowledgeNodeWikiEntry(@QueryParam("nodeid") int nodeid) {
		KnowledgeTreeModel ktm = new KnowledgeTreeModel();
		return new WikiEntry(ktm.getKnowledgeNodeById(nodeid));
	}
}
