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

	// Directory
		@GET
		// Changed
		@Produces(MediaType.APPLICATION_JSON)
		public Response getDirectories(@PathParam("ktreeid") int ktreeid) {
			try {
				return Response.ok(dm.getDirectories(ktreeid),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		// Changed
		@Path("add")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addDirectory(Directory d) {
			try {
				if (d.getTitle() == null) {
					return Response.status(Response.Status.CREATED).entity(
							dm.addDirectory(d.getTreeId(), d.getParentId(),
									"new Folder"))
							.build();
				} else {
					return Response.ok(
							dm.addDirectory(d.getTreeId(), d.getParentId(),
									d.getTitle()), MediaType.APPLICATION_JSON)
							.build();
				}
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		}

		@POST
		// Changed
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteDirectory(int id) {
			try {
				return Response.ok(dm.deleteDirectory(id),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}

		@POST
		// Changed
		@Path("rename")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response renameDirectory(Directory d) {
			try {
				return Response.ok(dm.renameDirectory(d),
						MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		}

		@POST
		// Changed
		@Path("move")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response moveDirectory(Directory d) {
			try {
				return Response.ok(dm.moveDirectory(d), MediaType.APPLICATION_JSON)
						.build();
			} catch (Exception e) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		}
}
