package cdar.bll.entity.producer;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.UserRole;
import cdar.bll.manager.DirectoryManager;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

/**
 * The Class TreeSimple.
 */
@XmlRootElement
public class TreeSimple {
	
	/** The tree. */
	private Tree tree;
	
	/** The nodes. */
	private Set<Node> nodes;
	
	/** The subnodes. */
	private Set<Subnode> subnodes;
	
	/** The node links. */
	private Set<NodeLink> nodeLinks;
	
	/** The directories. */
	private Set<Directory> directories;
	
	/** The templates. */
	private Set<Template> templates;
	
	/** The trm. */
	private TreeManager trm = new TreeManager(UserRole.PRODUCER);
	
	/** The nm. */
	private NodeManager nm = new NodeManager();
	
	/** The sm. */
	private SubnodeManager sm = new SubnodeManager();
	
	/** The nlm. */
	private NodeLinkManager nlm = new NodeLinkManager();
	
	/** The dm. */
	private DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
	
	/** The tm. */
	private TemplateManager tm = new TemplateManager();
	
	/**
	 * Instantiates a new tree simple.
	 */
	public TreeSimple() {}
	
	/**
	 * Instantiates a new tree simple.
	 *
	 * @param treeId the tree id
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public TreeSimple(int treeId) throws UnknownTreeException, EntityException, UnknownNodeException, UnknownUserException, UnknownProjectTreeException {
		setTree(trm.getTree(treeId));
		setNodes(nm.getNodes(treeId));
		setSubnodes(sm.getSubnodesFromTree(treeId));
		setNodeLinks(nlm.getNodeLinks(treeId));
		setDirectories(dm.getDirectories(treeId));
		setTemplates(tm.getKnowledgeTemplates(treeId));
	}

	/**
	 * Gets the tree.
	 *
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * Sets the tree.
	 *
	 * @param tree the new tree
	 */
	@XmlElement
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 */
	@XmlElement
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the subnodes.
	 *
	 * @return the subnodes
	 */
	public Set<Subnode> getSubnodes() {
		return subnodes;
	}

	/**
	 * Sets the subnodes.
	 *
	 * @param subnodes the new subnodes
	 */
	@XmlElement
	public void setSubnodes(Set<Subnode> subnodes) {
		this.subnodes = subnodes;
	}

	/**
	 * Gets the node links.
	 *
	 * @return the node links
	 */
	public Set<NodeLink> getNodeLinks() {
		return nodeLinks;
	}

	/**
	 * Sets the node links.
	 *
	 * @param nodeLinks the new node links
	 */
	@XmlElement
	public void setNodeLinks(Set<NodeLink> nodeLinks) {
		this.nodeLinks = nodeLinks;
	}

	/**
	 * Gets the directories.
	 *
	 * @return the directories
	 */
	public Set<Directory> getDirectories() {
		return directories;
	}

	/**
	 * Sets the directories.
	 *
	 * @param directories the new directories
	 */
	@XmlElement
	public void setDirectories(Set<Directory> directories) {
		this.directories = directories;
	}

	/**
	 * Gets the templates.
	 *
	 * @return the templates
	 */
	public Set<Template> getTemplates() {
		return templates;
	}

	/**
	 * Sets the templates.
	 *
	 * @param templates the new templates
	 */
	@XmlElement
	public void setTemplates(Set<Template> templates) {
		this.templates = templates;
	}
}
