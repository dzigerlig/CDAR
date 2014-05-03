package cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.producer.Template;
import cdar.bll.manager.producer.TemplateManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/templates")
public class TemplateController {
	private TemplateManager tm = new TemplateManager();

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getTemplates(@PathParam("ktreeid") int ktreeid) {
			try {
				return Response.ok(tm.getKnowledgeTemplates(ktreeid), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addTemplate(Template template) {
			try {
				return Response.status(Response.Status.CREATED).entity(tm.addKnowledgeTemplate(template.getTreeId(),template.getTitle(), template.getTemplatetext(),template.getDecisionMade())).build();
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
		
		@GET
		@Path("{templateid}")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getTemplate(@PathParam("ktreeid") int ktreeid,
				@PathParam("templateid") int templateid) {
			try {
				return Response.ok(tm.getKnowledgeTemplate(templateid), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("{templateid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateTemplate(@PathParam("templateid") int templateid,Template template) {
			template.setId(templateid);
			try {
				return Response.ok(tm.updateTemplate(template), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteTemplate(Template template) {
			try {
				return Response.ok(tm.deleteTemplate(template.getId()), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
}
