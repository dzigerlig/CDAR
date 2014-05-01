package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	
	public Set<ProjectNode> getProjectNodes(int ptreeId) throws UnknownProjectTreeException {
		Set<ProjectNode> projectnodes = new HashSet<ProjectNode>();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(ptreeId)) {
			projectnodes.add(projectNode);
		} 
		
		return projectnodes;
	}
	
	public ProjectNode getProjectNode(int nodeId) throws UnknownProjectNodeException {
		return pnr.getProjectNode(nodeId);
	}

	public ProjectNode addProjectNode(int projectTreeId, String projectNodeTitle, int pdid) throws Exception {
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTreeId);
		projectNode.setTitle(projectNodeTitle);
		projectNode.setDirectoryId(pdid);
		return pnr.createProjectNode(projectNode);
	}

	public boolean deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		return pnr.deleteProjectNode(projectNodeId);
	}

	public ProjectNode updateProjectNode(ProjectNode node) throws UnknownProjectNodeException {
		ProjectNode projectNode = pnr.getProjectNode(node.getId());
		projectNode.setTreeId(node.getTreeId());
		projectNode.setTitle(node.getTitle());
		projectNode.setStatus(node.getStatus());
		return pnr.updateProjectNode(projectNode);
	}
}
