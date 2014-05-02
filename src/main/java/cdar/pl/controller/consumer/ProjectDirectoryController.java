package cdar.pl.controller.consumer;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Tree;

@Path("ptrees/{ptreeid}/directories")
public class ProjectDirectoryController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectories() {
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDirectory() {
		return null;
	}
	
	@GET
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDirectory() {
		return null;
	}
	
	@POST
	@Path("{directoryid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDirectory() {
		return null;
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDirectory() {
		
	}
}
