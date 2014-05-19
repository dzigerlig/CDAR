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

import cdar.bll.entity.producer.Template;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/templates")
public class TemplateController {
	private final boolean ISPRODUCER = true;
	private LockingManager lm = new LockingManager();
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
	public Response addTemplate(@HeaderParam("uid") int uid,@PathParam("ktreeid") int treeId, Template template) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(tm
					.addKnowledgeTemplate(template));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{templateid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTemplate(@PathParam("ktreeid") int treeId,
			@PathParam("templateid") int templateId) {
		try {
			return StatusHelper
					.getStatusOk(tm.getKnowledgeTemplate(templateId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{templateid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTemplate(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, @PathParam("templateid") int templateId,
			Template template) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			template.setId(templateId);
			return StatusHelper.getStatusOk(tm.updateTemplate(template));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTemplate(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, Template template) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			tm.deleteTemplate(template.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
