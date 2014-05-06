package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeLinkManager {
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

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

	public Set<NodeLink> zoomUp(int nodeId) {
		//TODO
		return null;
	}

	public Set<NodeLink> zoomDown(int nodeId) {
		//TODO
		return null;
	}

	public List<NodeLink> getProjectNodeLinksBySubnode(int subnodeId) {
		// TODO Auto-generated method stub
		return null;
	}
}
