package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.SubNode;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeSubNodeDao;

public class SubNodeModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public void removeSubNodeById(int id) {
		//kpdc.removeKnowledgeSubNode(1,id);		
	}

	public SubNode addSubNode(SubNode sN) {
		return new SubNode(kpdc.addKnowledgeSubNode(sN.getId(), sN.getTitle()));
	}

	public Set<SubNode> getSubNodes(int treeId) {
		Set<SubNode> sN = new HashSet<SubNode>();
		/*for (KnowledgeSubNodeDao ksnd : kpdc.getKnowledgeTreeById(treeId).getKnowledgeSubNodes())) {
			sN.add(new SubNode(ksnd));
		}*/
		return sN;
	}
}
