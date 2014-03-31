package cdar.bll.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cdar.bll.producer.KnowledgeNode;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeMapping;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class NodeModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();

	public Set<Node> getNodes() {
		Set<Node> ln = new HashSet<Node>();
		for (KnowledgeNodeDao knd : kpdc.getKnowledgeTreeById(1)
				.getKnowledgeNodes()) {
			ln.add(new Node(knd));
		}
		return ln;
	}

	public void deleteNodeById(int id) {
		System.out.println("remove node" + id);
		kpdc.removeKnowledgeNode(1, id);
	}

	public Node addNode(Node n) {
		return new Node(kpdc.addKnowledgeNode(n.getRefTreeId(), n.getTitle()));
	}

	public Node dropNode(int id) {
		System.out.println(id+" dropped");
		KnowledgeNodeDao node = kpdc.getKnowledgeNodeById(id);
		node.setDynamicTreeFlag(1);
		return new Node(kpdc.updateNode(node));
	}

	public Node renameNode(Node n) {
		System.out.println(n.getTitle());
		KnowledgeNodeDao node = kpdc.getKnowledgeNodeById(n.getId());
		node.setTitle(n.getTitle());
		return new Node(kpdc.updateNode(node));
	}

	public Node undropNode(int id) {
		System.out.println(id+" undropped");
		KnowledgeNodeDao node = kpdc.getKnowledgeNodeById(id);
		node.setDynamicTreeFlag(0);
		return new Node(kpdc.updateNode(node));
	}

	public Node moveNode(NodeMapping nodemapping) {
		System.out.println("node moved");	
		KnowledgeNodeDao node = kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		DictionaryDao dic = kpdc.getDictionaryById(nodemapping.getDid());
		node.setDictionary(dic);
		return new Node(kpdc.updateNode(node));
	}
}
