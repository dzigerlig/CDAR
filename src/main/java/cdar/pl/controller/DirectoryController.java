package cdar.pl.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Directory;
import cdar.bll.manager.producer.DirectoryManager;

@Path("ktree/{ktreeid}/directories")
public class DirectoryController {
	private DirectoryManager dm = new DirectoryManager();

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getDirectories(@PathParam("ktreeid") int treeId) {
			try {
				return Response.ok(dm.getDirectories(treeId),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addDirectory(Directory directory) {
			try {
				if (directory.getTitle() == null) {
					return Response.status(Response.Status.CREATED).entity(
							dm.addDirectory(directory.getTreeId(), directory.getParentId(),
									"new Folder"))
							.build();
				} else {
					return Response.ok(
							dm.addDirectory(directory.getTreeId(), directory.getParentId(),
									directory.getTitle()), MediaType.APPLICATION_JSON)
							.build();
				}
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteDirectory(Directory directory) {
			try {
				return Response.ok(dm.deleteDirectory(directory.getId()),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		@Path("{directoryid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateDirectory(Directory directory) {
			try {
				return Response.ok(dm.updateDirectory(directory),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		
		@GET
		@Path("{directoryid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response getDirectory(@PathParam("directoryid") int directoryId) {
			try {
				return Response.ok(dm.getDirectory(directoryId), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
}
