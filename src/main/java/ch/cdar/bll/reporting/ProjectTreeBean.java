package ch.cdar.bll.reporting;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.consumer.Comment;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.consumer.CommentManager;
import ch.cdar.bll.manager.consumer.ProjectNodeLinkManager;
import ch.cdar.bll.manager.consumer.ProjectNodeManager;
import ch.cdar.bll.manager.consumer.ProjectSubnodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;

/**
 * The Class ProjectTreeBean.
 */
public class ProjectTreeBean extends ReportingBean {
	
	/**
	 * Instantiates a new project tree bean.
	 *
	 * @throws Exception the exception
	 */
	public ProjectTreeBean() throws Exception {
		super();
	}
	
	/** The Project Tree Manager. */
	private TreeManager ptm = new TreeManager(UserRole.CONSUMER);
	
	/** The Project Node Manager. */
	private ProjectNodeManager pnm = new ProjectNodeManager();
	
	/** The Project Subnode Manager. */
	private ProjectSubnodeManager psm = new ProjectSubnodeManager();
	
	/** The Media Wiki Manager. */
	private MediaWikiManager mwm = new MediaWikiManager();
	
	/** The Project Node Link Manager. */
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
	
	/** The Comment Manager. */
	private CommentManager cm = new CommentManager();
	
	/**
	 * Gets the tree title.
	 *
	 * @return the tree title
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public String getTreeTitle() throws UnknownProjectTreeException, EntityException, UnknownTreeException {
		return ptm.getTree(getTreeId()).getTitle();
	}
	
	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Set<ProjectNode> getNodes() throws UnknownProjectTreeException, EntityException {
		return pnm.getProjectNodes(getTreeId());
	}
	
	/**
	 * Gets the subnodes.
	 *
	 * @param nodeId the node id
	 * @return the subnodes
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public Set<ProjectSubnode> getSubnodes(int nodeId) throws UnknownProjectNodeLinkException, EntityException {
		return psm.getProjectSubnodesFromProjectNode(nodeId);
	}
	
	/**
	 * Gets the node wiki entry html.
	 *
	 * @param nodeId the node id
	 * @return the node wiki entry html
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 */
	public String getNodeWikiEntryHtml(int nodeId) throws UnknownProjectNodeException, EntityException {
		String html = mwm.getProjectNodeWikiEntry(nodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}
	
	/**
	 * Gets the subnode wiki entry html.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode wiki entry html
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public String getSubnodeWikiEntryHtml(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		String html = mwm.getKnowledgeProjectSubnodeWikiEntry(subnodeId).getWikiContentHtml();
		return html == null ? "Wiki entry not found" : html;
	}
	
	/**
	 * Gets the node links.
	 *
	 * @param target the target
	 * @param nodeId the node id
	 * @return the node links
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
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
	
	/**
	 * Gets the node.
	 *
	 * @param nodeId the node id
	 * @return the node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 */
	public ProjectNode getNode(int nodeId) throws UnknownProjectNodeException, EntityException {
		return pnm.getProjectNode(nodeId);
	}
	
	/**
	 * Gets the subnode.
	 *
	 * @param subnodeId the subnode id
	 * @return the subnode
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public ProjectSubnode getSubnode(int subnodeId) throws UnknownProjectSubnodeException, EntityException {
		return psm.getProjectSubnode(subnodeId);
	}
	
	/**
	 * Gets the node status.
	 *
	 * @param status the status
	 * @return the node status
	 */
	public String getNodeStatus(int status) {
		switch (status) {
			case 1: return "open";
			case 2: return "decided";
			case 3: return "accepted";
			case 4: return "rejected";
			case 5: return "closed";
			default: return "open";
		}
	}
	
	/**
	 * Gets the comments.
	 *
	 * @param nodeId the node id
	 * @return the comments
	 * @throws EntityException the entity exception
	 */
	public Set<Comment> getComments(int nodeId) throws EntityException {
		Set<Comment> comments = new HashSet<Comment>();
		
		for (Comment comment : cm.getComments(nodeId)) {
			comments.add(comment);
		}
		
		return comments;
	}
}
