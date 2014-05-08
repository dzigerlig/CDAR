package cdar.pl.controller.consumer;

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
import cdar.bll.entity.TreeXml;
import cdar.bll.entity.consumer.CreationTree;
import cdar.bll.manager.importexport.ConsumerImportExportManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{treeid}/exports")
public class ProjectTreeSimpleExportController {
	ConsumerImportExportManager ciem = new ConsumerImportExportManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getXmlTrees(@PathParam("treeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(ciem.getXmlTrees(treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addXmlTree(@HeaderParam("uid") int uid, @PathParam("treeid") int treeId, TreeXml treeXml) {
		try {
			if (treeXml.getIsFull()) {
				return StatusHelper.getStatusCreated(ciem.addXmlTreeFull(uid, treeId, treeXml.getTitle()));
			} else {
				return StatusHelper.getStatusCreated(ciem.addXmlTreeSimple(uid, treeId, treeXml.getTitle()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{xmltreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getXmlTree(@PathParam("xmltreeid") int xmlTreeId, @HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ciem.getXmlTree(xmlTreeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{xmltreeid}/set")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setXmlTree(@PathParam("xmltreeid") int xmlTreeId) {
		try {
			ciem.setXmlTree(xmlTreeId);
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteXmlTree(TreeXml treeXml) {
		try {
			ciem.deleteXmlTree(treeXml.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
