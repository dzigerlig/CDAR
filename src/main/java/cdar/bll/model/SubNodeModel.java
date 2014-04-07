package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.SubNode;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeSubNodeDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubNodeDao;

public class SubNodeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public void removeSubNodeById(int id) {
		pdc.getSubNode(id).delete();
	}

	public SubNode addSubNode(SubNode sN) {
		SubNodeDao subnode = new SubNodeDao(sN.getKnid(), sN.getTitle());
		return new SubNode(subnode.create());
	}

	//whole tree
	public Set<SubNode> getSubNodes(int treeId) {
		Set<SubNode> ln = new HashSet<SubNode>();

		for (NodeDao knd : pdc.getNodes(treeId)) {
			for (SubNodeDao nd : pdc.getSubNodes(knd.getId())) {
				ln.add(new SubNode(nd));
			}
		}
		return ln;
	}
}
