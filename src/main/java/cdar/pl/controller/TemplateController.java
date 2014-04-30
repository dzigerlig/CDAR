package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.producer.Template;
import cdar.bll.producer.managers.TemplateManager;

@Path("ktree/{ktreeid}/templates")
public class TemplateController {
	private TemplateManager tm = new TemplateManager();
	// TEMPLATES
		@GET
		// Changed
		@Produces(MediaType.APPLICATION_JSON)
		public Response getTemplates(@PathParam("ktreeid") int ktreeid) {
			try {
				return Response.ok(tm.getKnowledgeTemplates(ktreeid),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@GET
		// Changed
		@Path("{templateid}")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getTemplate(@PathParam("ktreeid") int ktreeid,
				@PathParam("templateid") int templateid) {
			try {
				return Response.ok(tm.getKnowledgeTemplate(templateid),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		// Changed
		@Path("add")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addTemplate(Template template) {

			try {
				return Response.ok(
						tm.addKnowledgeTemplate(template.getTreeId(),
								template.getTitle(), template.getTemplatetext(),
								template.getDecisionMade()),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

		}

		@POST
		// Changed
		@Path("rename")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response renameTemplate(Template template) {
			try {
				return Response.ok(tm.renameTemplate(template),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Path("default")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response setDefaultTemplate(@PathParam("ktreeid") int ktreeid,
				int templateId) {
			try {
				return Response.ok(tm.setDefaultTemplate(ktreeid, templateId),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		// Changed
		@Path("{ktreeid}/templates/edit")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response editTemplate(Template template) {
			try {
				return Response.ok(tm.updateTemplate(template),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		}

		@POST
		// Changed
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteTemplate(int id) {
			try {
				return Response.ok(tm.deleteTemplate(id),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

}
