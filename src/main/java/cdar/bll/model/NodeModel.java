package cdar.bll.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class NodeModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();

	public Set<Node> getNodes() {
		Set<Node> ln = new HashSet<Node>();
		for (KnowledgeNodeDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodes()) {
			ln.add(new Node(knd));
		}
		return ln;
	}

	public void removeNodeById(int id) {
		System.out.println("remove node"+id);
		kpdc.removeKnowledgeNode(1, id);
	}

	public Node addNode(Node n)	
	{ 
		return new Node(kpdc.addKnowledgeNode(n.getRefTreeId(),n.getTitle()));
	}
}
