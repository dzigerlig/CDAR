package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ptrees/{ptreeid}/comments")
public class CommentController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComment() {
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addComment() {
		return null;
	}
	
	@GET
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComment() {
		return null;
	}
	
	@POST
	@Path("{commentid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateComment() {
		return null;
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteComment() {
		
	}
}
