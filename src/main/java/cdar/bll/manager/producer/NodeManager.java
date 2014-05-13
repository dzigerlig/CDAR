package cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.NodeRepository;

public class NodeManager {
	private NodeRepository nr = new NodeRepository();

	public Set<Node> getNodes(int treeid) throws EntityException, UnknownTreeException   {
		Set<Node> nodes = new HashSet<Node>();
		for (Node node : nr.getNodes(treeid)) {
			nodes.add(node);
		}
		return nodes;
	}

	public Node getNode(int nodeId) throws UnknownNodeException, EntityException {
		return nr.getNode(nodeId);
	}

	public void deleteNode(int nodeId) throws UnknownNodeException {
		nr.deleteNode(nodeId);
	}

	public Node addNode(int uid, Node node) throws EntityException, UnknownUserException, UnknownTreeException, CreationException  {
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
		
		MediaWikiModel mwm = new MediaWikiModel();
		mwm.createWikiEntry(uid, node.getWikititle(), templateContent);
		
		return node;
	}

	public Node dropNode(int id) throws UnknownNodeException, EntityException   {
		Node node = getNode(id);
		node.setDynamicTreeFlag(1);
		return nr.updateNode(node);
	}

	public Node renameNode(Node node) throws UnknownNodeException, EntityException   {
		Node renamedNode = nr.getNode(node.getId());
		renamedNode.setTitle(node.getTitle());
		renamedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(renamedNode);
	}

	public Node undropNode(int id) throws UnknownNodeException, EntityException   {
		Node node = getNode(id);
		node.setDynamicTreeFlag(0);
		return nr.updateNode(node);
	}

	public Node moveNode(Node node) throws UnknownNodeException, EntityException   {
		Node movedNode = getNode(node.getId());
		movedNode.setDirectoryId(node.getDirectoryId());
		return nr.updateNode(movedNode);

		// TODO
		// kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
		// KnowledgeNodeDao node =
		// kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		// return new Node(node);
	}

	public Node updateNode(Node node) throws UnknownNodeException, EntityException   {
		Node updatedNode = getNode(node.getId());
		
		if (node.getDirectoryId()!=0) {
			updatedNode.setDirectoryId(node.getDirectoryId());
		}
		
		updatedNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
		
		if (node.getTitle()!=null) {
			updatedNode.setTitle(node.getTitle());
		}
		if (node.getTreeId()!=0) {
			updatedNode.setTreeId(node.getTreeId());
		}
		
		return nr.updateNode(updatedNode);
	}

	public Set<Node> drillUp(int nodeId) throws UnknownNodeException, EntityException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveZoomUp(nodeId, 2, nodes);
	}

	private Set<Node> recursiveZoomUp(int nodeId, int quantity, Set<Node> nodes) throws EntityException {
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

	public Set<Node> drillDown(int nodeId) throws UnknownNodeException, EntityException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeId));
		return recursiveZoomDown(nodeId, 2, nodes);
	}

	private Set<Node> recursiveZoomDown(int nodeId, int quantity, Set<Node> nodes) throws EntityException {
		if (quantity > 0) {
			for (Node node : nr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveZoomDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}
}
