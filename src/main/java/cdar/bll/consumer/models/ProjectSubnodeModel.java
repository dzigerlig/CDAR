package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectSubnode;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeDao;

public class ProjectSubnodeModel {
	private ConsumerDaoController cdc = new ConsumerDaoController();
	
	public ProjectSubnode addProjectSubnode(int kpnid, String title) {
		ProjectSubnodeDao projectSubnodeDao = new ProjectSubnodeDao(kpnid, cdc.getNextProjectSubnodePosition(kpnid), title);
		return new ProjectSubnode(projectSubnodeDao.create());
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectTree(int projectTreeId) {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectNodeDao node : cdc.getProjectNodes(projectTreeId)) {
			for (ProjectSubnodeDao subnode : cdc.getProjectSubnodes(node.getId())) {
				projectSubnodes.add(new ProjectSubnode(subnode));
			}
		}
		
		return projectSubnodes;
	}
	
	public Set<ProjectSubnode> getProjectSubnodesFromProjectNode(int projectNodeId) {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();
		
		for (ProjectSubnodeDao subnode : cdc.getProjectSubnodes(projectNodeId)) {
			projectSubnodes.add(new ProjectSubnode(subnode));
		}
		
		return projectSubnodes;
	}
	
	public ProjectSubnode getProjectSubnode(int projectSubnodeId) {
		return new ProjectSubnode(cdc.getProjectSubnode(projectSubnodeId));
	}
	
	public ProjectSubnode updateProjectSubnode(ProjectSubnode psn) {
		ProjectSubnodeDao projectSubnodeDao = cdc.getProjectSubnode(psn.getId());
		projectSubnodeDao.setKpnid(psn.getRefProjectNodeId());
		projectSubnodeDao.setTitle(psn.getTitle());
		return new ProjectSubnode(projectSubnodeDao.update());
	}
	
	public boolean removeProjectSubnode(int id) {
		return cdc.getProjectSubnode(id).delete();
	}
}
