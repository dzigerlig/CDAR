package cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.MediaWikiManager;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.helpers.PropertyHelper;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.NodeRepository;
import cdar.dal.user.UserRepository;
import cdar.pl.controller.StatusHelper;

public class NodeManager {
	private NodeRepository nr = new NodeRepository();

	public Set<Node> getNodes(int treeId) throws EntityException,
			UnknownTreeException {
		Set<Node> nodes = new HashSet<Node>();
		for (Node node : nr.getNodes(treeId)) {
			nodes.add(node);
		}
		return nodes;
	}

	public Node getNode(int nodeId) throws UnknownNodeException,
			EntityException {
		return nr.getNode(nodeId);
	}

	public void deleteNode(int nodeId) throws UnknownNodeException {
		nr.deleteNode(nodeId);
	}

	public Node addNode(int uid, Node node) throws EntityException,
			UnknownUserException, UnknownTreeException, CreationException {
		if (node.getDirectoryId() == 0) {
			DirectoryRepository dr = new DirectoryRepository();
			int rootDirectoryId = dr.getDirectories(node.getTreeId()).get(0)
					.getId();
			node.setDirectoryId(rootDirectoryId);
		}

		if (node.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			node.setTitle(String.format("new %s",
					propertyHelper.getProperty("NODE_DESCRIPTION")));
		}

		node = nr.createNode(node);

		TemplateManager tm = new TemplateManager();
		String templateContent = tm.getDefaultKnowledgeTemplateText(node
				.getTreeId());

		if (templateContent == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			templateContent = String.format("== %S ==",
					propertyHelper.getProperty("NODE_DESCRIPTION"));
		}

		MediaWikiManager mwm = new MediaWikiManager();
		mwm.createWikiEntry(uid, node.getWikititle(), templateContent);

		return node;
	}

	public Node dropNode(int nodeId) throws UnknownNodeException, EntityException {
		Node node = getNode(nodeId);
		node.setDynamicTreeFlag(1);
		return nr.updateNode(node);
	}

	public Node renameNode(Node node) throws UnknownNodeException,
			EntityException {
		Node renamedNode = nr.getNode(node.getId());
		renamedNode.setTitle(node.getTitle());
		renamedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(renamedNode);
	}

	public Node undropNode(int nodeId) throws UnknownNodeException, EntityException {
		Node node = getNode(nodeId);
		node.setDynamicTreeFlag(0);
		return nr.updateNode(node);
	}

	public Node moveNode(Node node) throws UnknownNodeException,
			EntityException {
		Node movedNode = getNode(node.getId());
		movedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(movedNode);

		// TODO
		// kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
		// KnowledgeNodeDao node =
		// kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		// return new Node(node);
	}

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

	public Set<Node> drillUp(int uid, int nodeId) throws UnknownNodeException,
			EntityException, UnknownUserException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), nodes);
	}

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

	public Node getRoot(int treeId) throws EntityException,
			UnknownNodeException {
		return nr.getRoot(treeId);
	}
}
