package cdar.bll.consumer.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdar.bll.consumer.ProjectTree;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;

public class ProjectTreeModel {
	
	private ConsumerDaoController cdc = new ConsumerDaoController();
	
	public Set<ProjectTree> getProjectTrees(int uid) {
		Set<ProjectTree> trees = new HashSet<ProjectTree>();
		for (ProjectTreeDao tree : cdc.getProjectTrees(uid)) {
			trees.add(new ProjectTree(tree));
		}
		return trees;
	} 

	public ProjectTree addProjectTree(int uid, String treeName) {
		try {
			ProjectTreeDao tree = new ProjectTreeDao(uid, treeName);
			return new ProjectTree(tree.create());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ProjectTree(-1);
	}
	
	public ProjectTree getProjectTree(int ptreeid) {
		return new ProjectTree(cdc.getProjectTree(ptreeid));
	}

	public void addKnowledgeTreeToProjectTree(int ktreeid, int ptreeid) {
		Map<Integer, Integer> linkMapping = new HashMap<Integer, Integer>();
		NodeModel nm = new NodeModel();
		NodeLinkModel nlm = new NodeLinkModel();
		
		for (Node node : nm.getNodes(ktreeid)) {
			ProjectNodeDao projectnode = new ProjectNodeDao(ptreeid);
			projectnode.setTitle(node.getTitle());
			projectnode.setWikititle(node.getWikiTitle());
			projectnode.create();
			linkMapping.put(node.getId(), projectnode.getId());
		}
		
		for (NodeLink nodelink : nlm.getNodeLinks(ktreeid)) {
			ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(linkMapping.get(nodelink.getSourceId()), linkMapping.get(nodelink.getTargetId()), ptreeid);
			projectnodelink.setKpnsnid(nodelink.getKsnid());
			projectnodelink.create();
		}
	}

	public boolean deleteProjectTree(int ptreeid) {
		return cdc.getProjectTree(ptreeid).delete();
	}

	public ProjectTree updateProjectTree(ProjectTree tree) {
		ProjectTreeDao projectTreeDao = cdc.getProjectTree(tree.getId());
		projectTreeDao.setName(tree.getName());
		return new ProjectTree(projectTreeDao.update());
	}
}
