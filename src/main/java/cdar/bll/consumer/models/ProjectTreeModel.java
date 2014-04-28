package cdar.bll.consumer.models;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkDao;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeRepository;

public class ProjectTreeModel {

	private ProjectTreeRepository ptr = new ProjectTreeRepository();
	private ProjectNodeRepository pnr = new ProjectNodeRepository();

	public Set<ProjectTree> getProjectTrees(int uid) throws SQLException {
		Set<ProjectTree> projectTrees = new HashSet<ProjectTree>();
		for (ProjectTree projectTree : ptr.getProjectTrees(uid)) {
			projectTrees.add(projectTree);
		}
		return projectTrees;
	}

	public ProjectTree addProjectTree(int uid, String treeTitle)
			throws Exception {
		ProjectTree projectTree = new ProjectTree();
		projectTree.setUid(uid);
		projectTree.setTitle(treeTitle);
		return ptr.createProjectTree(projectTree);
	}

	public ProjectTree getProjectTree(int ptreeId) throws UnknownProjectTreeException {
		return ptr.getProjectTree(ptreeId);
	}

	public void addKnowledgeTreeToProjectTree(int ktreeId, int ptreeId)
			throws SQLException, UnknownProjectNodeException, UnknownProjectTreeException {
		Map<Integer, Integer> linkMapping = new HashMap<Integer, Integer>();
		NodeModel nm = new NodeModel();
		SubnodeModel snm = new SubnodeModel();
		NodeLinkModel nlm = new NodeLinkModel();

		for (Node node : nm.getNodes(ktreeId)) {
			ProjectNode projectNode = new ProjectNode();
			projectNode.setRefProjectTreeId(ptreeId);
			projectNode.setTitle(node.getTitle());
			projectNode.setWikiTitle(node.getWikiTitle());
			projectNode = pnr.createProjectNode(projectNode);
			linkMapping.put(node.getId(), projectNode.getId());
		}

		for (Subnode subnode : snm.getSubnodesFromTree(ktreeId)) {
			ProjectSubnodeDao projectsubnode = new ProjectSubnodeDao(
					linkMapping.get(subnode.getKnid()), subnode.getPosition());
			projectsubnode.setTitle(subnode.getTitle());
			projectsubnode.setWikititle(subnode.getWikiTitle());
			projectsubnode.create();
		}

		for (NodeLink nodelink : nlm.getNodeLinks(ktreeId)) {
			ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(
					linkMapping.get(nodelink.getSourceId()),
					linkMapping.get(nodelink.getTargetId()), ptreeId);
			projectnodelink.setKpnsnid(nodelink.getKsnid());
			projectnodelink.create();
		}
	}

	public boolean deleteProjectTree(int ptreeId) throws UnknownProjectTreeException {
		return ptr.deleteProjectTree(ptreeId);
	}

	public ProjectTree updateProjectTree(ProjectTree projectTree) throws Exception {
		ProjectTree updatedProjectTree = ptr.getProjectTree(projectTree.getId());
		updatedProjectTree.setTitle(projectTree.getTitle());
		return ptr.updateProjectTree(updatedProjectTree);
	}
}
