package cdar.pl.controller;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.producer.models.TemplateModel;
import cdar.bll.producer.models.TreeModel;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

@Path("{uid}/ktree")
public class TreeController {
	private TreeModel ktm = new TreeModel();
	private NodeModel nm = new NodeModel();
	private NodeLinkModel lm = new NodeLinkModel();
	private DirectoryModel dm = new DirectoryModel();
	private SubnodeModel sm = new SubnodeModel();
	private TemplateModel tm = new TemplateModel();
	
	//Dynamic Tree
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Tree> getKnowledgeTreesByUid(@PathParam("uid") int uid) {
		return ktm.getTrees(uid);
	}
	
	@GET
	@Path("delete/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteTreeById(@PathParam("uid") int uid, @PathParam("ktreeid") int ktreeid) {
		return ktm.deleteTree(ktreeid);
	}
	
	@POST
	@Path("tree/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Tree addKnowledgeTree(String treeName, @PathParam("uid") int uid) {
		return ktm.addTree(uid, treeName);
	}
	
	@GET
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tree getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		return ktm.getTree(ktreeid);
	}
	
	//TEMPLATES
	@GET
	@Path("templates/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Template> getTemplates(@PathParam("ktreeid") int ktreeid) {
		return tm.getKnowledgeTemplates(ktreeid);
	}
	
	@GET
	@Path("templates/{ktreeid}/{templateid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Template getTemplate(@PathParam("ktreeid") int ktreeid, @PathParam("templateid") int templateid) {
		return tm.getKnowledgeTemplate(templateid);
	}
	
	@POST
	@Path("templates/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template addTemplate(Template template) {
		return tm.addKnowledgeTemplate(template.getTreeid(), template.getTitle(), template.getTemplatetext());
	}
	
	@POST
	@Path("templates/edit/{templateid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template editTemplate(Template template) {
		ProducerDaoController pdc = new ProducerDaoController();
		TemplateDao templatedao = pdc.getTemplate(template.getId());
		templatedao.setTemplatetext(template.getTemplatetext());
		templatedao.setTitle(template.getTitle());
		return new Template(templatedao.update());
	}
	
	@POST
	@Path("templates/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean deleteTemplate(int id) {
		return new Boolean(tm.deleteTemplate(id));
	}
	
	//Directory
	@GET
	@Path("/directories/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Directory> getDirectory(@PathParam("ktreeid") int ktreeid) {
		return dm.getDirectories(ktreeid);
	}
	
	@POST
	@Path("directories/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory addDirectory(Directory d) {
		return dm.addDirectory(d.getKtrid(), d.getParentid(), null);
	}
	
	@POST
	@Path("directories/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeDirectory(int id) {
		dm.removeDirectoryById(id);
		return Response.status(200).build();
	}
	
	@POST
	@Path("directories/rename/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameDirectory(Directory d) {
		dm.renameDirectory(d);
		return Response.status(200).build();
	}
	
	@POST
	@Path("directories/move/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveDirectory(Directory d) {
		dm.moveDirectory(d);
		return Response.status(200).build();
	}	
	
	//Nodes
	@GET
	@Path("/nodes/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> getNodes(@PathParam("ktreeid") int ktreeid) {
		return nm.getNodes(ktreeid);
	}
	
	@POST
	@Path("nodes/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node addNode(Node n) {
		return nm.addNode(n.getRefTreeId(), null, n.getDid());
	}
	
	@POST
	@Path("nodes/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNode(int id) {
		nm.deleteNode(id);
		return Response.status(200).build();
	}

	@POST
	@Path("nodes/drop/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node dropNode(int id) {
		return nm.dropNode(id);
	}
	
	@POST
	@Path("nodes/rename/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response renameNode(Node n) {
		nm.renameNode(n);
		return Response.status(200).build();
	}
	
	@POST
	@Path("nodes/undrop/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response undropNode(int id) {
		nm.undropNode(id);
		return Response.status(200).build();
	}
	
	@POST
	@Path("nodes/move/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveNode(Node n) {
		nm.moveNode(n);
		return Response.status(200).build();
	}
	
	//Links
	@GET
	@Path("links/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> getLinks(@PathParam("ktreeid") int ktreeid) {
		return lm.getNodeLinks(ktreeid);
	}		
	
	@POST
	@Path("links/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addLink(NodeLink nl) {		 
		return lm.addNodeLink(nl.getKtrid(), nl.getSourceId(), nl.getTargetId(), nl.getKsnid());
	}

	@POST
	@Path("links/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeLink(int id) {
		lm.removeNodeLink(id);
		return Response.status(200).build();
	}
	
	@POST
	@Path("links/update/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateLink(NodeLink nl) {
		lm.updateLink(nl);
		return Response.status(200).build();
	}
	
	//Subnodes
	@GET
	@Path("subnodes/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> getSubnodesByTree(@PathParam("ktreeid") int ktreeid) {
		return sm.getSubnodesFromTree(ktreeid);
	}

	@GET
	@Path("subnodes/{ktreeid}/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> getSubnodes(@PathParam("nodeid") int nodeid) {
		return sm.getSubnodesFromNode(nodeid);
	}
	
	@POST
	@Path("subnodes/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Subnode addSubnode(Subnode sN) {		 
		return sm.addSubnode(sN.getKnid(), sN.getTitle());
	}

	@POST
	@Path("subnodes/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeSubnode(int id) {
		sm.removeSubnode(id);
		return Response.status(200).build();
	}
}
