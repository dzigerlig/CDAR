package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.model.user.User;
import cdar.bll.producer.Directory;
import cdar.bll.producer.KnowledgeNode;
import cdar.bll.producer.Tree;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class TreeModel {

	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
	private UserDaoController udc = new UserDaoController();
	
	public Set<Tree> getKnowledgeTreesByUid(int uid) {
		User user = new User(udc.getUserById(uid));
		return user.getKnowledgeTrees();
	}

	public int addKnowledgeTreeByUid(int uid, String treeName) {
		try {
			kpdc.addKnowledgeTreeByUid(uid, treeName);
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int removeKnowledgeTreeById(int uid, int ktreeid) {
		return kpdc.removeKnowledgeTreeById(uid, ktreeid);
	}

	public Tree getKnowledgeTreeById(int treeId) {
		return new Tree(kpdc.getKnowledgeTreeById(treeId));
	}

	public KnowledgeNode getKnowledgeNodeById(int nodeid) {
		return new KnowledgeNode(kpdc.getKnowledgeNodeById(nodeid));
	}

	public Set<KnowledgeNode> getKnowledgeNodes(int ktreeid) {
		Set<KnowledgeNode> set = new HashSet<KnowledgeNode>();
		
		for (KnowledgeNodeDao pnd : kpdc.getKnowledgeTreeById(ktreeid).getKnowledgeNodes()) {
			set.add(new KnowledgeNode(pnd));
		}
		return set;
	}
	
	public Directory getDictionariesById(int dictionaryid) {
		return new Directory(kpdc.getDictionaryById(dictionaryid));
	}
	
}
