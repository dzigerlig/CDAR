package cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Tree;
import cdar.bll.entity.XmlTree;
import cdar.bll.manager.producer.XmlTreeManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/simpleexport")
public class ExportController {
	private XmlTreeManager xtm = new XmlTreeManager();
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getKnowledgeTreeSimpleXml(@PathParam("ktreeid") int ktreeid) {
			try {
				return StatusHelper.getStatusOk(xtm.getXmlTrees(ktreeid));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteKnowledgeTreeSimpleXml(Tree tree) {
			try {
				xtm.deleteXmlTree(tree.getId());
				return StatusHelper.getStatusOk(null);
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("set")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response setKnowledgeTreeSimpleXml(XmlTree xmlTree) {
			try {
				xtm.cleanTree(xmlTree.getId());
				return StatusHelper.getStatusOk(xtm.setXmlTree(xmlTree.getId()));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@GET
		@Path("add")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addKnowledgeTreeSimpleXml(@HeaderParam("uid") int uid,
				@PathParam("ktreeid") int ktrid) {
			try {
				return StatusHelper.getStatusOk(xtm.addXmlTree(uid, ktrid));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
}
