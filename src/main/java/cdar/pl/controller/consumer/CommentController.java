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
import cdar.pl.helpers.StatusHelper;

@Path("ptrees/{ptreeid}/nodes/{nodeid}/comments")
public class CommentController {
	private final boolean ISPRODUCER = false;
	private LockingManager lm = new LockingManager();
	private CommentManager cm = new CommentManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(cm.getComments(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

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
