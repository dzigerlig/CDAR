package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.PropertyHelper;
import cdar.bll.entity.Node;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.manager.producer.TemplateManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.consumer.ProjectDirectoryRepository;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.user.UserRepository;

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

	public ProjectNode addProjectNode(int uid, ProjectNode projectNode) throws UnknownProjectTreeException, CreationException, EntityException, UnknownUserException, UnknownTreeException {
		boolean createSubnode = true;
		if(projectNode.getWikititle()!=null) {
			createSubnode = false;
		}
		
		if (projectNode.getDirectoryId() == 0) {
			ProjectDirectoryRepository dr = new ProjectDirectoryRepository();
			int rootDirectoryId = dr.getDirectories(projectNode.getTreeId()).get(0).getId();
			projectNode.setDirectoryId(rootDirectoryId);
		}
		
		projectNode = pnr.createProjectNode(projectNode);
		
		if (createSubnode) {
			TemplateManager tm = new TemplateManager();
			String templateContent = tm.getDefaultKnowledgeTemplateText(projectNode.getTreeId());
			
			if (templateContent == null) {
				PropertyHelper propertyHelper = new PropertyHelper();
				templateContent = String.format("== %S ==", propertyHelper.getProperty("NODE_DESCRIPTION"));
			}
			
			MediaWikiManager mwm = new MediaWikiManager();
			mwm.createWikiEntry(uid, projectNode.getWikititle(), templateContent);
		}
		return projectNode;
	}

	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		pnr.deleteProjectNode(projectNodeId);
	}

	public ProjectNode updateProjectNode(int userId, ProjectNode projectNode) throws UnknownProjectNodeException, EntityException, UnknownUserException, UnknownTreeException, UnknownNodeException {
		ProjectNode updatedProjectNode = pnr.getProjectNode(projectNode.getId());
		
		if (projectNode.getTreeId()!=0) {
			updatedProjectNode.setTreeId(projectNode.getTreeId());
		}
		
		if (projectNode.getTitle()!=null) {
			updatedProjectNode.setTitle(projectNode.getTitle());
		}
		
		if (projectNode.getDirectoryId()!=0) {
			updatedProjectNode.setDirectoryId(projectNode.getDirectoryId());
		}
		
		updatedProjectNode.setDynamicTreeFlag(projectNode.getDynamicTreeFlag());
		
		
		if (projectNode.getStatus()!=0) {
			updatedProjectNode.setStatus(projectNode.getStatus());
			if (!projectNode.getWikititle().contains("PROJECTNODE_")) {
				MediaWikiManager mwm = new MediaWikiManager();
				TemplateManager tm = new TemplateManager();
				String content = String.format("%s\n\n%s", mwm.getProjectNodeWikiEntry(projectNode.getId()).getWikiContentPlain(), tm.getDefaultProjectTemplateText(projectNode.getInheritedTreeId()));
				updatedProjectNode.setWikititle(String.format("PROJECTNODE_%d", projectNode.getId()));
				mwm.createWikiEntry(userId, updatedProjectNode.getWikititle(), content);
			}
		}
		
		return pnr.updateProjectNode(updatedProjectNode);
	}


	public Set<ProjectNode> drillUp(int uid, int nodeId) throws UnknownProjectNodeException, EntityException, UnknownUserException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getProjectNode(nodeId));
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), nodes);
	}
	private Set<ProjectNode> recursiveDrillUp(int nodeId, int quantity, Set<ProjectNode> nodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectNode node : pnr.getSiblingNode(nodeId)) {
				nodes.add(node);
			}
			for (ProjectNode node : pnr.getParentNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveDrillUp(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}
	public Set<ProjectNode> drillDown(int uid, int nodeId) throws EntityException, UnknownProjectNodeException, UnknownUserException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getProjectNode(nodeId));
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), nodes);
	}

	private Set<ProjectNode> recursiveDrillDown(int nodeId, int quantity, Set<ProjectNode> nodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectNode node : pnr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveDrillDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	public ProjectNode renameNode(ProjectNode projectNode) throws UnknownProjectNodeException, EntityException, UnknownNodeException {
		ProjectNode updatedProjectNode = pnr.getProjectNode(projectNode.getId());
		updatedProjectNode.setTitle(projectNode.getTitle());
		return pnr.updateProjectNode(updatedProjectNode);
	}

	public ProjectNode getRoot(int treeId) throws UnknownNodeException, EntityException {
		return pnr.getRoot(treeId);
	}
}
