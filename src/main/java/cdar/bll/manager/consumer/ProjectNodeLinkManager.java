package cdar.bll.manager.consumer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.user.UserRepository;
import cdar.pl.controller.StatusHelper;

public class ProjectNodeLinkManager {
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

	public Set<NodeLink> getProjectNodeLinks(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		Set<NodeLink> projectNodeLinks = new HashSet<NodeLink>();
		
		for (NodeLink projectNodeLink : pnlr.getNodeLinks(projectTreeId)) {
			projectNodeLinks.add(projectNodeLink);
		}
		
		return projectNodeLinks;
	}

	public NodeLink addProjectNodeLink(NodeLink projectNodeLink) throws UnknownProjectTreeException {
		return pnlr.createNodeLink(projectNodeLink);
	}
	
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
	
	public NodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException, EntityException, UnknownNodeLinkException {
		return pnlr.getNodeLink(projectNodeLinkId);
	}
	
	public void deleteProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		pnlr.deleteNodeLink(projectNodeLinkId);
	}

	public Set<NodeLink> drillUp(int uid, int nodeId) throws EntityException, UnknownNodeLinkException, UnknownTreeException, UnknownUserException {
		Set<NodeLink> links = new HashSet<NodeLink>();
		return recursiveDrillUp(nodeId, new UserRepository().getUser(uid).getDrillHierarchy(), links);
	}
	
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
	
	private Set<NodeLink> recursiveDrillDown(int nodeId, int quantity, Set<NodeLink> links) throws UnknownNodeException, EntityException  {
		if (quantity > 0) {
			for (NodeLink nodeLink : pnlr.getFollowerNodeLinks(nodeId)) {
				links.add(nodeLink);
				links = recursiveDrillDown(nodeLink.getTargetId(), quantity-1, links);
			}
		}
		return links;
	}

	public List<NodeLink> getProjectNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException {
		List<NodeLink> nodeLinks = new ArrayList<NodeLink>();

		for (NodeLink nodeLink : pnlr.getNodeLinksBySubnode(subnodeId)) {
			nodeLinks.add(nodeLink);
		}
		
		return nodeLinks;
	}
	
	public NodeLink updateNodeLink(NodeLink nodelink) throws EntityException, UnknownNodeLinkException {
		NodeLink updatedNodeLink = pnlr.getNodeLink(nodelink.getId());
		if (nodelink.getSubnodeId()!=0) {
			updatedNodeLink.setSubnodeId(nodelink.getSubnodeId());
		}
		return pnlr.updateNodeLink(updatedNodeLink);
	}
}
