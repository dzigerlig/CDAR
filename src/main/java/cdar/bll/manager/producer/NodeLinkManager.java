package cdar.bll.manager.producer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.NodeLinkRepository;
import cdar.dal.producer.NodeRepository;
import cdar.dal.user.UserRepository;

import cdar.pl.controller.StatusHelper;

/**
 * The Class NodeLinkManager.
 */
public class NodeLinkManager {
	
	/** The nlr. */
	private NodeLinkRepository nlr = new NodeLinkRepository();

	/**
	 * Gets the node links.
	 *
	 * @param treeId the tree id
	 * @return the node links
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public Set<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException  {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinks(treeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	/**
	 * Delete node link.
	 *
	 * @param nodeLinkId the node link id
	 * @throws UnknownNodeLinkException the unknown node link exception
	 */
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException  
	{
		nlr.deleteNodeLink(nodeLinkId);
	}
	
	/**
	 * Adds the node link.
	 *
	 * @param nodeLink the node link
	 * @return the node link
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public NodeLink addNodeLink(NodeLink nodeLink) throws UnknownTreeException  
	{
		return nlr.createNodeLink(nodeLink);
	}

	/**
	 * Update node link.
	 *
	 * @param nodelink the nodelink
	 * @return the node link
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws EntityException the entity exception
	 */
	public NodeLink updateNodeLink(NodeLink nodelink) throws UnknownNodeLinkException, EntityException {
		NodeLink updatedNodeLink = nlr.getNodeLink(nodelink.getId());
		if (nodelink.getSubnodeId()!=0) {
			updatedNodeLink.setSubnodeId(nodelink.getSubnodeId());
		}
		return nlr.updateNodeLink(updatedNodeLink);
	}

	/**
	 * Gets the node link.
	 *
	 * @param nodeLinkId the node link id
	 * @return the node link
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws EntityException the entity exception
	 */
	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException {
		return nlr.getNodeLink(nodeLinkId);
	}

	/**
	 * Gets the node links by subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the node links by subnode
	 * @throws EntityException the entity exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException  {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
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
			Node rootNode = new NodeRepository().getRoot(treeId);
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
			for (NodeLink nodeLink : nlr.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : nlr.getParentNodeLinks(nodeId)) {
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
			for (NodeLink nodeLink : nlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveDrillDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}
}