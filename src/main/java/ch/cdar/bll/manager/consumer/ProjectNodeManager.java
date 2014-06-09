package ch.cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.helpers.SubnodeCopyHelper;
import ch.cdar.bll.manager.producer.TemplateManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.helpers.PropertyHelper;
import ch.cdar.dal.repository.consumer.ProjectDirectoryRepository;
import ch.cdar.dal.repository.consumer.ProjectNodeRepository;
import ch.cdar.dal.repository.user.UserRepository;

/**
 * The Class ProjectNodeManager.
 */
public class ProjectNodeManager {
	
	/** The Project Node Repository. */
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	
	/**
	 * Gets the project nodes.
	 *
	 * @param ptreeId the ptree id
	 * @return the project nodes
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Set<ProjectNode> getProjectNodes(int ptreeId) throws UnknownProjectTreeException, EntityException {
		Set<ProjectNode> projectNodes = new HashSet<ProjectNode>();
		
		for (ProjectNode projectNode : pnr.getNodes(ptreeId)) {
			projectNodes.add(projectNode);
		} 
		
		return projectNodes;
	}
	
	/**
	 * Gets the project node.
	 *
	 * @param projectNodeId the project node id
	 * @return the project node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 */
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException, EntityException {
		return pnr.getNode(projectNodeId);
	}

	/**
	 * Adds the project node.
	 *
	 * @param uid the uid
	 * @param projectNode the project node
	 * @param content the content
	 * @return the project node
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws CreationException the creation exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public ProjectNode addProjectNode(int uid, ProjectNode projectNode, String content) throws UnknownProjectTreeException, CreationException, EntityException, UnknownUserException, UnknownTreeException {
		boolean createNode = true;
		if(projectNode.getWikititle()!=null) {
			createNode = false;
		}
		
		if (projectNode.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			projectNode.setTitle(String.format("new %s",
					propertyHelper.getProperty("CDAR_NODE_DESCRIPTION")));
		}
		
		if (projectNode.getDirectoryId() == 0) {
			ProjectDirectoryRepository dr = new ProjectDirectoryRepository();
			int rootDirectoryId = dr.getDirectories(projectNode.getTreeId()).get(0).getId();
			projectNode.setDirectoryId(rootDirectoryId);
		}
		
		projectNode = pnr.createNode(projectNode);
		
		if (createNode) {
			PropertyHelper propertyHelper = new PropertyHelper();
			if (content == null) {
				content = String.format("== %S ==", propertyHelper.getProperty("CDAR_NODE_DESCRIPTION"));
			}
			MediaWikiManager mwm = new MediaWikiManager();
			mwm.createWikiEntry(uid, projectNode.getWikititle(), content);
		}
		return projectNode;
	}

	/**
	 * Delete project node.
	 *
	 * @param projectNodeId the project node id
	 * @throws UnknownProjectNodeException the unknown project node exception
	 */
	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		pnr.deleteNode(projectNodeId);
	}

	/**
	 * Update project node.
	 *
	 * @param userId the user id
	 * @param projectNode the project node
	 * @return the project node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownNodeException the unknown node exception
	 */
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


	/**
	 * Drill up.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public Set<ProjectNode> drillUp(int uid, int nodeId) throws UnknownProjectNodeException, EntityException, UnknownUserException {
		Set<ProjectNode> nodes = new HashSet<ProjectNode>();
		nodes.add(pnr.getNode(nodeId));
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), nodes);
	}
	
	/**
	 * Recursive drill up.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param nodes the nodes
	 * @return the sets the
	 * @throws EntityException the entity exception
	 */
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
	
	/**
	 * Drill down.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownNodeException the unknown node exception
	 */
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

	/**
	 * Recursive drill down.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param nodes the nodes
	 * @return the sets the
	 * @throws EntityException the entity exception
	 */
	private Set<ProjectNode> recursiveDrillDown(int nodeId, int quantity, Set<ProjectNode> nodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectNode node : pnr.getFollowerNode(nodeId)) {
				nodes.add(node);
				nodes = recursiveDrillDown(node.getId(), quantity - 1, nodes);
			}
		}
		return nodes;
	}

	/**
	 * Rename node.
	 *
	 * @param projectNode the project node
	 * @return the project node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	public ProjectNode renameNode(ProjectNode projectNode) throws UnknownProjectNodeException, EntityException, UnknownNodeException {
		ProjectNode updatedProjectNode = pnr.getNode(projectNode.getId());
		updatedProjectNode.setTitle(projectNode.getTitle());
		return pnr.updateNode(updatedProjectNode);
	}

	/**
	 * Gets the root.
	 *
	 * @param treeId the tree id
	 * @return the root
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	private ProjectNode getRoot(int treeId) throws UnknownNodeException, EntityException {
		return pnr.getRoot(treeId);
	}

	/**
	 * Copy node.
	 *
	 * @param uid the uid
	 * @param projectNode the project node
	 * @return the project node
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws CreationException the creation exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws InterruptedException 
	 */
	public ProjectNode copyNode(int uid, ProjectNode projectNode) throws UnknownProjectTreeException, CreationException, UnknownProjectNodeLinkException, EntityException, UnknownProjectNodeException, UnknownUserException, UnknownNodeException, UnknownProjectSubnodeException, UnknownTreeException, InterruptedException {
		int projectNodeId = projectNode.getId();
		MediaWikiManager mwm = new MediaWikiManager();
		projectNode = getProjectNode(projectNodeId);
		projectNode.setWikititle(null);
		projectNode.setDynamicTreeFlag(0);
		projectNode.setStatus(0);
		WikiEntry wikiEntry = mwm.getProjectNodeWikiEntry(projectNode.getId());
		ProjectNode newNode = addProjectNode(uid, projectNode, wikiEntry.getWikiContentPlain());
		SubnodeCopyHelper subnodeCopyHelper = new SubnodeCopyHelper(uid, projectNodeId, newNode.getId(), UserRole.CONSUMER);
		subnodeCopyHelper.start();
		return newNode;
	}
}
