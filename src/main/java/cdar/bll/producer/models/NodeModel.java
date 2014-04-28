package cdar.bll.producer.models;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.persistence.jdbc.producer.DirectoryRepository;
import cdar.dal.persistence.jdbc.producer.NodeRepository;

public class NodeModel {
	private WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();
	
	private NodeRepository nr = new NodeRepository();
	

	public Set<Node> getNodes(int treeid) throws SQLException {
		Set<Node> nodes = new HashSet<Node>();
		for (Node node : nr.getNodes(treeid)) {
			nodes.add(node);
		}
		return nodes;
	}

	public Node getNode(int nodeid) throws UnknownNodeException {
		return nr.getNode(nodeid);
	}

	public boolean deleteNode(int id) throws UnknownNodeException, Exception {
		return nr.deleteNode(getNode(id));
	}

	public Node addNode(int uid, int treeid, String title, int did) throws Exception {
		if (treeid != -1) {
			if (did == 0) {
				DirectoryRepository dr = new DirectoryRepository();
				int rootDirectoryId = dr.getDirectories(treeid).get(0).getId();
				did = rootDirectoryId;
			}

			TemplateModel tm = new TemplateModel();
			String templateContent = tm.getDefaultKnowledgeTemplate(treeid);
			Node node = new Node();
			node.setKtrid(treeid);
			node.setTitle(title);
			node.setDid(did);
			node = nr.createNode(node);

			if (templateContent == null) {
				templateContent = "== CDAR ==";
			}

			wikiHelper.addWikiEntry(node.getWikiTitle(), templateContent);

			MediaWikiCreationModel mwm = new MediaWikiCreationModel(uid, treeid,
					node.getWikiTitle(), templateContent, wikiHelper);
			mwm.start();
			return node;
		} else {
			Node node = new Node();
			node.setId(-1);
			return node;
		}
	}

	public Node dropNode(int id) throws Exception {
		Node node = getNode(id);
		node.setDynamicTreeFlag(1);
		return nr.updateNode(node);
	}

	public Node renameNode(Node node) throws Exception {
		Node renamedNode = nr.getNode(node.getId());
		renamedNode.setTitle(node.getTitle());
		renamedNode.setDid(node.getDid());
		return nr.updateNode(renamedNode);
	}

	public Node undropNode(int id) throws Exception {
		Node node = getNode(id);
		node.setDynamicTreeFlag(0);
		return nr.updateNode(node);
	}

	public Node moveNode(Node node) throws Exception {
		Node movedNode = getNode(node.getId());
		movedNode.setDid(node.getDid());
		return nr.updateNode(movedNode);

		// TODO
		// kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
		// KnowledgeNodeDao node =
		// kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		// return new Node(node);
	}

	public Node updateNode(Node node) throws Exception {
		Node updatedNode = getNode(node.getId());
		updatedNode.setDid(node.getDid());
		updatedNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
		updatedNode.setTitle(node.getTitle());
		return nr.updateNode(updatedNode);
	}

	public Set<Node> zoomUp(int nodeid) throws UnknownNodeException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeid));
		return rekZoomUp(nodeid, 2, nodes);
	}

	private Set<Node> rekZoomUp(int nodeid, int quantity, Set<Node> nodes) {
		if (quantity > 0) {
			for (Node node : nr.getSiblingNode(nodeid)) {
				nodes.add(node);
			}
			for (Node node : nr.getParentNode(nodeid)) {
				nodes.add(node);
				nodes = rekZoomUp(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	public Set<Node> zoomDown(int nodeid) throws UnknownNodeException {
		Set<Node> nodes = new HashSet<Node>();
		nodes.add(nr.getNode(nodeid));
		return rekZoomDown(nodeid, 2, nodes);
	}

	private Set<Node> rekZoomDown(int nodeid, int quantity, Set<Node> nodes) {
		if (quantity > 0) {
			for (Node node : nr.getFollowerNode(nodeid)) {
				nodes.add(node);
				nodes = rekZoomDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}
}
