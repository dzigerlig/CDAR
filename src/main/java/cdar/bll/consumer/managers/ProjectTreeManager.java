package cdar.bll.consumer.managers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectNodeLink;
import cdar.bll.consumer.ProjectSubnode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.managers.NodeLinkManager;
import cdar.bll.producer.managers.NodeManager;
import cdar.bll.producer.managers.SubnodeManager;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeRepository;

public class ProjectTreeManager {

	private ProjectTreeRepository ptr = new ProjectTreeRepository();
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

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
		NodeManager nm = new NodeManager();
		SubnodeManager snm = new SubnodeManager();
		NodeLinkManager nlm = new NodeLinkManager();

		for (Node node : nm.getNodes(ktreeId)) {
			ProjectNode projectNode = new ProjectNode();
			projectNode.setRefProjectTreeId(ptreeId);
			projectNode.setTitle(node.getTitle());
			projectNode.setWikiTitle(node.getWikiTitle());
			projectNode = pnr.createProjectNode(projectNode);
			linkMapping.put(node.getId(), projectNode.getId());
		}

		for (Subnode subnode : snm.getSubnodesFromTree(ktreeId)) {
			ProjectSubnode projectSubnode = new ProjectSubnode();
			projectSubnode.setTitle(subnode.getTitle());
			projectSubnode.setWikiTitle(subnode.getWikiTitle());
			projectSubnode.setRefProjectNodeId(linkMapping.get(subnode.getKnid()));
			projectSubnode.setPosition(subnode.getPosition());
			psr.createProjectSubnode(projectSubnode);
		}

		for (NodeLink nodelink : nlm.getNodeLinks(ktreeId)) {
			
			ProjectNodeLink projectNodeLink = new ProjectNodeLink();
			projectNodeLink.setSourceId(linkMapping.get(nodelink.getSourceId()));
			projectNodeLink.setTargetId(linkMapping.get(nodelink.getTargetId()));
			projectNodeLink.setRefProjectSubNodeId(nodelink.getKsnid());
			projectNodeLink.setRefProjectTreeId(ptreeId);
			pnlr.createProjectNodeLink(projectNodeLink);
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
