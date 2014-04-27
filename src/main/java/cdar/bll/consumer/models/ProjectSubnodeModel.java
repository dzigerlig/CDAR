package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectSubnode;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeDao;

public class ProjectSubnodeModel {
	private ConsumerDaoRepository cdc = new ConsumerDaoRepository();
	
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
	
	public void changeProjectSubnodePosition(int nodeid, int id, int position) {
		for (ProjectSubnodeDao subnode : cdc.getProjectSubnodes(nodeid)) {
			if (subnode.getId()==id) {
				subnode.setPosition(position);
				subnode.update();
			} else {
				if (subnode.getPosition()>=position) {
					subnode.setPosition(subnode.getPosition()+1);
					subnode.update();
				}
			}
		}
	}
	
	public boolean removeProjectSubnode(int id) {
		return cdc.getProjectSubnode(id).delete();
	}
}
