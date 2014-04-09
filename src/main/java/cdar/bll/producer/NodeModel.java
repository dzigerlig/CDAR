package cdar.bll.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class NodeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public Set<Node> getNodes(int treeid) {
		Set<Node> nodes = new HashSet<Node>();
		for (NodeDao node : pdc.getNodes(treeid)) {
			nodes.add(new Node(node));
		}
		return nodes;
	}
	
	public Node getNode(int nodeid) {
		return new Node(pdc.getNode(nodeid));
	}

	public boolean deleteNodeById(int id) {
		return pdc.getNode(id).delete();
	}

	public Node addNode(int treeid, String title, int did) {
		NodeDao node = new NodeDao(treeid, title, did);
		return new Node(node.create());
	}

	public Node dropNode(int id) {
		NodeDao node = pdc.getNode(id);
		node.setDynamicTreeFlag(1);
		return new Node(node.update());
	}

	public Node renameNode(Node n) {
		NodeDao node = pdc.getNode(n.getId());
		node.setTitle(n.getTitle());
		node.setDid(n.getDid());
		return new Node(node.update());
	}

	public Node undropNode(int id) {
		NodeDao node = pdc.getNode(id);
		node.setDynamicTreeFlag(0);
		return new Node(node.update());
	}

	public Node moveNode(Node n) {
		NodeDao node = pdc.getNode(n.getId());
		node.setDid(n.getDid());
		return new Node(node.update());

		//TODO
//		kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
//		KnowledgeNodeDao node = kpdc.getKnowledgeNodeById(nodemapping.getKnid());
//		return new Node(node);
	}
}
