/*
 * 
 */
package cdar.bll.reporting;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.UserRole;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;

// TODO: Auto-generated Javadoc
/**
 * The Class TreeBean.
 */
public class TreeBean extends ReportingBean {

	/**
	 * Instantiates a new tree bean.
	 *
	 * @throws Exception the exception
	 */
	public TreeBean() throws Exception {
		super();
	}

	/** The tm. */
	private TreeManager tm = new TreeManager(UserRole.PRODUCER);
	
	/** The nm. */
	private NodeManager nm = new NodeManager();
	
	/** The sm. */
	private SubnodeManager sm = new SubnodeManager();
	
	/** The mwm. */
	private MediaWikiManager mwm = new MediaWikiManager();
	
	/** The nlm. */
	private NodeLinkManager nlm = new NodeLinkManager();

	/**
	 * Gets the tree title.
	 *
	 * @return the tree title
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public String getTreeTitle() throws UnknownTreeException, UnknownProjectTreeException, EntityException {
		return tm.getTree(getTreeId()).getTitle();
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public Set<Node> getNodes() throws EntityException, UnknownTreeException {
		return nm.getNodes(getTreeId());
	}

	/**
	 * Gets the subnodes.
	 *
	 * @param nodeId the node id
	 * @return the subnodes
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	public Set<Subnode> getSubnodes(int nodeId) throws EntityException,
			UnknownNodeException {
		return sm.getSubnodesFromNode(nodeId);
	}

	/**
	 * Gets the node wiki entry html.
	 *
	 * @param nodeId the node id
	 * @return the node wiki entry html
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	public String getNodeWikiEntryHtml(int nodeId) throws UnknownNodeException, EntityException {
		String html = mwm.getKnowledgeNodeWikiEntry(nodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}

	/**
	 * Gets the subnode wiki entry html.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode wiki entry html
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws EntityException the entity exception
	 */
	public String getSubnodeWikiEntryHtml(int subnodeId) throws UnknownSubnodeException, EntityException {
		String html = mwm.getKnowledgeSubnodeWikiEntry(subnodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}

	/**
	 * Gets the node links.
	 *
	 * @param target the target
	 * @param nodeId the node id
	 * @return the node links
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
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

	/**
	 * Gets the node.
	 *
	 * @param nodeId the node id
	 * @return the node
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	public Node getNode(int nodeId) throws UnknownNodeException, EntityException {
		return nm.getNode(nodeId);
	}

	/**
	 * Gets the subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode
	 * @throws UnknownSubnodeException the unknown subnode exception
	 * @throws EntityException the entity exception
	 */
	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException, EntityException {
		return sm.getSubnode(subnodeId);
	}
}