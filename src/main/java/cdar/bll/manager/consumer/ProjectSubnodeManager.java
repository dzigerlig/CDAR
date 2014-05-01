package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectSubnodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();
	
	public ProjectSubnode addProjectSubnode(int kpnid, String title) throws UnknownProjectNodeLinkException, UnknownProjectNodeException {
		ProjectSubnode projectSubnode = new ProjectSubnode();
		projectSubnode.setRefProjectNodeId(kpnid);
		projectSubnode.setPosition(getNextProjectSubnodePosition(kpnid));
		projectSubnode.setTitle(title);
		return psr.createProjectSubnode(projectSubnode);
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectTree(int projectTreeId) throws UnknownProjectTreeException, UnknownProjectNodeLinkException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectNode projectNode : pnr.getProjectNodes(projectTreeId)) {
			for (ProjectSubnode projectSubnode : psr.getProjectSubnodes(projectNode.getId())) {
				projectSubnodes.add(projectSubnode);
			}
		}
		
		return projectSubnodes;
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectNode(int projectNodeId) throws UnknownProjectNodeLinkException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectSubnode projectSubnode : psr.getProjectSubnodes(projectNodeId)) {
			projectSubnodes.add(projectSubnode);
		}
		
		return projectSubnodes;
	}
	
	public ProjectSubnode getProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		return psr.getProjectSubnode(projectSubnodeId);
	}
	
	public ProjectSubnode updateProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectSubnodeException, UnknownProjectNodeLinkException {
		ProjectSubnode updatedProjectSubnode = psr.getProjectSubnode(projectSubnode.getId());
		updatedProjectSubnode.setRefProjectNodeId(projectSubnode.getRefProjectNodeId());
		updatedProjectSubnode.setTitle(projectSubnode.getTitle());
		updatedProjectSubnode.setPosition(projectSubnode.getPosition());
		updatedProjectSubnode.setStatus(projectSubnode.getStatus());
		return psr.updateProjectSubnode(updatedProjectSubnode);
	}
	
	public int getNextProjectSubnodePosition(int projectNodeId) throws UnknownProjectNodeLinkException {
		int position = 0;
		
		for (ProjectSubnode projectSubnode : getProjectSubnodesFromProjectNode(projectNodeId)) {
			if (projectSubnode.getPosition() > position) {
				position = projectSubnode.getPosition();
			}
		}
		
		return ++position;
	}
	
	public void changeProjectSubnodePosition(int projectNodeId, int id, int position) throws UnknownProjectNodeLinkException {
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
	
	public boolean removeProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		return psr.deleteProjectSubnode(projectSubnodeId);
	}
}