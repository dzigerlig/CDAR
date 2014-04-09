package cdar.bll.consumer.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.NodeModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.TreeModel;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

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
		//cdc.addKnowledgeTreeToProjectTree(ktreeid, ptreeid);
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
		
		for (NodeLink nodelink : nlm.getLinks(ktreeid)) {
			ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(linkMapping.get(nodelink.getSourceId()), linkMapping.get(nodelink.getTargetId()), ptreeid);
			projectnodelink.setKpnsnid(nodelink.getKsnid());
			projectnodelink.create();
		}
	}

	public Set<ProjectNode> getProjectNodes(int ptreeid) {
		Set<ProjectNode> projectnodes = new HashSet<ProjectNode>();
		
		for (ProjectNodeDao pnd : cdc.getProjectNodes(ptreeid)) {
			projectnodes.add(new ProjectNode(pnd));
		}
		
		return projectnodes;
	}
}
