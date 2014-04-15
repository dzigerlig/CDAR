package cdar.bll.export;

import java.util.List;
import java.util.Set;

import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;
import cdar.dal.persistence.jdbc.producer.TemplateDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class CDAR_TreeSimpleExport {
	private int treeId;
	private ProducerDaoController pdc = new ProducerDaoController();
	
	//XML Elements:
	private TreeDao tree;
	private List<TemplateDao> templates;
	private List<NodeDao> nodes;
	private Set<SubnodeDao> subnodes;
	private List<NodeLinkDao> links;
	private List<DirectoryDao> directories;
	
	public CDAR_TreeSimpleExport(int treeid) {
		setTree(pdc.getTree(treeid));
		setTemplates(pdc.getTemplates(treeid));
		setNodes(pdc.getNodes(treeid));
		setSubnodes(pdc.getSubnodesByTree(treeid));
		setLinks(pdc.getNodeLinks(treeid));
		setDirectories(pdc.getDirectories(treeid));
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	public TreeDao getTree() {
		return tree;
	}

	public void setTree(TreeDao tree) {
		this.tree = tree;
	}

	public List<TemplateDao> getTemplates() {
		return templates;
	}

	public void setTemplates(List<TemplateDao> templates) {
		this.templates = templates;
	}

	public List<NodeDao> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeDao> nodes) {
		this.nodes = nodes;
	}

	public Set<SubnodeDao> getSubnodes() {
		return subnodes;
	}

	public void setSubnodes(Set<SubnodeDao> subnodes) {
		this.subnodes = subnodes;
	}

	public List<NodeLinkDao> getLinks() {
		return links;
	}

	public void setLinks(List<NodeLinkDao> links) {
		this.links = links;
	}

	public List<DirectoryDao> getDirectories() {
		return directories;
	}

	public void setDirectories(List<DirectoryDao> directories) {
		this.directories = directories;
	}
}
