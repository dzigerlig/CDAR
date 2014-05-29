package ch.cdar.bll.entity.producer;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.producer.NodeLinkManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.bll.manager.producer.TemplateManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class TreeSimple representing a Simple Tree Export/Import
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
	
	/** The tree manager. */
	private TreeManager trm = new TreeManager(UserRole.PRODUCER);
	
	/** The node manager. */
	private NodeManager nm = new NodeManager();
	
	/** The subnode manager. */
	private SubnodeManager sm = new SubnodeManager();
	
	/** The node link manager. */
	private NodeLinkManager nlm = new NodeLinkManager();
	
	/** The directory manager. */
	private DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
	
	/** The template manager. */
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
