package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Subnode;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;

public class ProjectSubnodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();
	
	public ProjectSubnode addProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException, UnknownProjectNodeException, CreationException {
		return psr.createProjectSubnode(projectSubnode);
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectTree(int projectTreeId) throws UnknownProjectTreeException, UnknownProjectNodeLinkException, EntityException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(projectTreeId)) {
			for (ProjectSubnode projectSubnode : psr.getProjectSubnodes(projectNode.getId())) {
				projectSubnodes.add(projectSubnode);
			}
		}
		
		return projectSubnodes;
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectNode(int projectNodeId) throws UnknownProjectNodeLinkException, EntityException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectSubnode projectSubnode : psr.getProjectSubnodes(projectNodeId)) {
			projectSubnodes.add(projectSubnode);
		}
		
		return projectSubnodes;
	}
	
	public ProjectSubnode getProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException, EntityException {
		return psr.getProjectSubnode(projectSubnodeId);
	}
	
	public ProjectSubnode updateProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectSubnodeException, UnknownProjectNodeLinkException, EntityException {
		ProjectSubnode updatedProjectSubnode = psr.getProjectSubnode(projectSubnode.getId());
		if (projectSubnode.getNodeId()!=0) {
			updatedProjectSubnode.setNodeId(projectSubnode.getNodeId());
		}
		if (projectSubnode.getTitle()!=null) {
			updatedProjectSubnode.setTitle(projectSubnode.getTitle());
		}
		if (projectSubnode.getPosition()!=0) {
			int oldPosition = updatedProjectSubnode.getPosition();
			int newPosition = projectSubnode.getPosition();
			updatedProjectSubnode.setPosition(projectSubnode.getPosition());
			
			changeOtherProjectSubnodePositions(projectSubnode, oldPosition, newPosition);
		}
		if (projectSubnode.getStatus()!=0) {
			updatedProjectSubnode.setStatus(projectSubnode.getStatus());
		}
		return psr.updateProjectSubnode(updatedProjectSubnode);
	}
	
	private void changeOtherProjectSubnodePositions(Subnode projectSubnode, int oldPosition,
			int newPosition) throws UnknownProjectNodeLinkException, EntityException {
		for (ProjectSubnode otherProjectSubnode : psr.getProjectSubnodes(projectSubnode.getNodeId())) {
			if (otherProjectSubnode.getId()!=projectSubnode.getId()) {
				
				if (oldPosition < newPosition) {
					if (otherProjectSubnode.getPosition() > oldPosition
							&& otherProjectSubnode.getPosition() <= newPosition) {
						otherProjectSubnode.setPosition(projectSubnode.getPosition() - 1);
						psr.updateProjectSubnode(otherProjectSubnode);
					}
				}

				if (oldPosition > newPosition) {
					if (otherProjectSubnode.getPosition() >= newPosition
							&& otherProjectSubnode.getPosition() < oldPosition) {
						otherProjectSubnode.setPosition(projectSubnode.getPosition() + 1);
						psr.updateProjectSubnode(otherProjectSubnode);
					}
				}
			}
		}
	}
	
	public int getNextProjectSubnodePosition(int projectNodeId) throws UnknownProjectNodeLinkException, EntityException {
		int position = 0;
		
		for (ProjectSubnode projectSubnode : getProjectSubnodesFromProjectNode(projectNodeId)) {
			if (projectSubnode.getPosition() > position) {
				position = projectSubnode.getPosition();
			}
		}
		
		return ++position;
	}
	
	public void changeProjectSubnodePosition(int projectNodeId, int id, int position) throws UnknownProjectNodeLinkException, EntityException {
		for (ProjectSubnode projectSubnode : psr.getProjectSubnodes(projectNodeId)) {
			if (projectSubnode.getId()==id) {
				projectSubnode.setPosition(position);
				psr.updateProjectSubnode(projectSubnode);
			} else {
				if (projectSubnode.getPosition()>=position) {
					projectSubnode.setPosition(projectSubnode.getPosition()+1);
					psr.updateProjectSubnode(projectSubnode);
				}
			}
		}
	}
	
	public void deleteProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		psr.deleteProjectSubnode(projectSubnodeId);
	}

	public Set<ProjectSubnode> zoomUp(int nodeId) throws UnknownProjectNodeLinkException, EntityException {
		Set<ProjectSubnode> subnodes = new HashSet<ProjectSubnode>();
		for (ProjectSubnode subnode : psr.getProjectSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveZoomUp(nodeId, 2, subnodes);
	}

	private Set<ProjectSubnode> recursiveZoomUp(int nodeId, int quantity,
			Set<ProjectSubnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectSubnode subnode : psr.getSiblingSubnode(nodeId)) {
				subnodes.add(subnode);
			}
			for (ProjectSubnode subnode : psr.getParentSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveZoomUp(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	public Set<ProjectSubnode> zoomDown(int nodeId) throws UnknownProjectNodeLinkException, EntityException  {
		Set<ProjectSubnode> subnodes = new HashSet<ProjectSubnode>();
		for (ProjectSubnode subnode : psr.getProjectSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveZoomDown(nodeId, 2, subnodes);
	}

	private Set<ProjectSubnode> recursiveZoomDown(int nodeId, int quantity,
			Set<ProjectSubnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectSubnode subnode : psr.getFollowerSubnode(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveZoomDown(subnode.getNodeId(), quantity - 1, subnodes);
			}
		}
		return subnodes;
	}
}
