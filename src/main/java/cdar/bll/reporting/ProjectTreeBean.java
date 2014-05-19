package cdar.bll.reporting;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.UserRole;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;

public class ProjectTreeBean extends ReportingBean {
	public ProjectTreeBean() throws Exception {
		super();
	}
	
	private TreeManager ptm = new TreeManager(UserRole.CONSUMER);
	private ProjectNodeManager pnm = new ProjectNodeManager();
	private ProjectSubnodeManager psm = new ProjectSubnodeManager();
	private MediaWikiManager mwm = new MediaWikiManager();
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
	
	public String getTreeTitle() throws UnknownProjectTreeException, EntityException, UnknownTreeException {
		return ptm.getTree(getTreeId()).getTitle();
	}
	
	public Set<ProjectNode> getNodes() throws UnknownProjectTreeException, EntityException {
		return pnm.getProjectNodes(getTreeId());
	}
	
	public Set<ProjectSubnode> getSubnodes(int nodeId) throws UnknownProjectNodeLinkException, EntityException {
		return psm.getProjectSubnodesFromProjectNode(nodeId);
	}
	
	public String getNodeWikiEntryHtml(int nodeId) throws UnknownProjectNodeException, EntityException {
		String html = mwm.getProjectNodeWikiEntry(nodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}
	
	public String getSubnodeWikiEntryHtml(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		String html = mwm.getKnowledgeProjectSubnodeWikiEntry(subnodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}
	
	public Set<NodeLink> getNodeLinks(boolean target, int nodeId) throws UnknownProjectTreeException, EntityException {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
		
		for (NodeLink nodeLink : pnlm.getProjectNodeLinks(getTreeId())) {
			if (target) {
				if (nodeLink.getTargetId()==nodeId) {
					nodeLinks.add(nodeLink);
				}
			} else {
				if (nodeLink.getSourceId()==nodeId) {
					nodeLinks.add(nodeLink);
				}
			}
		}
		return nodeLinks;
	}
	
	public ProjectNode getNode(int nodeId) throws UnknownProjectNodeException, EntityException {
		return pnm.getProjectNode(nodeId);
	}
	
	public ProjectSubnode getSubnode(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		return psm.getProjectSubnode(subnodeId);
	}
	
	public String getNodeStatus(int status) {
		switch (status) {
			case 2: return "accepted";
			case 3: return "declined";
			case 4: return "revoked";
			default: return "undecided";
		}
	}
}
