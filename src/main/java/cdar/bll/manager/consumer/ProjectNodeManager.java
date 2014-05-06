package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	
	public Set<ProjectNode> getProjectNodes(int ptreeId) throws UnknownProjectTreeException, EntityException {
		Set<ProjectNode> projectNodes = new HashSet<ProjectNode>();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(ptreeId)) {
			projectNodes.add(projectNode);
		} 
		
		return projectNodes;
	}
	
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException, EntityException {
		return pnr.getProjectNode(projectNodeId);
	}

	public ProjectNode addProjectNode(ProjectNode projectNode) throws UnknownProjectTreeException {
		return pnr.createProjectNode(projectNode);
	}

	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		pnr.deleteProjectNode(projectNodeId);
	}

	public ProjectNode updateProjectNode(ProjectNode projectNode) throws UnknownProjectNodeException, EntityException {
		ProjectNode updatedProjectNode = pnr.getProjectNode(projectNode.getId());
		
		if (projectNode.getTreeId()!=0) {
			updatedProjectNode.setTreeId(projectNode.getTreeId());
		}
		
		if (projectNode.getTitle()!=null) {
			updatedProjectNode.setTitle(projectNode.getTitle());
		}
		
		if (projectNode.getStatus()!=0) {
			updatedProjectNode.setStatus(projectNode.getStatus());
		}
		
		return pnr.updateProjectNode(updatedProjectNode);
	}

	public Object zoomUp(int nodeId) {
		//TODO
		return null;
	}

	public Object zoomDown(int nodeId) {
		//TODO
		return null;
	}

	public ProjectNode renameNode(ProjectNode projectNode) throws UnknownProjectNodeException, EntityException {
		ProjectNode updatedProjectNode = pnr.getProjectNode(projectNode.getId());
		updatedProjectNode.setTitle(projectNode.getTitle());
		return pnr.updateProjectNode(updatedProjectNode);
	}
}
