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
import cdar.bll.entity.NodeLink;
import cdar.bll.exceptions.LockingException;
import cdar.bll.manager.LockingManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.dal.producer.NodeRepository;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/links")
public class KnowledgeNodeLinkController {
	private final boolean ISPRODUCER = true;
	private LockingManager lm = new LockingManager();
	private NodeLinkManager nlm = new NodeLinkManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLinks(@PathParam("ktreeid") int ktreeid) {
		try {
			return StatusHelper.getStatusOk(nlm.getNodeLinks(ktreeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNodeLink(@HeaderParam("uid") int uid,@PathParam("ktreeid") int treeId,NodeLink nodeLink) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			return StatusHelper.getStatusCreated(nlm.addNodeLink(nodeLink));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNodeLink(@HeaderParam("uid") int uid,@PathParam("ktreeid") int treeId,NodeLink nodeLink) {
		try {
			lm.lock(ISPRODUCER, treeId, uid);
			nlm.deleteNodeLink(nodeLink.getId());
			return StatusHelper.getStatusOk(null);
		}catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		}  catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("{linkid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNodeLink(@HeaderParam("uid") int uid,@PathParam("ktreeid") int treeId,NodeLink nodeLink) {
		try {			lm.lock(ISPRODUCER, treeId, uid);

			return StatusHelper.getStatusOk(nlm.updateNodeLink(nodeLink));
		} catch (LockingException e) {
			return StatusHelper.getStatusConflict(lm.getLockText(ISPRODUCER, treeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodes/{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpLink(@HeaderParam("uid") int uid,@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nlm.drillUp(uid,nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	// Changed
	@Path("nodes/{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownLink(@HeaderParam("uid") int uid,@PathParam("ktreeid") int ktreeid,@PathParam("nodeid") int nodeId) {
		try {
			if (nodeId == 0) {
				Node rootNode = new NodeRepository().getRoot(ktreeid);
				if(rootNode==null)
				{
					return StatusHelper.getStatusOk(null);
				}
				nodeId = rootNode.getId();
			}
			return StatusHelper.getStatusOk(nlm.drillDown(uid,nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
