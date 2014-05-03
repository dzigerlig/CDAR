package cdar.bll.manager.producer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.NodeRepository;

public class NodeManager {
	private WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();

	private NodeRepository nr = new NodeRepository();

	public Set<Node> getNodes(int treeid) throws SQLException {
		Set<Node> nodes = new HashSet<Node>();
		for (Node node : nr.getNodes(treeid)) {
			nodes.add(node);
		}
		return nodes;
	}

	public Node getNode(int nodeId) throws UnknownNodeException {
		return nr.getNode(nodeId);
	}

	public void deleteNode(int nodeId) throws UnknownNodeException, Exception {
		nr.deleteNode(nodeId);
	}

	public Node addNode(int uid, Node node)
			throws Exception {
		if (node.getDirectoryId() == 0) {
			DirectoryRepository dr = new DirectoryRepository();
			int rootDirectoryId = dr.getDirectories(node.getTreeId()).get(0).getId();
			node.setDirectoryId(rootDirectoryId);
		}

		TemplateManager tm = new TemplateManager();
		String templateContent = tm.getDefaultKnowledgeTemplateText(node.getTreeId());
		node = nr.createNode(node);

		if (templateContent == null) {
			templateContent = "== CDAR ==";
		}

		wikiHelper.addWikiEntry(node.getWikititle(), templateContent);

		MediaWikiCreationModel mwm = new MediaWikiCreationModel(uid, node.getTreeId(),
				node.getWikititle(), templateContent, wikiHelper);
		mwm.start();
		return node;
	}

	public Node dropNode(int id) throws Exception {
		Node node = getNode(id);
		node.setDynamicTreeFlag(1);
		return nr.updateNode(node);
	}

	public Node renameNode(Node node) throws Exception {
		Node renamedNode = nr.getNode(node.getId());
		renamedNode.setTitle(node.getTitle());
		renamedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(renamedNode);
	}

	public Node undropNode(int id) throws Exception {
		Node node = getNode(id);
		node.setDynamicTreeFlag(0);
		return nr.updateNode(node);
	}

	public Node moveNode(Node node) throws Exception {
		Node movedNode = getNode(node.getId());
		movedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(movedNode);

		// TODO
		// kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
		// KnowledgeNodeDao node =
		// kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		// return new Node(node);
	}

	public Node updateNode(Node node) throws Exception {
		Node updatedNode = getNode(node.getId());
		updatedNode.setDirectoryId(node.getDirectoryId());
		updatedNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
		updatedNode.setTitle(node.getTitle());
		return nr.updateNode(updatedNode);
	}

	public Set<Node> zoomUp(int nodeId) throws UnknownNodeException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveZoomUp(nodeId, 2, nodes);
	}

	private Set<Node> recursiveZoomUp(int nodeId, int quantity, Set<Node> nodes) {
		if (quantity > 0) {
			for (Node node : nr.getSiblingNode(nodeId)) {
				nodes.add(node);
			}
			for (Node node : nr.getParentNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveZoomUp(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	public Set<Node> zoomDown(int nodeId) throws UnknownNodeException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveZoomDown(nodeId, 2, nodes);
	}

	private Set<Node> recursiveZoomDown(int nodeId, int quantity, Set<Node> nodes) {
		if (quantity > 0) {
			for (Node node : nr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveZoomDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}
}
