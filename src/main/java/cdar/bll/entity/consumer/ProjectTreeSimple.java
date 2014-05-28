package cdar.bll.entity.consumer;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.entity.Directory;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Tree;
import cdar.bll.entity.UserRole;
import cdar.bll.manager.DirectoryManager;
import cdar.bll.manager.TreeManager;
import cdar.bll.manager.consumer.CommentManager;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

/**
 * The Class ProjectTreeSimple.
 */
@XmlRootElement
public class ProjectTreeSimple {
		
		/** The tree. */
		private Tree tree;
		
		/** The project nodes. */
		private Set<ProjectNode> projectNodes;
		
		/** The project subnodes. */
		private Set<ProjectSubnode> projectSubnodes;
		
		/** The links. */
		private Set<NodeLink> links;
		
		/** The directories. */
		private Set<Directory> directories;
		
		/** The comments. */
		private Set<Comment> comments;
		
		/** The ptm. */
		private TreeManager ptm = new TreeManager(UserRole.CONSUMER);
		
		/** The psm. */
		private ProjectSubnodeManager psm = new ProjectSubnodeManager();
		
		/** The pnm. */
		private ProjectNodeManager pnm = new ProjectNodeManager();
		
		/** The pnlm. */
		private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		
		/** The pdm. */
		private DirectoryManager pdm = new DirectoryManager(UserRole.CONSUMER);
		
		/** The cm. */
		private CommentManager cm = new CommentManager();
		
		/**
		 * Instantiates a new project tree simple.
		 */
		public ProjectTreeSimple() {}
		
		/**
		 * Instantiates a new project tree simple.
		 *
		 * @param treeId the tree id
		 * @throws UnknownProjectTreeException the unknown project tree exception
		 * @throws EntityException the entity exception
		 * @throws UnknownProjectNodeLinkException the unknown project node link exception
		 * @throws UnknownTreeException the unknown tree exception
		 * @throws UnknownUserException the unknown user exception
		 */
		public ProjectTreeSimple(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownTreeException, UnknownUserException {
			setTree(ptm.getTree(treeId));
			setProjectNodes(pnm.getProjectNodes(treeId));
			setLinks(pnlm.getProjectNodeLinks(treeId));
			setDirectories(pdm.getDirectories(treeId));
			setComments(cm.getCommentsByTree(treeId));
			setProjectSubnodes(psm.getProjectSubnodesFromProjectTree(treeId));
		}

		/**
		 * Gets the tree.
		 *
		 * @return the tree
		 */
		public Tree getTree() {
			return tree;
		}

		/**
		 * Sets the tree.
		 *
		 * @param tree the new tree
		 */
		@XmlElement
		public void setTree(Tree tree) {
			this.tree = tree;
		}

		/**
		 * Gets the project nodes.
		 *
		 * @return the project nodes
		 */
		public Set<ProjectNode> getProjectNodes() {
			return projectNodes;
		}

		/**
		 * Sets the project nodes.
		 *
		 * @param projectNodes the new project nodes
		 */
		@XmlElement
		public void setProjectNodes(Set<ProjectNode> projectNodes) {
			this.projectNodes = projectNodes;
		}

		/**
		 * Gets the project subnodes.
		 *
		 * @return the project subnodes
		 */
		public Set<ProjectSubnode> getProjectSubnodes() {
			return projectSubnodes;
		}

		/**
		 * Sets the project subnodes.
		 *
		 * @param projectSubnodes the new project subnodes
		 */
		@XmlElement
		public void setProjectSubnodes(Set<ProjectSubnode> projectSubnodes) {
			this.projectSubnodes = projectSubnodes;
		}

		/**
		 * Gets the links.
		 *
		 * @return the links
		 */
		public Set<NodeLink> getLinks() {
			return links;
		}

		/**
		 * Sets the links.
		 *
		 * @param links the new links
		 */
		@XmlElement
		public void setLinks(Set<NodeLink> links) {
			this.links = links;
		}

		/**
		 * Gets the directories.
		 *
		 * @return the directories
		 */
		public Set<Directory> getDirectories() {
			return directories;
		}

		/**
		 * Sets the directories.
		 *
		 * @param directories the new directories
		 */
		@XmlElement
		public void setDirectories(Set<Directory> directories) {
			this.directories = directories;
		}

		/**
		 * Gets the comments.
		 *
		 * @return the comments
		 */
		public Set<Comment> getComments() {
			return comments;
		}

		/**
		 * Sets the comments.
		 *
		 * @param comments the new comments
		 */
		@XmlElement
		public void setComments(Set<Comment> comments) {
			this.comments = comments;
		}
}
