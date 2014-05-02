package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ptrees/{ptreeid}/links")
public class ProjectNodeLinkController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeLink() {
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink() {
		return null;
	}
	
	@GET
	@Path("{linkid}/nodes/{nodeid}/zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpNodeLink() {
		return null;
	}
	
	@GET
	@Path("{linkid}/nodes/{nodeid}/zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownNodeLink() {
		return null;
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink() {
		return null;
	}
}
