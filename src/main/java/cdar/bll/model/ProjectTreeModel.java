package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.model.user.User;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeConsumerDaoController;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectNodeDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class ProjectTreeModel {
	
	private KnowledgeConsumerDaoController kcdc = new KnowledgeConsumerDaoController();
	private UserDaoController udc = new UserDaoController();
	
	public Set<ProjectTree> getProjectTreesByUid(int uid) {
		User user = new User(udc.getUserById(uid));
		return user.getProjectTrees();
	}

	public Integer addProjectTreeByUid(int uid, String treeName) {
		try {
			kcdc.addKnowledgeProjectTreeByUid(uid, treeName);
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int removeProjectTreeById(int uid, int ptreeid) {
		return kcdc.removeKnowledgeProjectTreeById(uid, ptreeid);
	}
	
	public ProjectNode getProjectNodeById(int nodeid) {
		return new ProjectNode(kcdc.getKnowledgeProjectNodeById(nodeid));
	}
	
	public ProjectTree getProjectTreeById(int ptreeid) {
		return new ProjectTree(kcdc.getKnowledgeProjectTreeById(ptreeid));
	}

	public void addKnowledgeTreeToProjectTree(int ktreeid, int ptreeid) {
		kcdc.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
	}

	public Set<ProjectNode> getProjectNodes(int ptreeid) {
		Set<ProjectNode> set = new HashSet<ProjectNode>();
		
		for (KnowledgeProjectNodeDao pnd : kcdc.getKnowledgeProjectTreeById(ptreeid).getKnowledgeProjectNodes()) {
			set.add(new ProjectNode(pnd));
		}
		
		return set;
	}
}
