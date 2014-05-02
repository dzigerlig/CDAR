package cdar.pl.controller.consumer;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Tree;
import cdar.bll.manager.consumer.ProjectDirectoryManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/directories")
public class ProjectDirectoryController {
	private ProjectDirectoryManager pdm = new ProjectDirectoryManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectories(@PathParam ("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pdm.getDirectories(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDirectory(Directory directory) {
		try {
			return StatusHelper.getStatusCreated(directory);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
	
	@GET
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectory(@PathParam ("directoryid") int directoryId) {
		try {
			return StatusHelper.getStatusOk(pdm.getDirectory(directoryId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
	
	@POST
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDirectory(Directory directory) {
		try {
			return StatusHelper.getStatusOk(pdm.updateDirectory(directory));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDirectory(Directory directory) {
		try {
			pdm.deleteDirectory(directory.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
}
