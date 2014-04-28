package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectSubnode;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeRepository;

public class ProjectSubnodeModel {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();
	
	public ProjectSubnode addProjectSubnode(int kpnid, String title) throws UnknownProjectNodeLinkException, UnknownProjectNodeException {
		ProjectSubnode projectSubnode = new ProjectSubnode();
		projectSubnode.setRefProjectNodeId(kpnid);
		projectSubnode.setPosition(psr.getNextProjectSubnodePosition(kpnid));
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
		return psr.updateProjectSubnode(updatedProjectSubnode);
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
