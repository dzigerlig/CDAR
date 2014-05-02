package cdar.pl.controller.consumer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ptrees/{ptreeid}/nodes/{nodeid}/subnodes")
public class ProjectSubnodeController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes() {
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode() {
		return null;
	}
	
	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode() {
		return null;
	}
	
	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnode() {
		return null;
	}
	
	@GET
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodeWikiEntry() {
		return null;
	}
	
	@POST
	@Path("{subnodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnodeWikiEntry() {
		return null;
	}
	
	@GET
	@Path("zoomup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomUpSubnode() {
		return null;
	}
	
	@GET
	@Path("zoomdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response zoomDownSubnode() {
		return null;
	}
	
	@POST
	@Path("rename")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renameSubnode() {
		return null;
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSubnode() {
		return null;
	}
}
