package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
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

	public ProjectNode addProjectNode(ProjectNode projectNode) throws UnknownProjectTreeException, CreationException {
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

	public Set<ProjectNode> zoomUp(int nodeId) throws UnknownProjectNodeException, EntityException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getProjectNode(nodeId));
		return recursiveZoomUp(nodeId, 2, nodes);
	}
	private Set<ProjectNode> recursiveZoomUp(int nodeId, int quantity, Set<ProjectNode> nodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectNode node : pnr.getSiblingNode(nodeId)) {
				nodes.add(node);
			}
			for (ProjectNode node : pnr.getParentNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveZoomUp(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}
	public Set<ProjectNode> zoomDown(int nodeId) throws EntityException, UnknownProjectNodeException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getProjectNode(nodeId));
		return recursiveZoomDown(nodeId, 2, nodes);
	}

	private Set<ProjectNode> recursiveZoomDown(int nodeId, int quantity, Set<ProjectNode> nodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectNode node : pnr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveZoomDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	public ProjectNode renameNode(ProjectNode projectNode) throws UnknownProjectNodeException, EntityException {
		ProjectNode updatedProjectNode = pnr.getProjectNode(projectNode.getId());
		updatedProjectNode.setTitle(projectNode.getTitle());
		return pnr.updateProjectNode(updatedProjectNode);
	}
}
