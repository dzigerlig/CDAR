/*
 * 
 */
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

import cdar.bll.entity.consumer.Comment;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.consumer.CommentManager;
import cdar.pl.controller.StatusHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class CommentController.
 */
@Path("ptrees/{ptreeid}/nodes/{nodeid}/comments")
public class CommentController {
	
	/** The isproducer. */
	private final boolean ISPRODUCER = false;
	
	/** The lm. */
	private LockingManager lm = new LockingManager();
	
	/** The cm. */
	private CommentManager cm = new CommentManager();

	/**
	 * Gets the comments.
	 *
	 * @param nodeId the node id
	 * @return the comments
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(cm.getComments(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Adds the comment.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @param comment the comment
	 * @return the response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, @PathParam("nodeid") int nodeId,
			Comment comment) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			comment.setUserId(uid);
			comment.setNodeId(nodeId);
			return StatusHelper.getStatusCreated(cm.addComment(comment));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			ex.printStackTrace();
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Gets the comment.
	 *
	 * @param commentId the comment id
	 * @return the comment
	 */
	@GET
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComment(@PathParam("commentid") int commentId) {
		try {
			return StatusHelper.getStatusOk(cm.getComment(commentId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Update comment.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param commentId the comment id
	 * @param comment the comment
	 * @return the response
	 */
	@POST
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateComment(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId,
			@PathParam("commentid") int commentId, Comment comment) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			comment.setId(commentId);
			return StatusHelper.getStatusOk(cm.updateComment(comment));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	/**
	 * Delete comment.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param comment the comment
	 * @return the response
	 */
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteComment(@HeaderParam("uid") int uid,
			@PathParam("ptreeid") int treeId, Comment comment) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			cm.deleteComment(comment.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
