package cdar.pl.controller.producer;

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
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/directories")
public class DirectoryController {
	private DirectoryManager dm = new DirectoryManager();

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response getDirectories(@PathParam("ktreeid") int treeId) {
			try {
				return StatusHelper.getStatusOk(dm.getDirectories(treeId));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addDirectory(Directory directory) {
			try {
				if (directory.getTitle() == null) {
					return StatusHelper.getStatusCreated(dm.addDirectory(directory.getTreeId(), directory.getParentId(),"new Folder"));
				} else {
					return StatusHelper.getStatusCreated(dm.addDirectory(directory.getTreeId(), directory.getParentId(), directory.getTitle()));
				}
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("delete")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response deleteDirectory(Directory directory) {
			try {
				dm.deleteDirectory(directory.getId());
				return StatusHelper.getStatusOk(null);
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}

		@POST
		@Path("{directoryid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response updateDirectory(@PathParam("directoryid") int directoryId, Directory directory) {
			directory.setId(directoryId);
			try {
				return StatusHelper.getStatusOk(dm.updateDirectory(directory));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
		
		@GET
		@Path("{directoryid}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response getDirectory(@PathParam("directoryid") int directoryId) {
			try {
				return StatusHelper.getStatusOk(dm.getDirectory(directoryId));
			} catch (Exception e) {
				return StatusHelper.getStatusBadRequest();
			}
		}
}
