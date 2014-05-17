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

import cdar.bll.entity.Directory;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.consumer.ProjectDirectoryManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/directories")
public class ProjectDirectoryController {
	private final boolean ISPRODUCER = false;
	private LockingManager lm = new LockingManager();
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
	public Response addDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Directory directory) {
		try {			lm.lock(ISPRODUCER, treeId, uid);

			return StatusHelper.getStatusCreated(pdm.addDirectory(directory));
		}catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
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
	public Response updateDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, @PathParam ("directoryid") int directoryId,Directory directory) {
		try {			lm.lock(ISPRODUCER, treeId, uid);

			directory.setId(directoryId);
			return StatusHelper.getStatusOk(pdm.updateDirectory(directory));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		}catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Directory directory) {
		try {			lm.lock(ISPRODUCER, treeId, uid);

			pdm.deleteDirectory(directory.getId());
			return StatusHelper.getStatusOk(null);
		}catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		} 
	}
}
