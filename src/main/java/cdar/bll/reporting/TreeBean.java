package cdar.bll.reporting;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.entity.CdarDescriptions;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.WikiEntry;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TreeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class TreeBean {
	private int treeId;
	private Date creationTime;
	private CdarDescriptions cdarDescriptions;

	private TreeManager tm = new TreeManager();
	private NodeManager nm = new NodeManager();
	private SubnodeManager sm = new SubnodeManager();
	private MediaWikiModel mwm = new MediaWikiModel();
	private NodeLinkManager nlm = new NodeLinkManager();

	public TreeBean() throws Exception {
		setCreationTime(new Date());
		setCdarDescriptions(new CdarDescriptions());
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) throws UnknownTreeException,
			EntityException, UnknownNodeException, UnknownUserException,
			UnknownSubnodeException {
		this.treeId = treeId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public CdarDescriptions getCdarDescriptions() {
		return cdarDescriptions;
	}

	public void setCdarDescriptions(CdarDescriptions cdarDescriptions) {
		this.cdarDescriptions = cdarDescriptions;
	}

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
