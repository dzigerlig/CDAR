package ch.cdar.bll.manager.consumer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.dal.consumer.ProjectNodeLinkRepository;
import ch.cdar.dal.consumer.ProjectNodeRepository;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.user.UserRepository;
import ch.cdar.pl.controller.StatusHelper;

/**
 * The Class ProjectNodeLinkManager.
 */
public class ProjectNodeLinkManager {
	
	/** The Project Node Link Repository. */
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

	/**
	 * Gets the project node links.
	 *
	 * @param projectTreeId the project tree id
	 * @return the project node links
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Set<NodeLink> getProjectNodeLinks(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		Set<NodeLink> projectNodeLinks = new HashSet<NodeLink>();
		
		for (NodeLink projectNodeLink : pnlr.getNodeLinks(projectTreeId)) {
			projectNodeLinks.add(projectNodeLink);
		}
		
		return projectNodeLinks;
	}

	/**
	 * Adds the project node link.
	 *
	 * @param projectNodeLink the project node link
	 * @return the node link
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public NodeLink addProjectNodeLink(NodeLink projectNodeLink) throws UnknownProjectTreeException {
		return pnlr.createNodeLink(projectNodeLink);
	}
	
	/**
	 * Update link.
	 *
	 * @param nodeLink the node link
	 * @return the node link
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 */
	public NodeLink updateLink(NodeLink nodeLink) throws UnknownProjectNodeLinkException, EntityException, UnknownNodeLinkException {
		NodeLink updatedProjectNodeLink = pnlr.getNodeLink(nodeLink.getId());
		if (nodeLink.getTreeId()!=0) {
			updatedProjectNodeLink.setTreeId(nodeLink.getTreeId());
		}
		if (nodeLink.getSubnodeId()!=0) {
			updatedProjectNodeLink.setSubnodeId(nodeLink.getSubnodeId());
		}
		if (nodeLink.getSourceId()!=0) {
			updatedProjectNodeLink.setSourceId(nodeLink.getSourceId());
		}
		if (nodeLink.getTargetId()!=0) {
			updatedProjectNodeLink.setTargetId(nodeLink.getTargetId());
		}
		return pnlr.updateNodeLink(updatedProjectNodeLink);
	}
	
	/**
	 * Gets the project node link.
	 *
	 * @param projectNodeLinkId the project node link id
	 * @return the project node link
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 */
	public NodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException, EntityException, UnknownNodeLinkException {
		return pnlr.getNodeLink(projectNodeLinkId);
	}
	
	/**
	 * Delete project node link.
	 *
	 * @param projectNodeLinkId the project node link id
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 */
	public void deleteProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		pnlr.deleteNodeLink(projectNodeLinkId);
	}

	/**
	 * Drill up.
	 *
	 * @param uid the uid
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public Set<NodeLink> drillUp(int uid, int nodeId) throws EntityException, UnknownNodeLinkException, UnknownTreeException, UnknownUserException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}
	
	/**
	 * Drill down.
	 *
	 * @param uid the uid
	 * @param treeId the tree id
	 * @param nodeId the node id
	 * @return the sets the
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public Set<NodeLink> drillDown(int uid, int treeId, int nodeId) throws UnknownNodeException, EntityException, UnknownUserException  {
		if (nodeId == 0) {
			ProjectNode rootNode = new ProjectNodeRepository().getRoot(treeId);
			if (rootNode == null) {
				return null;
			}
			nodeId = rootNode.getId();
		}
		
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveDrillDown(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}

	/**
	 * Recursive drill up.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param links the links
	 * @return the sets the
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	private Set<NodeLink> recursiveDrillUp(int nodeId, int quantity, Set<NodeLink> links) throws EntityException, UnknownNodeLinkException, UnknownTreeException {
		if (quantity > 0) {
			for (NodeLink nodeLink : pnlr.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : pnlr.getParentNodeLinks(nodeId)) {
				links.add(nodeLink);
				links=recursiveDrillUp(nodeLink.getSourceId(), quantity-1, links);
			}
		}
		return links;
	}
	
	/**
	 * Recursive drill down.
	 *
	 * @param nodeId the node id
	 * @param quantity the quantity
	 * @param links the links
	 * @return the sets the
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	private Set<NodeLink> recursiveDrillDown(int nodeId, int quantity, Set<NodeLink> links) throws UnknownNodeException, EntityException  {
		if (quantity > 0) {
			for (NodeLink nodeLink : pnlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveDrillDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}

	/**
	 * Gets the project node links by subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the project node links by subnode
	 * @throws EntityException the entity exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
	public List<NodeLink> getProjectNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : pnlr.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	/**
	 * Update node link.
	 *
	 * @param nodelink the nodelink
	 * @return the node link
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 */
	public NodeLink updateNodeLink(NodeLink nodelink) throws EntityException, UnknownNodeLinkException {
		NodeLink updatedNodeLink = pnlr.getNodeLink(nodelink.getId());
		if (nodelink.getSubnodeId()!=0) {
			updatedNodeLink.setSubnodeId(nodelink.getSubnodeId());
		}
		return pnlr.updateNodeLink(updatedNodeLink);
	}
}
