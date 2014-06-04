package ch.cdar.bll.reporting;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.producer.NodeLinkManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownSubnodeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;

/**
 * The Class TreeBean.
 */
public class TreeBean extends ReportingBean {
	/** The Tree Manager. */
	private TreeManager tm = new TreeManager(UserRole.PRODUCER);
	
	/** The Node Manager. */
	private NodeManager nm = new NodeManager();
	
	/** The Subnode Manager. */
	private SubnodeManager sm = new SubnodeManager();
	
	/** The Media Wiki Manager. */
	private MediaWikiManager mwm = new MediaWikiManager();
	
	/** The Node Link Manager. */
	private NodeLinkManager nlm = new NodeLinkManager();
	
	/**
	 * Instantiates a new tree bean.
	 *
	 * @throws Exception the exception
	 */
	public TreeBean() throws Exception {
		super(UserRole.PRODUCER);
	}

	/**
	 * Gets the tree title.
	 *
	 * @return the tree title
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException 
	 */
	public String getTreeTitle() throws UnknownTreeException, UnknownProjectTreeException, EntityException, UnknownUserException {
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