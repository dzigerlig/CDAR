package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Subnode;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.helpers.PropertyHelper;
import cdar.dal.user.UserRepository;

public class ProjectSubnodeManager {
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

	public ProjectSubnode addProjectSubnode(int uid, ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException,
			UnknownProjectNodeException, CreationException,
			UnknownUserException, EntityException {
		boolean createSubnode = true;
		if (projectSubnode.getWikititle() != null) {
			createSubnode = false;
		}

		projectSubnode.setPosition(getNextSubnodePosition(projectSubnode
				.getNodeId()));
		projectSubnode = psr.createSubnode(projectSubnode);

		if (createSubnode) {
			PropertyHelper propertyHelper = new PropertyHelper();
			String content = String.format("== %S ==", propertyHelper.getProperty("SUBNODE_DESCRIPTION"));

			MediaWikiManager mwm = new MediaWikiManager();
			mwm.createWikiEntry(uid, projectSubnode.getWikititle(), content);
		}

		return projectSubnode;
	}

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

	public Set<ProjectSubnode> getProjectSubnodesFromProjectNode(
			int projectNodeId) throws UnknownProjectNodeLinkException,
			EntityException {
		Set<ProjectSubnode> projectSubnodes = new HashSet<ProjectSubnode>();

		for (ProjectSubnode projectSubnode : psr
				.getSubnodes(projectNodeId)) {
			projectSubnodes.add(projectSubnode);
		}

		return projectSubnodes;
	}

	public ProjectSubnode getProjectSubnode(int projectSubnodeId)
			throws UnknownProjectSubnodeException, EntityException {
		return psr.getSubnode(projectSubnodeId);
	}

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

	public void deleteProjectSubnode(int projectSubnodeId)
			throws UnknownProjectSubnodeException,
			UnknownProjectNodeLinkException, EntityException {
		changeSubnodePositionOnDelete(projectSubnodeId);
		psr.deleteSubnode(projectSubnodeId);
	}

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

	public ProjectSubnode renameSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException, UnknownProjectSubnodeException, EntityException {
		ProjectSubnode renamedProjectSubnode = psr.getSubnode(projectSubnode.getId());
		renamedProjectSubnode.setTitle(projectSubnode.getTitle());
		return psr.updateSubnode(renamedProjectSubnode);
	}
}
