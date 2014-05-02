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

import cdar.bll.entity.Tree;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.manager.consumer.ProjectTreeManager;
import cdar.pl.controller.StatusHelper;

@Path("ptrees")
public class ProjectTreeController {
	private ProjectTreeManager ptm = new ProjectTreeManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreesByUid(@HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getProjectTrees(uid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectTree(String treeTitle, @HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusCreated(ptm.addProjectTree(uid, treeTitle));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectTreeById(@PathParam("ptreeid") int ptreeid, @HeaderParam("uid") int uid) {
		try {
			return StatusHelper.getStatusOk(ptm.getProjectTree(ptreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{ptreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateProjectTree(Tree tree) {
		try {
			return StatusHelper.getStatusOk(ptm.updateProjectTree(tree));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTreeById(int ptreeid) {
		try {
			return StatusHelper.getStatusOk(ptm.deleteProjectTree(ptreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{ptreeid}/ktrees/{ktreeid}/copy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKnowledgeTreeToProjectTree(@PathParam("ktreeid") int ktreeid, @PathParam("ptreeid") int ptreeid) {
		try {
			ptm.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{ptreeid}/subnodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("ptreeid") int treeId) {
		try {
			ProjectSubnodeManager psm = new ProjectSubnodeManager();
			return StatusHelper.getStatusOk(psm.getProjectSubnodesFromProjectTree(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
