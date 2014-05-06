package cdar.bll.entity.export;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.producer.Template;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.TreeRepository;

@XmlRootElement
public class CDAR_TreeSimple {
	private int xmlTreeId;
	
	private Tree tree;
	private Set<Template> templates;
	private Set<Node> nodes;
	private Set<Subnode> subnodes;
	private Set<NodeLink> links;
	private Set<Directory> directories;
	
	private TemplateManager tem = new TemplateManager();
	private NodeManager nm = new NodeManager();
	private SubnodeManager snm = new SubnodeManager();
	private NodeLinkManager nlm = new NodeLinkManager();
	private DirectoryManager dm = new DirectoryManager();
	
	private TreeRepository tr = new TreeRepository();
	
	public CDAR_TreeSimple() {}
	
	public CDAR_TreeSimple(int treeid) throws EntityException, UnknownTreeException, UnknownNodeException, UnknownUserException {
		setTree(tr.getTree(treeid));
		setTemplates(tem.getKnowledgeTemplates(treeid));
		setNodes(nm.getNodes(treeid));
		setSubnodes(snm.getSubnodesFromTree(treeid));
		setLinks(nlm.getNodeLinks(treeid));
		setDirectories(dm.getDirectories(treeid));
	}

	public int getXmlTreeId() {
		return xmlTreeId;
	}
	
	public void setXmlTreeId(int xmlTreeId) {
		this.xmlTreeId = xmlTreeId;
	}

	public Tree getTree() {
		return tree;
	}

	@XmlElement
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Set<Template> getTemplates() {
		return templates;
	}

	@XmlElement
	public void setTemplates(Set<Template> templates) {
		this.templates = templates;
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

	public Set<NodeLink> getLinks() {
		return links;
	}

	@XmlElement
	public void setLinks(Set<NodeLink> links) {
		this.links = links;
	}

	public Set<Directory> getDirectories() {
		return directories;
	}

	@XmlElement
	public void setDirectories(Set<Directory> directories) {
		this.directories = directories;
	}
}
