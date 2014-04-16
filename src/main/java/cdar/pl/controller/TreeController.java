package cdar.pl.controller;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cdar.bll.CDAR_Boolean;
import cdar.bll.CDAR_BooleanChanges;
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

@Path("{uid}/ktree")
public class TreeController {
	private TreeModel ktm = new TreeModel();
	private NodeModel nm = new NodeModel();
	private NodeLinkModel lm = new NodeLinkModel();
	private DirectoryModel dm = new DirectoryModel();
	private SubnodeModel sm = new SubnodeModel();
	private TemplateModel tm = new TemplateModel();

	// Dynamic Tree 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Tree> getKnowledgeTreesByUid(@PathParam("uid") int uid) {
		return ktm.getTrees(uid);
	}

	@POST
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteTreeById(int ktreeid) {
		return new CDAR_Boolean(ktm.deleteTree(ktreeid));
	}

	@POST
	@Path("tree/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Tree addKnowledgeTree(String treeTitle, @PathParam("uid") int uid) {
		return ktm.addTree(uid, treeTitle);
	}
	
	@POST
	@Path("tree/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Tree renameKnowledgeTree(Tree tree) {
		return ktm.updateTree(tree);
	}
	
	@GET
	@Path("{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tree getKnowledgeTreeById(@PathParam("ktreeid") int ktreeid) {
		return ktm.getTree(ktreeid);
	}

	// TEMPLATES
	@GET
	@Path("templates/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Template> getTemplates(@PathParam("ktreeid") int ktreeid) {
		return tm.getKnowledgeTemplates(ktreeid);
	}

	@GET
	@Path("templates/{ktreeid}/{templateid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Template getTemplate(@PathParam("ktreeid") int ktreeid,
			@PathParam("templateid") int templateid) {
		return tm.getKnowledgeTemplate(templateid);
	}

	@POST
	@Path("templates/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template addTemplate(Template template) {
		return tm.addKnowledgeTemplate(template.getTreeid(),
				template.getTitle(), template.getTemplatetext());
	}
	
	@POST
	@Path("templates/rename/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template renameTemplate(Template template) {
		return tm.renameTemplate(template);
	}
	
	@POST
	@Path("templates/default/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Set<Template> setDefaultTemplate(@PathParam("ktreeid") int ktreeid, int templateId) {
		return tm.setDefaultTemplate(ktreeid, templateId);
	}
 
	@POST
	@Path("templates/edit/{templateid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template editTemplate(Template template) {
		return tm.updateTemplate(template);
	}

	@POST
	@Path("templates/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteTemplate(int id) {
		return new CDAR_Boolean(tm.deleteTemplate(id));
	}

	// Directory
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
		return dm.addDirectory(d.getKtrid(), d.getParentid(), "new Folder");
	}

	@POST
	@Path("directories/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteDirectory(int id) {
		return new CDAR_Boolean(dm.deleteDirectory(id));
	}

	@POST
	@Path("directories/rename/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory renameDirectory(Directory d) {
		return dm.renameDirectory(d);
	}

	@POST
	@Path("directories/move/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory moveDirectory(Directory d) {
		return dm.moveDirectory(d);
	}

	// Nodes
	@GET
	@Path("/nodes/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> getNodes(@PathParam("ktreeid") int ktreeid) {
		return nm.getNodes(ktreeid);
	}
	
	@GET
	@Path("/nodes/{ktreeid}/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getNode(@PathParam("nodeid") int nodeid) {
		return nm.getNode(nodeid);
	}

	@POST
	@Path("nodes/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node addNode(Node n) {
		return nm.addNode(n.getKtrid(), "new Node", n.getDid());
	}

	@POST
	@Path("nodes/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteNode(int id) {
		return new CDAR_Boolean(nm.deleteNode(id));
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
	public Node renameNode(Node n) {
		return nm.renameNode(n);
	}

	@POST
	@Path("nodes/undrop/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node undropNode(int id) {
		return nm.undropNode(id);
	}

	@POST
	@Path("nodes/move/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node moveNode(Node n) {
		return nm.moveNode(n);
	}

	// Links
	@GET
	@Path("links/{ktreeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> getLinks(@PathParam("ktreeid") int ktreeid) {
		return lm.getNodeLinks(ktreeid);
	}

	@POST
	@Path("links/add/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addNodeLink(NodeLink nl) {
		return lm.addNodeLink(nl.getKtrid(), nl.getSourceId(),
				nl.getTargetId(), nl.getKsnid());
	}

	@POST
	@Path("links/delete/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteNodeLink(int id) {
		return new CDAR_Boolean(lm.deleteNodeLink(id));
	}

	@POST
	@Path("links/update/{ktreeid}")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink updateNodeLink(NodeLink nl) {
		return lm.updateNodeLink(nl);
	}

	// Subnodes
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
	@Path("subnodes/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Subnode renameSubnode(Subnode subnode) {
		return sm.renameSubnode(subnode);
	}
	
	@POST
	@Path("subnodes/moveup")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean moveSubnodeUp(Subnode subnode) {
		return new CDAR_Boolean(sm.moveSubnodeUp(subnode));
	}
	
	@POST
	@Path("subnodes/movedown")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean moveSubnodeDown(Subnode subnode) {
		return new CDAR_Boolean(sm.moveSubnodeDown(subnode));
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
	public CDAR_BooleanChanges<NodeLink> deleteSubnode(int id) {
		List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(id);
		return new CDAR_BooleanChanges<NodeLink>(sm.deleteSubnode(id), nodelinks);
	}
}
