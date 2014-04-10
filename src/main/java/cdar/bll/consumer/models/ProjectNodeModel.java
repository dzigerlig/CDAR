package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNode;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;

public class ProjectNodeModel {
	private ConsumerDaoController cdc = new ConsumerDaoController();
	
	public Set<ProjectNode> getProjectNodes(int ptreeid) {
		Set<ProjectNode> projectnodes = new HashSet<ProjectNode>();
		
		for (ProjectNodeDao pnd : cdc.getProjectNodes(ptreeid)) {
			projectnodes.add(new ProjectNode(pnd));
		}
		
		return projectnodes;
	}
	
	public ProjectNode getProjectNode(int nodeid) {
		return new ProjectNode(cdc.getProjectNode(nodeid));
	}
}
