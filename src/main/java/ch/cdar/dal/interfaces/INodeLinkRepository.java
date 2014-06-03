package ch.cdar.dal.interfaces;

import java.util.List;

import ch.cdar.bll.entity.NodeLink;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;

/**
 * The Interface INodeLinkRepository.
 */
public interface INodeLinkRepository {
	/**
	 * Gets the node links.
	 *
	 * @param treeId the tree id
	 * @return the node links
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public List<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException, UnknownProjectTreeException;
	
	/**
	 * Gets the parent node links.
	 *
	 * @param nodeId the node id
	 * @return the parent node links
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeLinkException the unknown node link exception
	 */
	public List<NodeLink> getParentNodeLinks(int nodeId) throws EntityException, UnknownNodeLinkException;
	
	/**
	 * Gets the sibling node links.
	 *
	 * @param nodeId the node id
	 * @return the sibling node links
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws EntityException the entity exception
	 */
	public List<NodeLink> getSiblingNodeLinks(int nodeId) throws UnknownTreeException, EntityException;
	
	/**
	 * Gets the follower node links.
	 *
	 * @param nodeId the node id
	 * @return the follower node links
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	public List<NodeLink> getFollowerNodeLinks(int nodeId) throws UnknownNodeException, EntityException;
	
	/**
	 * Gets the node links by subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the node links by subnode
	 * @throws EntityException the entity exception
	 * @throws UnknownSubnodeException the unknown subnode exception
	 */
	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException;
	
	/**
	 * Gets the node link.
	 *
	 * @param nodeLinkId the node link id
	 * @return the node link
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws EntityException the entity exception
	 * @throws UnknownProjectNodeLinkException 
	 */
	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException, UnknownProjectNodeLinkException;
	
	/**
	 * Creates the node link.
	 *
	 * @param nodeLink the node link
	 * @return the node link
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public NodeLink createNodeLink(NodeLink nodeLink) throws UnknownTreeException, UnknownProjectTreeException;
	
	/**
	 * Update node link.
	 *
	 * @param nodeLink the node link
	 * @return the node link
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws UnknownProjectNodeLinkException 
	 */
	public NodeLink updateNodeLink(NodeLink nodeLink) throws UnknownNodeLinkException, UnknownProjectNodeLinkException;
	
	/**
	 * Delete node link.
	 *
	 * @param nodeLinkId the node link id
	 * @throws UnknownNodeLinkException the unknown node link exception
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 */
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException, UnknownProjectNodeLinkException;
}
