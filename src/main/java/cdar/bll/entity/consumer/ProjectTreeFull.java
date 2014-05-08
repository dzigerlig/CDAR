package cdar.bll.entity.consumer;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.entity.Directory;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Tree;
import cdar.bll.manager.consumer.CommentManager;
import cdar.bll.manager.consumer.ProjectDirectoryManager;
import cdar.bll.manager.consumer.ProjectNodeLinkManager;
import cdar.bll.manager.consumer.ProjectNodeManager;
import cdar.bll.manager.consumer.ProjectSubnodeManager;
import cdar.bll.manager.consumer.ProjectTreeManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.bll.wiki.WikiEntry;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

@XmlRootElement
public class ProjectTreeFull {
	private Tree tree;
	private Set<ProjectNode> projectNodes;
	private Set<ProjectSubnode> projectSubnodes;
	private Set<NodeLink> links;
	private Set<Directory> directories;
	private Set<Comment> comments;
	private Set<WikiEntry> wikiEntries;
	
	private ProjectTreeManager ptm = new ProjectTreeManager();
	private ProjectSubnodeManager psm = new ProjectSubnodeManager();
	private ProjectNodeManager pnm = new ProjectNodeManager();
	private ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
	private ProjectDirectoryManager pdm = new ProjectDirectoryManager();
	private CommentManager cm = new CommentManager();
	private MediaWikiModel mwm = new MediaWikiModel();
	
	public ProjectTreeFull() { }
	
	public ProjectTreeFull(int treeId) throws UnknownProjectTreeException, EntityException, UnknownProjectNodeLinkException, UnknownProjectNodeException, UnknownProjectSubnodeException {
		setTree(ptm.getProjectTree(treeId));
		setProjectNodes(pnm.getProjectNodes(treeId));
		setLinks(pnlm.getProjectNodeLinks(treeId));
		setDirectories(pdm.getDirectories(treeId));
		setComments(cm.getCommentsByTree(treeId));
		setProjectSubnodes(psm.getProjectSubnodesFromProjectTree(treeId));
		fillWikiEntries();
	}
	
	private void fillWikiEntries() throws UnknownProjectNodeException, EntityException, UnknownProjectSubnodeException {
		Set<WikiEntry> wikiEntries = new HashSet<WikiEntry>();
		
		for (ProjectNode projectNode : getProjectNodes()) {
			wikiEntries.add(mwm.getProjectNodeWikiEntry(projectNode.getId()));
		}
		
		for (ProjectSubnode projectSubnode : getProjectSubnodes()) {
			wikiEntries.add(mwm.getKnowledgeProjectSubnodeWikiEntry(projectSubnode.getId()));
		}
		
		setWikiEntries(wikiEntries);
	}
	
	public Tree getTree() {
		return tree;
	}

	@XmlElement
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Set<ProjectNode> getProjectNodes() {
		return projectNodes;
	}

	@XmlElement
	public void setProjectNodes(Set<ProjectNode> projectNodes) {
		this.projectNodes = projectNodes;
	}

	public Set<ProjectSubnode> getProjectSubnodes() {
		return projectSubnodes;
	}

	@XmlElement
	public void setProjectSubnodes(Set<ProjectSubnode> projectSubnodes) {
		this.projectSubnodes = projectSubnodes;
	}

	public Set<NodeLink> getLinks() {
		return links;
	}

	@XmlElement
	public void setLinks(Set<NodeLink> links) {
		this.links = links;
	}

	public Set<Directory> getDirectories() {
		return directories;
	}

	@XmlElement
	public void setDirectories(Set<Directory> directories) {
		this.directories = directories;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	@XmlElement
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@XmlElement
	public Set<WikiEntry> getWikiEntries() {
		return wikiEntries;
	}

	public void setWikiEntries(Set<WikiEntry> wikiEntries) {
		this.wikiEntries = wikiEntries;
	}
}
