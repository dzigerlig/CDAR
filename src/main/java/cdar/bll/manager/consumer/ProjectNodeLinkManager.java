package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.NodeLink;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeLinkManager {
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

	public Set<NodeLink> getProjectNodeLinks(int projecttreeid) throws UnknownProjectTreeException {
		Set<NodeLink> projectNodeLinks = new HashSet<NodeLink>();
		
		for (NodeLink projectNodeLink : pnlr.getProjectNodeLinks(projecttreeid)) {
			projectNodeLinks.add(projectNodeLink);
		}
		
		return projectNodeLinks;
	}

	public NodeLink addProjectNodeLink(int kptid, int sourceId, int targetId, int kpnsnid) throws UnknownProjectTreeException {
		NodeLink projectNodeLink = new NodeLink();
		projectNodeLink.setTreeId(kptid);
		projectNodeLink.setSourceId(sourceId);
		projectNodeLink.setTargetId(targetId);
		projectNodeLink.setSubnodeId(kpnsnid);
		return pnlr.createProjectNodeLink(projectNodeLink);
	}
	
	public NodeLink updateLink(NodeLink nodeLink) throws UnknownProjectNodeLinkException {
		NodeLink updatedProjectNodeLink = pnlr.getProjectNodeLink(nodeLink.getId());
		updatedProjectNodeLink.setTreeId(nodeLink.getTreeId());
		updatedProjectNodeLink.setSubnodeId(nodeLink.getSubnodeId());
		updatedProjectNodeLink.setSourceId(nodeLink.getSourceId());
		updatedProjectNodeLink.setTargetId(nodeLink.getTargetId());
		return pnlr.updateProjectNodeLink(updatedProjectNodeLink);
	}
	
	public NodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		return pnlr.getProjectNodeLink(projectNodeLinkId);
	}
	
	public boolean removeProjectNodeLink(int projectNodeLinkId) throws Exception {
		return pnlr.deleteProjectNodeLink(projectNodeLinkId);
	}
}
