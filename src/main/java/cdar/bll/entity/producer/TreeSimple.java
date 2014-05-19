package cdar.bll.entity.producer;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.UserRole;
import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

@XmlRootElement
public class TreeSimple {
	private Tree tree;
	private Set<Node> nodes;
	private Set<Subnode> subnodes;
	private Set<NodeLink> nodeLinks;
	private Set<Directory> directories;
	private Set<Template> templates;
	
	private TreeManager trm = new TreeManager(UserRole.PRODUCER);
	private NodeManager nm = new NodeManager();
	private SubnodeManager sm = new SubnodeManager();
	private NodeLinkManager nlm = new NodeLinkManager();
	private DirectoryManager dm = new DirectoryManager();
	private TemplateManager tm = new TemplateManager();
	
	public TreeSimple() {}
	
	public TreeSimple(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownProjectTreeException {
		System.out.println("treeid: " + treeId);
		setTree(trm.getTree(treeId));
		setNodes(nm.getNodes(treeId));
		setSubnodes(sm.getSubnodesFromTree(treeId));
		setNodeLinks(nlm.getNodeLinks(treeId));
		setDirectories(dm.getDirectories(treeId));
		setTemplates(tm.getKnowledgeTemplates(treeId));
	}

	public Tree getTree() {
		return tree;
	}

	@XmlElement
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	@XmlElement
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Subnode> getSubnodes() {
		return subnodes;
	}

	@XmlElement
	public void setSubnodes(Set<Subnode> subnodes) {
		this.subnodes = subnodes;
	}

	public Set<NodeLink> getNodeLinks() {
		return nodeLinks;
	}

	@XmlElement
	public void setNodeLinks(Set<NodeLink> nodeLinks) {
		this.nodeLinks = nodeLinks;
	}

	public Set<Directory> getDirectories() {
		return directories;
	}

	@XmlElement
	public void setDirectories(Set<Directory> directories) {
		this.directories = directories;
	}

	public Set<Template> getTemplates() {
		return templates;
	}

	@XmlElement
	public void setTemplates(Set<Template> templates) {
		this.templates = templates;
	}
}
