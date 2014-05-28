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

/**
 * The Class TreeImportExportController.
 */
@Path("ktrees/{ktreeid}/exports")
public class TreeImportExportController {
	
	/** The piem. */
	private ProducerImportExportManager piem = new ProducerImportExportManager();
	
	/**
	 * Gets the xml trees.
	 *
	 * @param treeId the tree id
	 * @return the xml trees
	 */
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
	
	/**
	 * Gets the xml file string.
	 *
	 * @param xmlTreeId the xml tree id
	 * @return the xml file string
	 */
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
	
	/**
	 * Adds the xml tree.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param treeXml the tree xml
	 * @return the response
	 */
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
	
	/**
	 * Gets the xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @param uid the uid
	 * @return the xml tree
	 */
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
	
	/**
	 * Update xml tree.
	 *
	 * @param treeXml the tree xml
	 * @return the response
	 */
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
	
	/**
	 * Sets the xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @param cleantree the cleantree
	 * @return the response
	 */
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
	
	/**
	 * Delete xml tree.
	 *
	 * @param treeXml the tree xml
	 * @return the response
	 */
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
