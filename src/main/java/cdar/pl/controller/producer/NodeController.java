package cdar.pl.controller.producer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.Node;
import cdar.bll.entity.WikiEntry;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/nodes")
public class NodeController {
	private final boolean ISPRODUCER = true;
	private NodeManager nm = new NodeManager();
	private LockingManager lm = new LockingManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("ktreeid") int ktreeid) {
		try {
			return StatusHelper.getStatusOk(nm.getNodes(ktreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("ktreeid") int treeId,
			@HeaderParam("uid") int uid, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(nm.addNode(uid, node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.getNode(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{nodeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId,
			Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			node.setId(nodeId);
			return StatusHelper.getStatusOk(nm.updateNode(node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{nodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusOk(nm.renameNode(node));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Node node) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			nm.deleteNode(node.getId());
			return StatusHelper.getStatusOk(null);
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillUp(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillDown(uid, treeId, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.getKnowledgeNodeWikiEntry(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{nodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeNodeWikiEntry(@HeaderParam("uid") int uid,
			WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeNodeWikiEntry(uid,
					wikiEntry));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
