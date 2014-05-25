package cdar.bll.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.UserRole;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.interfaces.INodeLinkRepository;
import cdar.dal.producer.NodeLinkRepository;
import cdar.dal.producer.NodeRepository;
import cdar.dal.user.UserRepository;

public class NodeLinkManager {
	private INodeLinkRepository nodeLinkRepository;
	private UserRole userRole;
	
	public NodeLinkManager(UserRole userRole) {
		setUserRole(userRole);
		if (getUserRole() == UserRole.CONSUMER) {
			nodeLinkRepository = new ProjectNodeLinkRepository();
		} else {
			nodeLinkRepository = new NodeLinkRepository();
		}
	}
	
	public Set<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException, UnknownProjectTreeException  {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();

		for (NodeLink nodeLink : nodeLinkRepository.getNodeLinks(treeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException, UnknownProjectNodeLinkException  
	{
		nodeLinkRepository.deleteNodeLink(nodeLinkId);
	}
	
	public NodeLink addNodeLink(NodeLink nodeLink) throws UnknownTreeException, UnknownProjectTreeException  
	{
		return nodeLinkRepository.createNodeLink(nodeLink);
	}

	public NodeLink updateNodeLink(NodeLink nodeLink) throws UnknownNodeLinkException, EntityException {
		NodeLink updatedNodeLink = nodeLinkRepository.getNodeLink(nodeLink.getId());
		if (nodeLink.getTreeId()!=0) {
			updatedNodeLink.setTreeId(nodeLink.getTreeId());
		}
		if (nodeLink.getSubnodeId()!=0) {
			updatedNodeLink.setSubnodeId(nodeLink.getSubnodeId());
		}
		if (nodeLink.getSourceId()!=0) {
			updatedNodeLink.setSourceId(nodeLink.getSourceId());
		}
		if (nodeLink.getTargetId()!=0) {
			updatedNodeLink.setTargetId(nodeLink.getTargetId());
		}
		return nodeLinkRepository.updateNodeLink(updatedNodeLink);
	}

	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException {
		return nodeLinkRepository.getNodeLink(nodeLinkId);
	}

	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException  {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : nodeLinkRepository.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public Set<NodeLink> drillUp(int uid, int nodeId) throws EntityException, UnknownNodeLinkException, UnknownTreeException, UnknownUserException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}
	
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

	private Set<NodeLink> recursiveDrillUp(int nodeId, int quantity, Set<NodeLink> links) throws EntityException, UnknownNodeLinkException, UnknownTreeException {
		if (quantity > 0) {
			for (NodeLink nodeLink : nodeLinkRepository.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : nodeLinkRepository.getParentNodeLinks(nodeId)) {
				links.add(nodeLink);
				links=recursiveDrillUp(nodeLink.getSourceId(), quantity-1, links);
			}
		}
		return links;
	}
	
	private Set<NodeLink> recursiveDrillDown(int nodeId, int quantity, Set<NodeLink> links) throws UnknownNodeException, EntityException  {
		if (quantity > 0) {
			for (NodeLink nodeLink : nodeLinkRepository.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveDrillDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}
	

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
}
