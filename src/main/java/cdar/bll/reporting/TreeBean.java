package cdar.bll.reporting;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TreeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;

public class TreeBean extends ReportingBean {
	
	public TreeBean() throws Exception {
		super();
	}

	private TreeManager tm = new TreeManager();
	private NodeManager nm = new NodeManager();
	private SubnodeManager sm = new SubnodeManager();
	private MediaWikiModel mwm = new MediaWikiModel();
	private NodeLinkManager nlm = new NodeLinkManager();


	public String getTreeTitle() throws UnknownTreeException {
		return tm.getTree(getTreeId()).getTitle();
	}

	public Set<Node> getNodes() throws EntityException, UnknownTreeException {
		return nm.getNodes(getTreeId());
	}

	public Set<Subnode> getSubnodes(int nodeId) throws EntityException,
			UnknownNodeException {
		return sm.getSubnodesFromNode(nodeId);
	}

	public String getNodeWikiEntryHtml(int nodeId) throws UnknownNodeException, EntityException {
		String html = mwm.getKnowledgeNodeWikiEntry(nodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}

	public String getSubnodeWikiEntryHtml(int subnodeId) throws UnknownSubnodeException, EntityException {
		String html = mwm.getKnowledgeSubnodeWikiEntry(subnodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}
	
	public Set<NodeLink> getNodeLinks(boolean target, int nodeId) throws EntityException, UnknownTreeException {
		Set<NodeLink> nodeLinks = new HashSet<NodeLink>();
	
		for (NodeLink nodeLink : nlm.getNodeLinks(getTreeId())) {
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
	
	public Node getNode(int nodeId) throws UnknownNodeException, EntityException {
		return nm.getNode(nodeId);
	}
	
	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException, EntityException {
		return sm.getSubnode(subnodeId);
	}
}
