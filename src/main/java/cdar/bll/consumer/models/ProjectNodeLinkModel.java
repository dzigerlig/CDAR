package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.ProjectNodeLink;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkDao;

public class ProjectNodeLinkModel {
	private ConsumerDaoController cdc = new ConsumerDaoController();

	public Set<ProjectNodeLink> getProjectNodeLinks(int projecttreeid) {
		Set<ProjectNodeLink> projectNodeLinks = new HashSet<ProjectNodeLink>();
		
		for (ProjectNodeLinkDao pnld : cdc.getProjectNodeLinks(projecttreeid)) {
			projectNodeLinks.add(new ProjectNodeLink(pnld));
		}
		
		return projectNodeLinks;
	}

	public ProjectNodeLink addProjectNodeLink(int kptid, int sourceid, int targetid, int kpnsnid) {
		ProjectNodeLinkDao pnld = new ProjectNodeLinkDao();
		pnld.setKptid(kptid);
		pnld.setSourceid(sourceid);
		pnld.setTargetid(targetid);
		pnld.setKpnsnid(kpnsnid);
		return new ProjectNodeLink(pnld.create());
	}
	
	public ProjectNodeLink updateLink(ProjectNodeLink pnl) {
		ProjectNodeLinkDao pnld = cdc.getProjectNodeLink(pnl.getId());
		pnld.setKptid(pnl.getRefProjectTreeId());
		pnld.setKpnsnid(pnl.getRefProjectSubNodeId());
		pnld.setSourceid(pnl.getSourceId());
		pnld.setTargetid(pnl.getTargetId());
		return new ProjectNodeLink(pnld.update());
	}
	
	public ProjectNodeLink getProjectNodeLink(int id) {
		return new ProjectNodeLink(cdc.getProjectNodeLink(id));
	}
	
	public boolean removeProjectNodeLink(int id) {
		ProjectNodeLinkDao pnld = cdc.getProjectNodeLink(id);
		return pnld.delete();
	}

}
