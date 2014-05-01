package cdar.pl.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.entity.ChangesWrapper;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.SubnodeManager;

@Path("ktree/{ktreeid}/subnodes")
public class KnowledgeSubnodeController {
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager lm = new NodeLinkManager();

	// Subnodes
	@GET
	// Changed
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubnodesByTree(@PathParam("ktreeid") int ktreeid) {
		try {
			return Response.ok(sm.getSubnodesFromTree(ktreeid),
					MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
