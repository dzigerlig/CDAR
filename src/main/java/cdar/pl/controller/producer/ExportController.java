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

import cdar.bll.manager.producer.XmlTreeManager;

@Path("ktrees/{ktreeid}/simpleexport")
public class ExportController {
	private XmlTreeManager xtm = new XmlTreeManager();
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getKnowledgeTreeSimpleXml(@PathParam("ktreeid") int ktreeid) {
			try {
				return Response.ok(xtm.getXmlTrees(ktreeid),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteKnowledgeTreeSimpleXml(int id) {
			try {
				return Response.ok(xtm.deleteXmlTree(id),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Path("set")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response setKnowledgeTreeSimpleXml(int id) {
			try {
				xtm.cleanTree(id);
				return Response.ok(xtm.setXmlTree(id),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@GET
		@Path("add")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addKnowledgeTreeSimpleXml(@HeaderParam("uid") int uid,
				@PathParam("ktreeid") int ktrid) {
			try {
				return Response.ok(xtm.addXmlTree(uid, ktrid),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
}
