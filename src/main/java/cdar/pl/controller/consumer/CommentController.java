package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.consumer.Comment;
import cdar.bll.manager.consumer.CommentManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/nodes/{nodeid}/comments")
public class CommentController {
	private CommentManager cm = new CommentManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(@PathParam ("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(cm.getComments(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment(Comment comment) {
		try {
			return StatusHelper.getStatusCreated(cm.addComment(comment));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComment(@PathParam ("commentid") int commentId) {
		try {
			return StatusHelper.getStatusOk(cm.getComment(commentId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateComment(Comment comment) {
		try {
			return StatusHelper.getStatusOk(cm.updateComment(comment));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteComment(Comment comment) {
		try {
			cm.deleteComment(comment.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
