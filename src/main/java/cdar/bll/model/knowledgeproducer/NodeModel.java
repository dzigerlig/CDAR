package cdar.bll.model.knowledgeproducer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class NodeModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();

	public List<Node> getNodes() {
		List<Node> ln = new ArrayList<Node>();
		for (KnowledgeNodeDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodes()) {
			ln.add(new Node(knd));
		}
		return ln;
	}

	public List<Node> getNodesWithFollowers(int Id, int quantityOfFollowers) {
		List<Node> ln = new ArrayList<Node>();
		for (KnowledgeNodeDao knd : kpdc.getKnowledgeTreeById(1).getKnowledgeNodes()) {
			ln.add(new Node(knd));
		}
		return ln;
	}

	public void removeNodeById(int id) {
		kpdc.removeKnowledgeNode(1, id);
		System.out.println("Node " + id + " removed");
		//kpdc.removeKnowledgeNode(1, id);
	}

	public Node addNode(Node n)	
	{ 
		return new Node(kpdc.addKnowledgeNode(n.getRefTreeId(),n.getTitle()));
		//return new Node(new Random().nextInt(),new Date(),new Date(),"newTitle","wikiTitle",0,0);
	}
}
