package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNodeLink;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkRepository;

public class ProjectNodeLinkModel {
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();

	public Set<ProjectNodeLink> getProjectNodeLinks(int projecttreeid) throws UnknownProjectTreeException {
		Set<ProjectNodeLink> projectNodeLinks = new HashSet<ProjectNodeLink>();
		
		for (ProjectNodeLink projectNodeLink : pnlr.getProjectNodeLinks(projecttreeid)) {
			projectNodeLinks.add(projectNodeLink);
		}
		
		return projectNodeLinks;
	}

	public ProjectNodeLink addProjectNodeLink(int kptid, int sourceId, int targetId, int kpnsnid) throws UnknownProjectTreeException {
		ProjectNodeLink projectNodeLink = new ProjectNodeLink();
		projectNodeLink.setRefProjectTreeId(kptid);
		projectNodeLink.setSourceId(sourceId);
		projectNodeLink.setTargetId(targetId);
		projectNodeLink.setRefProjectSubNodeId(kpnsnid);
		return pnlr.createProjectNodeLink(projectNodeLink);
	}
	
	public ProjectNodeLink updateLink(ProjectNodeLink pnl) throws UnknownProjectNodeLinkException {
		ProjectNodeLink updatedProjectNodeLink = pnlr.getProjectNodeLink(pnl.getId());
		updatedProjectNodeLink.setRefProjectTreeId(pnl.getRefProjectTreeId());
		updatedProjectNodeLink.setRefProjectSubNodeId(pnl.getRefProjectSubNodeId());
		updatedProjectNodeLink.setSourceId(pnl.getSourceId());
		updatedProjectNodeLink.setTargetId(pnl.getTargetId());
		return pnlr.updateProjectNodeLink(updatedProjectNodeLink);
	}
	
	public ProjectNodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		return pnlr.getProjectNodeLink(projectNodeLinkId);
	}
	
	public boolean removeProjectNodeLink(int projectNodeLinkId) throws Exception {
		return pnlr.deleteProjectNodeLink(projectNodeLinkId);
	}
}
