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
		Set<ProjectNode> projectNodes = new HashSet<ProjectNode>();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(ptreeId)) {
			projectNodes.add(projectNode);
		} 
		
		return projectNodes;
	}
	
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		return pnr.getProjectNode(projectNodeId);
	}

	public ProjectNode addProjectNode(ProjectNode projectNode) throws Exception {
		return pnr.createProjectNode(projectNode);
	}

	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		pnr.deleteProjectNode(projectNodeId);
	}

	public ProjectNode updateProjectNode(ProjectNode node) throws UnknownProjectNodeException {
		ProjectNode projectNode = pnr.getProjectNode(node.getId());
		projectNode.setTreeId(node.getTreeId());
		projectNode.setTitle(node.getTitle());
		projectNode.setStatus(node.getStatus());
		return pnr.updateProjectNode(projectNode);
	}

	public Object zoomUp(int nodeId) {
		//TODO
		return null;
	}

	public Object zoomDown(int nodeId) {
		//TODO
		return null;
	}
}
