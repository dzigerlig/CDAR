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
import cdar.bll.entity.UserRole;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.DirectoryManager;
import cdar.bll.manager.LockingManager;
import cdar.pl.controller.StatusHelper;

/**
 * The Class ProjectDirectoryController.
 */
@Path("ptrees/{ptreeid}/directories")
public class ProjectDirectoryController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The pdm. */
	DirectoryManager pdm = new DirectoryManager(UserRole.CONSUMER);

	/**
	 * Gets the directories.
	 *
	 * @param treeId the tree id
	 * @return the directories
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectories(@PathParam("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pdm.getDirectories(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the directory.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param directory the directory
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Directory directory) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(pdm.addDirectory(directory));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the directory.
	 *
	 * @param directoryId the directory id
	 * @return the directory
	 */
	@GET
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectory(@PathParam("directoryid") int directoryId) {
		try {
			return StatusHelper.getStatusOk(pdm.getDirectory(directoryId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update directory.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param directoryId the directory id
	 * @param directory the directory
	 * @return the response
	 */
	@POST
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId,
			@PathParam("directoryid") int directoryId, Directory directory) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			directory.setId(directoryId);
			return StatusHelper.getStatusOk(pdm.updateDirectory(directory));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete directory.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param directory the directory
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDirectory(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Directory directory) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			pdm.deleteDirectory(directory.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
