package ch.cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.helper.SubnodeCopyHelper;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exception.CreationException;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownNodeException;
import ch.cdar.dal.exception.UnknownProjectNodeException;
import ch.cdar.dal.exception.UnknownSubnodeException;
import ch.cdar.dal.exception.UnknownTreeException;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.helper.PropertyHelper;
import ch.cdar.dal.repository.producer.DirectoryRepository;
import ch.cdar.dal.repository.producer.NodeRepository;
import ch.cdar.dal.repository.user.UserRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeManager.
 */
public class NodeManager {

	/** The Node Repository. */
	private NodeRepository nr = new NodeRepository();

	/**
	 * Gets the nodes.
	 * 
	 * @param treeId
	 *            the tree id
	 * @return the nodes
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownTreeException
	 *             the unknown tree exception
	 */
	public Set<Node> getNodes(int treeId) throws EntityException,
			UnknownTreeException {
		Set<Node> nodes = new HashSet<Node>();
		for (Node node : nr.getNodes(treeId)) {
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * Gets the node.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the node
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws EntityException
	 *             the entity exception
	 */
	public Node getNode(int nodeId) throws UnknownNodeException,
			EntityException {
		return nr.getNode(nodeId);
	}

	/**
	 * Delete node.
	 * 
	 * @param nodeId
	 *            the node id
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 */
	public void deleteNode(int nodeId) throws UnknownNodeException {
		nr.deleteNode(nodeId);
	}

	/**
	 * Adds the node.
	 *
	 * @param uid            the uid
	 * @param node            the node
	 * @param templateContent the template content
	 * @return the node
	 * @throws EntityException             the entity exception
	 * @throws UnknownUserException             the unknown user exception
	 * @throws UnknownTreeException             the unknown tree exception
	 * @throws CreationException             the creation exception
	 */
	public Node addNode(int uid, Node node, String templateContent)
			throws EntityException, UnknownUserException, UnknownTreeException,
			CreationException {
		if (node.getDirectoryId() == 0) {
			DirectoryRepository dr = new DirectoryRepository();
			int rootDirectoryId = dr.getDirectories(node.getTreeId()).get(0)
					.getId();
			node.setDirectoryId(rootDirectoryId);
		}

		if (node.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			node.setTitle(String.format("new %s",
					propertyHelper.getProperty("CDAR_NODE_DESCRIPTION")));
		}

		node = nr.createNode(node);

		TemplateManager tm = new TemplateManager();

		if (templateContent == null) {
			templateContent = tm.getDefaultKnowledgeTemplateText(node
					.getTreeId());

			if (templateContent == null) {
				PropertyHelper propertyHelper = new PropertyHelper();
				templateContent = String.format("== %S ==",
						propertyHelper.getProperty("CDAR_NODE_DESCRIPTION"));
			}
		}

		MediaWikiManager mwm = new MediaWikiManager();
		mwm.createWikiEntry(uid, node.getWikititle(), templateContent);

		return node;
	}

	/**
	 * Rename node.
	 * 
	 * @param node
	 *            the node
	 * @return the node
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws EntityException
	 *             the entity exception
	 */
	public Node renameNode(Node node) throws UnknownNodeException,
			EntityException {
		Node renamedNode = nr.getNode(node.getId());
		renamedNode.setTitle(node.getTitle());
		renamedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(renamedNode);
	}

	/**
	 * Update node.
	 * 
	 * @param node
	 *            the node
	 * @return the node
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws EntityException
	 *             the entity exception
	 */
	public Node updateNode(Node node) throws UnknownNodeException,
			EntityException {
		Node updatedNode = getNode(node.getId());

		if (node.getDirectoryId() != 0) {
			updatedNode.setDirectoryId(node.getDirectoryId());
		}

		updatedNode.setDynamicTreeFlag(node.getDynamicTreeFlag());

		if (node.getTitle() != null) {
			updatedNode.setTitle(node.getTitle());
		}
		if (node.getTreeId() != 0) {
			updatedNode.setTreeId(node.getTreeId());
		}
		if (node.getWikititle() != null) {
			updatedNode.setWikititle(node.getWikititle());
		}

		return nr.updateNode(updatedNode);
	}

	/**
	 * Drill up.
	 * 
	 * @param uid
	 *            the uid
	 * @param nodeId
	 *            the node id
	 * @return the sets the
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownUserException
	 *             the unknown user exception
	 */
	public Set<Node> drillUp(int uid, int nodeId) throws UnknownNodeException,
			EntityException, UnknownUserException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), nodes);
	}

	/**
	 * Recursive drill up.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param quantity
	 *            the quantity
	 * @param nodes
	 *            the nodes
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 */
	private Set<Node> recursiveDrillUp(int nodeId, int quantity, Set<Node> nodes)
			throws EntityException {
		if (quantity > 0) {
			for (Node node : nr.getSiblingNode(nodeId)) {
				nodes.add(node);
			}
			for (Node node : nr.getParentNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveDrillUp(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	/**
	 * Drill down.
	 * 
	 * @param uid
	 *            the uid
	 * @param treeId
	 *            the tree id
	 * @param nodeId
	 *            the node id
	 * @return the sets the
	 * @throws UnknownNodeException
	 *             the unknown node exception
	 * @throws EntityException
	 *             the entity exception
	 * @throws UnknownUserException
	 *             the unknown user exception
	 */
	public Set<Node> drillDown(int uid, int treeId, int nodeId)
			throws UnknownNodeException, EntityException, UnknownUserException {
		if (nodeId == 0) {
			Node rootNode = getRoot(treeId);
			if (rootNode == null) {
				return null;
			}
			nodeId = rootNode.getId();
		}

		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), nodes);
	}

	/**
	 * Recursive drill down.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param quantity
	 *            the quantity
	 * @param nodes
	 *            the nodes
	 * @return the sets the
	 * @throws EntityException
	 *             the entity exception
	 */
	private Set<Node> recursiveDrillDown(int nodeId, int quantity,
			Set<Node> nodes) throws EntityException {
		if (quantity > 0) {
			for (Node node : nr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveDrillDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	/**
	 * Gets the root.
	 *
	 * @param treeId            the tree id
	 * @return the root
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	private Node getRoot(int treeId) throws EntityException,
			UnknownNodeException {
		return nr.getRoot(treeId);
	}

	/**
	 * Copy node.
	 *
	 * @param uid the uid
	 * @param node the node
	 * @return the node
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws CreationException the creation exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
	public Node copyNode(int uid, Node node) throws UnknownNodeException, EntityException, UnknownProjectNodeException, UnknownUserException, UnknownTreeException, CreationException, UnknownSubnodeException {
		int nodeId = node.getId();
		MediaWikiManager mwm = new MediaWikiManager();
		node = getNode(nodeId);
		node.setWikititle(null);
		node.setDynamicTreeFlag(0);
		WikiEntry wikiEntry = mwm.getKnowledgeNodeWikiEntry(node.getId());
		Node newNode = addNode(uid, node, wikiEntry.getWikiContentPlain());
		SubnodeCopyHelper sch = new SubnodeCopyHelper(uid, nodeId, newNode.getId(), newNode.getTreeId(), UserRole.PRODUCER);
		sch.start();
		return newNode;
	}
}
