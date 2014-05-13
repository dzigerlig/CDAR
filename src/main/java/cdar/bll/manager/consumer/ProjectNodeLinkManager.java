package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.dal.consumer.NodeLinkRepository;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.user.UserRepository;

public class ProjectNodeLinkManager {
	private NodeLinkRepository pnlr = new NodeLinkRepository();

	public Set<NodeLink> getProjectNodeLinks(int projecttreeid) throws UnknownProjectTreeException, EntityException {
		Set<NodeLink> projectNodeLinks = new HashSet<NodeLink>();
		
		for (NodeLink projectNodeLink : pnlr.getProjectNodeLinks(projecttreeid)) {
			projectNodeLinks.add(projectNodeLink);
		}
		
		return projectNodeLinks;
	}

	public NodeLink addProjectNodeLink(NodeLink projectNodeLink) throws UnknownProjectTreeException {
		return pnlr.createProjectNodeLink(projectNodeLink);
	}
	
	public NodeLink updateLink(NodeLink nodeLink) throws UnknownProjectNodeLinkException, EntityException {
		NodeLink updatedProjectNodeLink = pnlr.getProjectNodeLink(nodeLink.getId());
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
		return pnlr.updateProjectNodeLink(updatedProjectNodeLink);
	}
	
	public NodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException, EntityException {
		return pnlr.getProjectNodeLink(projectNodeLinkId);
	}
	
	public void deleteProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		pnlr.deleteProjectNodeLink(projectNodeLinkId);
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
			for (NodeLink nodeLink : pnlr.getSiblingNodeLinks(nodeId)) {
				links.add(nodeLink);
			}
			for (NodeLink nodeLink : pnlr.getParentNodeLinks(nodeId)) {
				links.add(nodeLink);
				links=recursiveZoomUp(nodeLink.getSourceId(), quantity-1, links);
			}
		}
		return links;
	}
	
	private Set<NodeLink> recursiveZoomDown(int nodeId, int quantity, Set<NodeLink> links) throws UnknownNodeException, EntityException  {
		if (quantity > 0) {
			for (NodeLink nodeLink : pnlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveZoomDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}

	public List<NodeLink> getProjectNodeLinksBySubnode(int subnodeId) {
		// TODO Auto-generated method stub
		return null;
	}
}
