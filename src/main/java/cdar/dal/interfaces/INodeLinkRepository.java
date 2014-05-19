package cdar.dal.interfaces;

import java.util.List;

import cdar.bll.entity.NodeLink;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;

public interface INodeLinkRepository {
	public List<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException, UnknownProjectTreeException;
	public List<NodeLink> getParentNodeLinks(int nodeId) throws EntityException, UnknownNodeLinkException;
	public List<NodeLink> getSiblingNodeLinks(int nodeId) throws UnknownTreeException, EntityException;
	public List<NodeLink> getFollowerNodeLinks(int nodeId) throws UnknownNodeException, EntityException;
	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException;
	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException;
	public NodeLink createNodeLink(NodeLink nodeLink) throws UnknownTreeException, UnknownProjectTreeException;
	public NodeLink updateNodeLink(NodeLink nodeLink) throws UnknownNodeLinkException;
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException, UnknownProjectNodeLinkException;
}
