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
				return StatusHelper.getStatusOk(tm.getKnowledgeTemplates(ktreeid));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addTemplate(Template template) {
			try {
				return StatusHelper.getStatusCreated(tm.addKnowledgeTemplate(template.getTreeId(),template.getTitle(), template.getTemplatetext(),template.getDecisionMade()));
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
				return StatusHelper.getStatusOk(tm.getKnowledgeTemplate(templateid));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("{templateid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateTemplate(Template template) {
			try {
				return StatusHelper.getStatusOk(tm.updateTemplate(template));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteTemplate(Template template) {
			try {
				tm.deleteTemplate(template.getId());
				return StatusHelper.getStatusOk(null);
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
}
