package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;

public class ProjectTreeModel {
	
	private ConsumerDaoController cdc = new ConsumerDaoController();
	
	public Set<ProjectTree> getProjectTreesByUid(int uid) {
		Set<ProjectTree> trees = new HashSet<ProjectTree>();
		for (ProjectTreeDao tree : cdc.getProjectTrees(uid)) {
			trees.add(new ProjectTree(tree));
		}
		return trees;
	}

	public ProjectTree addProjectTreeByUid(int uid, String treeName) {
		try {
			ProjectTreeDao tree = new ProjectTreeDao(uid, treeName);
			return new ProjectTree(tree.create());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ProjectTree(-1);
	}

	public boolean removeProjectTreeById(int uid, int ptreeid) {
		ProjectTreeDao tree = cdc.getProjectTreeById(ptreeid);
		return tree.delete();
	}
	
	public ProjectNode getProjectNodeById(int nodeid) {
		return new ProjectNode(cdc.getProjectNode(nodeid));
	}
	
	public ProjectTree getProjectTreeById(int ptreeid) {
		return new ProjectTree(cdc.getProjectTreeById(ptreeid));
	}

	public void addKnowledgeTreeToProjectTree(int ktreeid, int ptreeid) {
		cdc.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
	}

	public Set<ProjectNode> getProjectNodes(int ptreeid) {
		Set<ProjectNode> set = new HashSet<ProjectNode>();
		
		for (ProjectNodeDao pnd : cdc.getProjectNodes(ptreeid)) {
			set.add(new ProjectNode(pnd));
		}
		
		return set;
	}
}
