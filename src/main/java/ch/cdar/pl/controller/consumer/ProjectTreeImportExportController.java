package ch.cdar.pl.controller.consumer;

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

import ch.cdar.bll.entity.TreeXml;
import ch.cdar.bll.manager.importexport.ConsumerImportExportManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownXmlTreeException;
import ch.cdar.pl.controller.StatusHelper;

/**
 * The Class ProjectTreeImportExportController.
 */
@Path("ptrees/{treeid}/exports")
public class ProjectTreeImportExportController {
	
	/** The ciem. */
	private ConsumerImportExportManager ciem = new ConsumerImportExportManager();

	/**
	 * Gets the xml trees.
	 *
	 * @param treeId the tree id
	 * @return the xml trees
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getXmlTrees(@PathParam("treeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(ciem.getXmlTrees(treeId));
		} catch (Exception e) {  
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
		ConsumerImportExportManager ciem = new ConsumerImportExportManager();
		try {
			return StatusHelper.getStatusOk(ciem.getXmlTree(xmlTreeId).getXmlString());
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
	public Response addXmlTree(@HeaderParam("uid") int uid, @PathParam("treeid") int treeId, TreeXml treeXml) {
		try {
			if (treeXml.getIsFull()) {
				return StatusHelper.getStatusCreated(ciem.addXmlTreeFull(uid, treeId, treeXml.getTitle(), treeXml.getXmlString()));
			} else {
				return StatusHelper.getStatusCreated(ciem.addXmlTreeSimple(uid, treeId, treeXml.getTitle(), treeXml.getXmlString()));
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
			return StatusHelper.getStatusOk(ciem.getXmlTree(xmlTreeId));
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
			return StatusHelper.getStatusOk(ciem.updateXmlTree(treeXml));
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
			ciem.setXmlTree(xmlTreeId, cleantree);
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
			ciem.deleteXmlTree(treeXml.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
