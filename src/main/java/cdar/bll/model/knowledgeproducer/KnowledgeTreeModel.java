package cdar.bll.model.knowledgeproducer;

import java.util.Set;

import cdar.bll.model.knowledgeconsumer.ProjectNode;
import cdar.bll.model.user.User;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class KnowledgeTreeModel {

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
}
