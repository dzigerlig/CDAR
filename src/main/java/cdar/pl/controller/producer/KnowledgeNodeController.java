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

import org.glassfish.jersey.internal.util.PropertiesHelper;

import cdar.bll.entity.Node;
import cdar.bll.entity.WikiEntry;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.dal.PropertyHelper;
import cdar.pl.controller.StatusHelper;

@Path("ktrees/{ktreeid}/nodes")
public class KnowledgeNodeController {
	private NodeManager nm = new NodeManager();

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
	public Response addNode(@HeaderParam("uid") int uid, Node node) {
		try {
			if (node.getTitle() == null) {
				PropertyHelper propertyHelper = new PropertyHelper();
				node.setTitle("new "+ propertyHelper.getProperty("NODE_DESCRIPTION"));
			}
			return StatusHelper.getStatusCreated(nm.addNode(uid, node));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("nodeid") int nodeid) {
		try {
			return StatusHelper.getStatusOk(nm.getNode(nodeid));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(@PathParam("nodeid") int nodeid, Node node) {
		try {
			node.setId(nodeid);
			return StatusHelper.getStatusOk(nm.updateNode(node));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameNode (Node node) {
		try {
			return StatusHelper.getStatusOk(nm.renameNode(node));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@POST
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(Node node) {
		try {
			nm.deleteNode(node.getId());
			return StatusHelper.getStatusOk(null);
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drillup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillUpNode(@HeaderParam("uid") int uid,@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillUp(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}

	@GET
	@Path("{nodeid}/drilldown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response drillDownNode(@HeaderParam("uid") int uid,@PathParam("nodeid") int nodeId) {
		try {
			return StatusHelper.getStatusOk(nm.drillDown(uid, nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@GET
	@Path("{nodeid}/wiki")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKnowledgeNodeWikiEntry(@PathParam("nodeid") int nodeId) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.getKnowledgeNodeWikiEntry(nodeId));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
	
	@POST
	@Path("{nodeid}/wiki")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateKnowledgeNodeWikiEntry(@HeaderParam("uid") int uid, WikiEntry wikiEntry) {
		try {
			MediaWikiModel mwm = new MediaWikiModel();
			return StatusHelper.getStatusOk(mwm.saveKnowledgeNodeWikiEntry(uid, wikiEntry));
		} catch (Exception e) {
			return StatusHelper.getStatusBadRequest();
		}
	}
}
