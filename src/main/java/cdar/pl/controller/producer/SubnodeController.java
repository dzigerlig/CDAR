package cdar.pl.controller.producer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.ChangesWrapper;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.User;
import cdar.bll.entity.WikiEntry;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.NodeRepository;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/nodes/{nodeid}/subnodes")
public class SubnodeController {
	private final boolean ISPRODUCER = true;
	private LockingManager lm = new LockingManager();
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager nlm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodes(@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnodesFromNode(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(sm.addSubnode(uid, treeId,
					subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId,
			@PathParam("subnodeid") int subnodeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			subnode.setId(subnodeId);
			return StatusHelper.getStatusOk(sm.updateSubnode(subnode));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{subnodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnode(@PathParam("subnodeid") int subnodeId) {
		try {
			return StatusHelper.getStatusOk(sm.getSubnode(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{subnodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			sm.renameSubnode(subnode);
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(nlm
					.getNodeLinksBySubnode(subnode.getId()), "update"));
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
	public Response deleteSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, Subnode subnode) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			List<NodeLink> nodelinks = nlm.getNodeLinksBySubnode(subnode
					.getId());
			sm.deleteSubnode(subnode.getId());
			return StatusHelper.getStatusOk(new ChangesWrapper<NodeLink>(
					nodelinks, "delete"));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER,
					treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpSubnode(@HeaderParam("uid") int uid,
			@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.drillUp(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownSubnode(@HeaderParam("uid") int uid,
			@PathParam("ktreeid") int treeId, @PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(sm.drillDown(uid, treeId, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{subnodeid}/wiki")
	public Response getKnowledgeSubnodeWikiEntry(
			@PathParam("subnodeid") int subnodeId) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm
					.getKnowledgeSubnodeWikiEntry(subnodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{subnodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeSubnodeWikiEntry(
			@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiManager mwm = new MediaWikiManager();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeSubnodeWikiEntry(
					uid, wikiEntry));
		} catch (UnknownUserException | EntityException ex) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}