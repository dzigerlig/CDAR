package cdar.bll.manager.producer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.NodeLinkRepository;
import cdar.dal.user.UserRepository;

public class NodeLinkManager {
	private NodeLinkRepository nlr = new NodeLinkRepository();

	public Set<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException  {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinks(treeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException  
	{
		nlr.deleteNodeLink(nodeLinkId);
	}
	
	public NodeLink addNodeLink(NodeLink nodeLink) throws UnknownTreeException  
	{
		return nlr.createNodeLink(nodeLink);
	}

	public NodeLink updateNodeLink(NodeLink nodelink) throws UnknownNodeLinkException, EntityException {
		NodeLink updatedNodeLink = nlr.getNodeLink(nodelink.getId());
		if (nodelink.getSubnodeId()!=0) {
			updatedNodeLink.setSubnodeId(nodelink.getSubnodeId());
		}
		return nlr.updateNodeLink(updatedNodeLink);
	}

	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException {
		return nlr.getNodeLink(nodeLinkId);
	}

	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException  {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : nlr.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public Set<NodeLink> drillUp(int uid, int nodeId) throws EntityException, UnknownNodeLinkException, UnknownTreeException, UnknownUserException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}
	
	public Set<NodeLink> drillDown(int uid, int nodeId) throws UnknownNodeException, EntityException, UnknownUserException  {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveZoomDown(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}

	private Set<NodeLink> recursiveZoomUp(int nodeId, int quantity, Set<NodeLink> links) throws EntityException, UnknownNodeLinkException, UnknownTreeException {
		if (quantity > 0) {
			for (NodeLink nodeLink : nlr.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : nlr.getParentNodeLinks(nodeId)) {
				links.add(nodeLink);
				links=recursiveZoomUp(nodeLink.getSourceId(), quantity-1, links);
			}
		}
		return links;
	}
	
	private Set<NodeLink> recursiveZoomDown(int nodeId, int quantity, Set<NodeLink> links) throws UnknownNodeException, EntityException  {
		if (quantity > 0) {
			for (NodeLink nodeLink : nlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveZoomDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}
}
