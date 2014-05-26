package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

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
import cdar.dal.helpers.PropertyHelper;
import cdar.dal.user.UserRepository;

import cdar.pl.controller.StatusHelper;

public class ProjectNodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	
	public Set<ProjectNode> getProjectNodes(int ptreeId) throws UnknownProjectTreeException, EntityException {
		Set<ProjectNode> projectNodes = new HashSet<ProjectNode>();
		
		for (ProjectNode projectNode : pnr.getNodes(ptreeId)) {
			projectNodes.add(projectNode);
		} 
		
		return projectNodes;
	}
	
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException, EntityException {
		return pnr.getNode(projectNodeId);
	}

	public ProjectNode addProjectNode(int uid, ProjectNode projectNode) throws UnknownProjectTreeException, CreationException, EntityException, UnknownUserException, UnknownTreeException {
		boolean createSubnode = true;
		if(projectNode.getWikititle()!=null) {
			createSubnode = false;
		}
		
		if (projectNode.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			projectNode.setTitle(String.format("new %s",
					propertyHelper.getProperty("NODE_DESCRIPTION")));
		}
		
		if (projectNode.getDirectoryId() == 0) {
			ProjectDirectoryRepository dr = new ProjectDirectoryRepository();
			int rootDirectoryId = dr.getDirectories(projectNode.getTreeId()).get(0).getId();
			projectNode.setDirectoryId(rootDirectoryId);
		}
		
		projectNode = pnr.createNode(projectNode);
		
		if (createSubnode) {
			PropertyHelper propertyHelper = new PropertyHelper();
			String content = String.format("== %S ==", propertyHelper.getProperty("NODE_DESCRIPTION"));
			MediaWikiManager mwm = new MediaWikiManager();
			mwm.createWikiEntry(uid, projectNode.getWikititle(), content);
		}
		return projectNode;
	}

	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		pnr.deleteNode(projectNodeId);
	}

	public ProjectNode updateProjectNode(int userId, ProjectNode projectNode) throws UnknownProjectNodeException, EntityException, UnknownUserException, UnknownTreeException, UnknownNodeException {
		ProjectNode updatedProjectNode = pnr.getNode(projectNode.getId());
		
		if (projectNode.getTreeId()!=0) {
			updatedProjectNode.setTreeId(projectNode.getTreeId());
		}
		
		if (projectNode.getTitle()!=null) {
			updatedProjectNode.setTitle(projectNode.getTitle());
		}
		
		if (projectNode.getDirectoryId()!=0) {
			updatedProjectNode.setDirectoryId(projectNode.getDirectoryId());
		}
		
		if (projectNode.getWikititle()!=null) {
			updatedProjectNode.setWikititle(projectNode.getWikititle());
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
		
		return pnr.updateNode(updatedProjectNode);
	}


	public Set<ProjectNode> drillUp(int uid, int nodeId) throws UnknownProjectNodeException, EntityException, UnknownUserException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getNode(nodeId));
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
	public Set<ProjectNode> drillDown(int uid, int treeId, int nodeId) throws EntityException, UnknownProjectNodeException, UnknownUserException, UnknownNodeException {
		if (nodeId == 0) {
			ProjectNode rootNode = getRoot(treeId);
			if (rootNode == null) {
				return null;
			}
			nodeId = rootNode.getId();
		}
		
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getNode(nodeId));
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
		ProjectNode updatedProjectNode = pnr.getNode(projectNode.getId());
		updatedProjectNode.setTitle(projectNode.getTitle());
		return pnr.updateNode(updatedProjectNode);
	}

	private ProjectNode getRoot(int treeId) throws UnknownNodeException, EntityException {
		return pnr.getRoot(treeId);
	}
}
