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

import cdar.bll.entity.Directory;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/directories")
public class KnowledgeDirectoryController {
	private final boolean ISPRODUCER = true;
	private LockingManager lm = new LockingManager();
	private DirectoryManager dm = new DirectoryManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectories(@PathParam("ktreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(dm.getDirectories(treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDirectory(@HeaderParam("uid") int uid,@PathParam("ktreeid") int treeId, Directory directory) {
		try{
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(dm.addDirectory(directory));
		}catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} 
		catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteDirectory(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, Directory directory) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			dm.deleteDirectory(directory.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{directoryid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDirectory(@HeaderParam("uid") int uid, @PathParam("ktreeid") int treeId, @PathParam("directoryid") int directoryId,
			Directory directory) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			directory.setId(directoryId);
			return StatusHelper.getStatusOk(dm.updateDirectory(directory));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{directoryid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDirectory(@PathParam("directoryid") int directoryId) {
		try {
			return StatusHelper.getStatusOk(dm.getDirectory(directoryId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
