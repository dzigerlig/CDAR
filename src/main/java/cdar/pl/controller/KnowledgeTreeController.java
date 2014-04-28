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
import cdar.bll.producer.XmlTree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.producer.models.TemplateModel;
import cdar.bll.producer.models.TreeModel;
import cdar.bll.producer.models.XmlTreeModel;

@Path("ktree")
public class KnowledgeTreeController {
	private TreeModel ktm = new TreeModel();
	private NodeModel nm = new NodeModel();
	private NodeLinkModel lm = new NodeLinkModel();
	private DirectoryModel dm = new DirectoryModel();
	private SubnodeModel sm = new SubnodeModel();
	private TemplateModel tm = new TemplateModel();
	private XmlTreeModel xtm = new XmlTreeModel();

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
	@GET//Changed
	@Path("{ktreeid}/templates")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Template> getTemplates(@PathParam("ktreeid") int ktreeid) {
		return tm.getKnowledgeTemplates(ktreeid);
	}

	@GET//Changed
	@Path("{ktreeid}/templates/{templateid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Template getTemplate(@PathParam("ktreeid") int ktreeid,
			@PathParam("templateid") int templateid) {
		return tm.getKnowledgeTemplate(templateid);
	}

	@POST//Changed
	@Path("{ktreeid}/templates/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template addTemplate(Template template) {
		return tm.addKnowledgeTemplate(template.getTreeId(),
				template.getTitle(), template.getTemplatetext(), template.getDecisionMade());
	}

	@POST//Changed
	@Path("{ktreeid}/templates/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template renameTemplate(Template template) {
		return tm.renameTemplate(template);
	}

	@POST
	@Path("{ktreeid}/templates/default")
	@Consumes(MediaType.APPLICATION_JSON)
	public Set<Template> setDefaultTemplate(@PathParam("ktreeid") int ktreeid,
			int templateId) {
		return tm.setDefaultTemplate(ktreeid, templateId);
	}

	@POST//Changed
	@Path("{ktreeid}/templates/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	public Template editTemplate(Template template) {
		return tm.updateTemplate(template);
	}

	@POST//Changed
	@Path("{ktreeid}/templates/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteTemplate(int id) {
		return new CDAR_Boolean(tm.deleteTemplate(id));
	}

	// Directory
	@GET//Changed
	@Path("{ktreeid}/directories")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Directory> getDirectories(@PathParam("ktreeid") int ktreeid) {
		return dm.getDirectories(ktreeid);
	}

	@POST//Changed
	@Path("{ktreeid}/directories/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory addDirectory(Directory d) {
		if (d.getTitle()==null) {
			return dm.addDirectory(d.getKtrid(), d.getParentid(), "new Folder");
		} else {
			return dm.addDirectory(d.getKtrid(), d.getParentid(), d.getTitle());
		}
	}

	@POST//Changed
	@Path("{ktreeid}/directories/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteDirectory(int id) {
		return new CDAR_Boolean(dm.deleteDirectory(id));
	}

	@POST//Changed
	@Path("{ktreeid}/directories/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory renameDirectory(Directory d) {
		return dm.renameDirectory(d);
	}

	@POST//Changed
	@Path("{ktreeid}/directories/move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Directory moveDirectory(Directory d) {
		return dm.moveDirectory(d);
	}

	// Nodes
	@GET//Changed
	@Path("{ktreeid}/nodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> getNodes(@PathParam("ktreeid") int ktreeid) {
		return nm.getNodes(ktreeid);
	}

	@GET//Changed
	@Path("{ktreeid}/nodes/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getNode(@PathParam("nodeid") int nodeid) {
		return nm.getNode(nodeid);
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node addNode(@PathParam("uid") int uid, Node n) {
		if (n.getTitle()==null) {
			return nm.addNode(uid, n.getKtrid(), "new Node", n.getDid());
		} else {
			return nm.addNode(uid, n.getKtrid(), n.getTitle(), n.getDid());
		}
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteNode(int id) {
		return new CDAR_Boolean(nm.deleteNode(id));
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/drop")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node dropNode(int id) {
		return nm.dropNode(id);
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node renameNode(Node n) {
		return nm.renameNode(n);
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/undrop")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node undropNode(int id) {
		return nm.undropNode(id);
	}

	@POST//Changed
	@Path("{ktreeid}/nodes/move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Node moveNode(Node n) {
		return nm.moveNode(n);
	}
	
	@GET//Changed
	@Path("{ktreeid}/nodes/{nodeid}/zoomUp")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> zoomUpNode(@PathParam("nodeid") int nodeid) {
		return nm.zoomUp(nodeid);
	}
	@GET//Changed
	@Path("{ktreeid}/nodes/{nodeid}/zoomDown")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Node> zoomDownNode(@PathParam("nodeid") int nodeid) {
		return nm.zoomDown(nodeid);
	}


	// Links
	@GET//Changed
	@Path("{ktreeid}/links")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> getLinks(@PathParam("ktreeid") int ktreeid) {
		return lm.getNodeLinks(ktreeid);
	}

	@POST//Changed
	@Path("{ktreeid}/links/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink addNodeLink(NodeLink nl) {
		return lm.addNodeLink(nl.getKtrid(), nl.getSourceId(),
				nl.getTargetId(), nl.getKsnid());
	}

	@POST//Changed
	@Path("{ktreeid}/links/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteNodeLink(int id) {
		return new CDAR_Boolean(lm.deleteNodeLink(id));
	}

	@POST//Changed
	@Path("{ktreeid}/links/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public NodeLink updateNodeLink(NodeLink nl) {
		return lm.updateNodeLink(nl);
	}
	
	@GET//Changed
	@Path("{ktreeid}/links/{nodeid}/zoomUp")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> zoomUpLink(@PathParam("nodeid") int nodeid) {
		return lm.zoomUp(nodeid);
	}
	
	@GET//Changed
	@Path("{ktreeid}/links/{nodeid}/zoomDown")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<NodeLink> zoomDownLink(@PathParam("nodeid") int nodeid) {
		return lm.zoomDown(nodeid);
	}

	// Subnodes
	@GET//Changed
	@Path("{ktreeid}/subnodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> getSubnodesByTree(@PathParam("ktreeid") int ktreeid) {
		return sm.getSubnodesFromTree(ktreeid);
	}

	@GET//Changed
	@Path("{ktreeid}/subnodes/{nodeid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> getSubnodes(@PathParam("nodeid") int nodeid) {
		return sm.getSubnodesFromNode(nodeid);
	}

	@POST//Changed
	@Path("{ktreeid}/subnodes/rename")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_BooleanChanges<NodeLink> renameSubnode(Subnode subnode) {
		return new CDAR_BooleanChanges<NodeLink>(sm.renameSubnode(subnode), lm.getNodeLinksBySubnode(subnode.getId()), "update");
	}

	@POST//Changed
	@Path("{ktreeid}/subnodes/moveup")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean moveSubnodeUp(Subnode subnode) {
		return new CDAR_Boolean(sm.moveSubnodeUp(subnode));
	}

	@POST//Changed
	@Path("{ktreeid}/subnodes/movedown")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean moveSubnodeDown(Subnode subnode) {
		return new CDAR_Boolean(sm.moveSubnodeDown(subnode));
	}

	@POST//Changed
	@Path("{ktreeid}/subnodes/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Subnode addSubnode(Subnode sN) {
		return sm.addSubnode(sN.getKnid(), sN.getTitle());
	}

	@POST//Changed
	@Path("{ktreeid}/subnodes/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_BooleanChanges<NodeLink> deleteSubnode(int id) {
		List<NodeLink> nodelinks = lm.getNodeLinksBySubnode(id);
		return new CDAR_BooleanChanges<NodeLink>(sm.deleteSubnode(id),
				nodelinks, "delete");
	}
	
	@GET//Changed
	@Path("{ktreeid}/subnodes/{nodeid}/zoomUp")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> zoomUpSubnode(@PathParam("nodeid") int nodeid) {
		return sm.zoomUp(nodeid);
	}
	
	@GET//Changed
	@Path("{ktreeid}/subnodes/{nodeid}/zoomDown")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Subnode> zoomDownSubnode(@PathParam("nodeid") int nodeid) {
		return sm.zoomDown(nodeid);
	}

	// TREE XML
	@GET//Changed
	@Path("{ktreeid}/simpleexport")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<XmlTree> getKnowledgeTreeSimpleXml(
			@PathParam("ktreeid") int ktreeid) {
		return xtm.getXmlTrees(ktreeid);
	}

	@POST//Changed
	@Path("{ktreeid}/simpleexport/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean deleteKnowledgeTreeSimpleXml(int id) {
		return new CDAR_Boolean(xtm.deleteXmlTree(id));
	}

	@POST//Changed
	@Path("{ktreeid}/singleexport/set")
	@Consumes(MediaType.APPLICATION_JSON)
	public CDAR_Boolean setKnowledgeTreeSimpleXml(int id) {
		xtm.cleanTree(id);
		return new CDAR_Boolean(xtm.setXmlTree(id));
	}

	@GET//Changed
	@Path("{ktreeid}/simpleexport/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public XmlTree addKnowledgeTreeSimpleXml(@PathParam("uid") int uid, @PathParam("ktreeid") int ktrid) {
		return xtm.addXmlTree(uid, ktrid);
	}
}
