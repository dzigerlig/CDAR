package cdar.pl.controller.consumer;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ptrees/{ptreeid}/nodes")
public class ProjectNodeController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes() {
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode() {
		return null;
	}
	
	@GET
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode() {
		return null;
	}
	
	@POST
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNode() {
		return null;
	}
	
	@GET
	@Path("{directoryid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeWikiEntry() {
		return null;
	}
	
	@POST
	@Path("{directoryid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNodeWikiEntry() {
		return null;
	}
	
	@GET
	@Path("zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpNode() {
		return null;
	}
	
	@GET
	@Path("zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownNode() {
		return null;
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNode() {
		return null;
	}
}
