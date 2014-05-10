package cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.TreeXml;
import cdar.bll.manager.importexport.ProducerImportExportManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/exports")
public class TreeImportExportController {
	private ProducerImportExportManager piem = new ProducerImportExportManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getXmlTrees(@PathParam("ktreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(piem.getXmlTrees(treeId));
		} catch (Exception e) {  
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{xmltreeid}/filexml")
	@Produces(MediaType.APPLICATION_XML)
	public Response getXmlFileString(@PathParam ("xmltreeid") int xmlTreeId) {
		try {
			return StatusHelper.getStatusOk(piem.getXmlTree(xmlTreeId).getXmlString());
		} catch (UnknownXmlTreeException | EntityException e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addXmlTree(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, TreeXml treeXml) {
		try {
			if (treeXml.getIsFull()) {
				return StatusHelper.getStatusCreated(piem.addXmlTreeFull(uid, treeId, treeXml.getTitle(), treeXml.getXmlString()));
			} else {
				return StatusHelper.getStatusCreated(piem.addXmlTreeSimple(uid, treeId, treeXml.getTitle(), treeXml.getXmlString()));
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
			return StatusHelper.getStatusOk(piem.getXmlTree(xmlTreeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{xmltreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateXmlTree(TreeXml treeXml) {
		try {
			return StatusHelper.getStatusOk(piem.updateXmlTree(treeXml));
		} catch (Exception e) {
			e.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{xmltreeid}/set")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setXmlTree(@PathParam("xmltreeid") int xmlTreeId, @QueryParam("cleantree") boolean cleantree) {
		try {
			piem.setXmlTree(xmlTreeId, cleantree);
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
			piem.deleteXmlTree(treeXml.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
