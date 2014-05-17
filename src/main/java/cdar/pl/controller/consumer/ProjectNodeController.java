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

import cdar.PropertyHelper;
import cdar.bll.entity.WikiEntry;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.pl.controller.StatusHelper;

@Path("ptrees/{ptreeid}/nodes")
public class ProjectNodeController {
	private final boolean ISPRODUCER = false;
	private LockingManager lm = new LockingManager();
	private ProjectNodeManager pnm = new ProjectNodeManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ptreeid") int treeId) {
		try {
			return StatusHelper.getStatusOk(pnm.getProjectNodes(treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);

			if (projectNode.getTitle() == null) {
				PropertyHelper propertyHelper = new PropertyHelper();
				projectNode.setTitle("new "
						+ propertyHelper.getProperty("NODE_DESCRIPTION"));
			}
			return StatusHelper.getStatusCreated(pnm
					.addProjectNode(projectNode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.getProjectNode(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, @PathParam("nodeid") int nodeId,
			ProjectNode node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);

			node.setId(nodeId);
			return StatusHelper.getStatusOk(pnm.updateProjectNode(uid, node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper
					.getStatusOk(mwm.getProjectNodeWikiEntry(nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	// TODO locking??
	@POST
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNodeWikiEntry(@HeaderParam("uid") int uid,
			WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.saveProjectNodeWikiEntry(uid,
					wikiEntry));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {

			return StatusHelper.getStatusOk(pnm.drillUp(uid, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(pnm.drillDown(uid, nodeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{nodeid}/rename")
	@Produces(MediaType.APPLICATION_JSON)
	public Response renameNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);

			return StatusHelper.getStatusOk(pnm.renameNode(projectNode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNode(@PathParam("ptreeid") int treeId,
			@HeaderParam("uid") int uid, ProjectNode projectNode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);

			pnm.deleteProjectNode(projectNode.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
