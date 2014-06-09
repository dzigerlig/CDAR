package ch.cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.exception.LockingException;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.LockingManager;
import ch.cdar.pl.StatusHelper;

/**
 * The Class DirectoryController.
 */
@Path("ktrees/{ktreeid}/directories")
public class DirectoryController {
	/** The isproducer. */
	private final boolean ISPRODUCER = true;
	
	/** The locking manager. */
	private LockingManager lm = new LockingManager();
	
	/** The directory manager. */
	private DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);

	/**
	 * Gets the directories.
	 *
	 * @param treeId the tree id
	 * @return the directories
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectories(@PathParam("ktreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(dm.getDirectories(treeId));
		} catch (Exception e) {
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

	/**
	 * Gets the directory.
	 *
	 * @param directoryId the directory id
	 * @return the directory
	 */
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
