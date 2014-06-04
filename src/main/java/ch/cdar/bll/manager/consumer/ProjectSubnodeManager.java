package ch.cdar.bll.manager.consumer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.WikiEntry;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.consumer.ProjectNodeRepository;
import ch.cdar.dal.consumer.ProjectSubnodeRepository;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.helpers.PropertyHelper;
import ch.cdar.dal.user.UserRepository;

/**
 * The Class ProjectSubnodeManager.
 */
public class ProjectSubnodeManager {
	
	/** The Project Node Repository. */
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	
	/** The Project Subnode Repository. */
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

	/**
	 * Adds the project subnode.
	 *
	 * @param uid the uid
	 * @param projectSubnode the project subnode
	 * @param content the content
	 * @return the project subnode
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws CreationException the creation exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws InterruptedException 
	 */
	public ProjectSubnode addProjectSubnode(int uid, ProjectSubnode projectSubnode, String content, CountDownLatch subnodeLatch) throws UnknownProjectNodeLinkException,
			UnknownProjectNodeException, CreationException,
			UnknownUserException, EntityException, InterruptedException {
		boolean createSubnode = true;
		if (projectSubnode.getWikititle() != null) {
			createSubnode = false;
		}

		if (projectSubnode.getPosition()==0) {
			projectSubnode.setPosition(getNextSubnodePosition(projectSubnode
				.getNodeId()));
		}
		projectSubnode = psr.createSubnode(projectSubnode);

		if (createSubnode) {
			PropertyHelper propertyHelper = new PropertyHelper();
			if (content==null) {
				content = String.format("== %S ==", propertyHelper.getProperty("SUBNODE_DESCRIPTION"));
			}
			MediaWikiManager mwm = new MediaWikiManager();
			if (subnodeLatch==null) {
				mwm.createWikiEntry(uid, projectSubnode.getWikititle(), content);				
			}
			else{
				mwm.createWikiEntry(uid, projectSubnode.getWikititle(), content, subnodeLatch);				
			}
		}

		return projectSubnode;
	}

	/**
	 * Gets the next subnode position.
	 *
	 * @param nodeId the node id
	 * @return the next subnode position
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	private int getNextSubnodePosition(int nodeId)
			throws UnknownProjectNodeLinkException, EntityException {
		int position = 0;

		for (Subnode subnode : getProjectSubnodesFromProjectNode(nodeId)) {
			if (subnode.getPosition() > position) {
				position = subnode.getPosition();
			}
		}

		return ++position;
	}

	/**
	 * Gets the project subnodes from project tree.
	 *
	 * @param projectTreeId the project tree id
	 * @return the project subnodes from project tree
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public Set<ProjectSubnode> getProjectSubnodesFromProjectTree(
			int projectTreeId) throws UnknownProjectTreeException,
			UnknownProjectNodeLinkException, EntityException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();

		for (ProjectNode projectNode : pnr.getNodes(projectTreeId)) {
			for (ProjectSubnode projectSubnode : psr
					.getSubnodes(projectNode.getId())) {
				projectSubnodes.add(projectSubnode);
			}
		}

		return projectSubnodes;
	}

	/**
	 * Gets the project subnodes from project node.
	 *
	 * @param projectNodeId the project node id
	 * @return the project subnodes from project node
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public Set<ProjectSubnode> getProjectSubnodesFromProjectNode(
			int projectNodeId) throws UnknownProjectNodeLinkException,
			EntityException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();

		for (ProjectSubnode projectSubnode : psr.getSubnodes(projectNodeId)) {
			projectSubnodes.add(projectSubnode);
		}

		return projectSubnodes;
	}

	/**
	 * Gets the project subnode.
	 *
	 * @param projectSubnodeId the project subnode id
	 * @return the project subnode
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public ProjectSubnode getProjectSubnode(int projectSubnodeId)
			throws UnknownProjectSubnodeException, EntityException {
		return psr.getSubnode(projectSubnodeId);
	}

	/**
	 * Update project subnode.
	 *
	 * @param projectSubnode the project subnode
	 * @return the project subnode
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public ProjectSubnode updateProjectSubnode(ProjectSubnode projectSubnode)
			throws UnknownProjectSubnodeException,
			UnknownProjectNodeLinkException, EntityException {
		ProjectSubnode updatedProjectSubnode = psr
				.getSubnode(projectSubnode.getId());
		if (projectSubnode.getNodeId() != 0) {
			updatedProjectSubnode.setNodeId(projectSubnode.getNodeId());
		}
		if (projectSubnode.getTitle() != null) {
			updatedProjectSubnode.setTitle(projectSubnode.getTitle());
		}
		if (projectSubnode.getPosition() != 0) {
			int oldPosition = updatedProjectSubnode.getPosition();
			int newPosition = projectSubnode.getPosition();
			updatedProjectSubnode.setPosition(projectSubnode.getPosition());
			changeOtherProjectSubnodePositions(projectSubnode, oldPosition,
					newPosition);
		}
		if (projectSubnode.getStatus() != 0) {
			updatedProjectSubnode.setStatus(projectSubnode.getStatus());
		}
		return psr.updateSubnode(updatedProjectSubnode);
	}

	/**
	 * Change other project subnode positions.
	 *
	 * @param projectSubnode the project subnode
	 * @param oldPosition the old position
	 * @param newPosition the new position
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	private void changeOtherProjectSubnodePositions(Subnode projectSubnode,
			int oldPosition, int newPosition)
			throws UnknownProjectNodeLinkException, EntityException {
		for (ProjectSubnode otherProjectSubnode : psr
				.getSubnodes(projectSubnode.getNodeId())) {
			if (otherProjectSubnode.getId() != projectSubnode.getId()) {
				if (oldPosition < newPosition) {
					if (otherProjectSubnode.getPosition() > oldPosition
							&& otherProjectSubnode.getPosition() <= newPosition) {
						otherProjectSubnode.setPosition(projectSubnode
								.getPosition() - 1);
						psr.updateSubnode(otherProjectSubnode);
					}
				}

				if (oldPosition > newPosition) {
					if (otherProjectSubnode.getPosition() >= newPosition
							&& otherProjectSubnode.getPosition() < oldPosition) {
						otherProjectSubnode.setPosition(projectSubnode
								.getPosition() + 1);
						psr.updateSubnode(otherProjectSubnode);
					}
				}
			}
		}
	}

	/**
	 * Delete project subnode.
	 *
	 * @param projectSubnodeId the project subnode id
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public void deleteProjectSubnode(int projectSubnodeId)
			throws UnknownProjectSubnodeException,
			UnknownProjectNodeLinkException, EntityException {
		changeSubnodePositionOnDelete(projectSubnodeId);
		psr.deleteSubnode(projectSubnodeId);
	}

	/**
	 * Change subnode position on delete.
	 *
	 * @param subnodeId the subnode id
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 */
	private void changeSubnodePositionOnDelete(int subnodeId)
			throws UnknownProjectNodeLinkException, EntityException,
			UnknownProjectSubnodeException {
		Subnode delSubnode = getProjectSubnode(subnodeId);

		for (ProjectSubnode projectSubnode : getProjectSubnodesFromProjectNode(delSubnode
				.getNodeId())) {
			if (projectSubnode.getPosition() > delSubnode.getPosition()) {
				projectSubnode.setPosition(projectSubnode.getPosition() - 1);
				psr.updateSubnode(projectSubnode);
			}
		}
	}

	/**
	 * Drill up.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public Set<ProjectSubnode> drillUp(int uid, int nodeId)
			throws UnknownProjectNodeLinkException, EntityException,
			UnknownUserException {
		Set<ProjectSubnode> subnodes = new HashSet<ProjectSubnode>();
		for (ProjectSubnode subnode : psr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), subnodes);
	}

	/**
	 * Recursive drill up.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param subnodes the subnodes
	 * @return the sets the
	 * @throws EntityException the entity exception
	 */
	private Set<ProjectSubnode> recursiveDrillUp(int nodeId, int quantity,
			Set<ProjectSubnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectSubnode subnode : psr.getSiblingSubnodes(nodeId)) {
				subnodes.add(subnode);
			}
			for (ProjectSubnode subnode : psr.getParentSubnodes(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillUp(subnode.getNodeId(), quantity - 1,
						subnodes);
			}
		}
		return subnodes;
	}

	/**
	 * Drill down.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	public Set<ProjectSubnode> drillDown(int uid, int treeId, int nodeId)
			throws UnknownProjectNodeLinkException, EntityException,
			UnknownUserException, UnknownNodeException {
		if (nodeId == 0) {
			ProjectNode rootNode = new ProjectNodeRepository().getRoot(treeId);
			if (rootNode == null) {
				return null;
			}
			nodeId = rootNode.getId();
		}
		
		Set<ProjectSubnode> subnodes = new HashSet<ProjectSubnode>();
		for (ProjectSubnode subnode : psr.getSubnodes(nodeId)) {
			subnodes.add(subnode);
		}
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid)
				.getDrillHierarchy(), subnodes);
	}

	/**
	 * Recursive drill down.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param subnodes the subnodes
	 * @return the sets the
	 * @throws EntityException the entity exception
	 */
	private Set<ProjectSubnode> recursiveDrillDown(int nodeId, int quantity,
			Set<ProjectSubnode> subnodes) throws EntityException {
		if (quantity > 0) {
			for (ProjectSubnode subnode : psr.getFollowerSubnodes(nodeId)) {
				subnodes.add(subnode);
				subnodes = recursiveDrillDown(subnode.getNodeId(),
						quantity - 1, subnodes);
			}
		}
		return subnodes;
	}

	/**
	 * Rename subnode.
	 *
	 * @param projectSubnode the project subnode
	 * @return the project subnode
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public ProjectSubnode renameSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException, UnknownProjectSubnodeException, EntityException {
		ProjectSubnode renamedProjectSubnode = psr.getSubnode(projectSubnode.getId());
		renamedProjectSubnode.setTitle(projectSubnode.getTitle());
		return psr.updateSubnode(renamedProjectSubnode);
	}
	
	/**
	 * Copy subnodes.
	 *
	 * @param uid the uid
	 * @param projectNodeId the project node id
	 * @param newNodeId the new node id
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws CreationException the creation exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws InterruptedException 
	 */
	public void copySubnodes(int uid, int projectNodeId, int newNodeId) throws UnknownProjectNodeException, EntityException, UnknownProjectNodeLinkException, CreationException, UnknownUserException, UnknownProjectSubnodeException, InterruptedException {
		MediaWikiManager mwm = new MediaWikiManager();
		Set <ProjectSubnode> subnodeList = getProjectSubnodesFromProjectNode(projectNodeId);
		CountDownLatch subnodeLatch = new CountDownLatch(subnodeList.size());
		for (ProjectSubnode subnode : subnodeList) {
			WikiEntry swe = mwm.getKnowledgeProjectSubnodeWikiEntry(subnode.getId());
			subnode.setWikititle(null);
			subnode.setNodeId(newNodeId);
			addProjectSubnode(uid, subnode, swe.getWikiContentPlain(),subnodeLatch);
		}
		subnodeLatch.await();
	}
}
