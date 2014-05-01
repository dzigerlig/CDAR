package cdar.bll.manager.consumer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.dal.consumer.ProjectDirectoryRepository;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.consumer.ProjectTreeRepository;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.producer.DirectoryRepository;

public class ProjectTreeManager {

	private ProjectTreeRepository ptr = new ProjectTreeRepository();
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

	public Set<Tree> getProjectTrees(int uid) throws SQLException {
		Set<Tree> projectTrees = new HashSet<Tree>();
		for (Tree projectTree : ptr.getProjectTrees(uid)) {
			projectTrees.add(projectTree);
		}
		return projectTrees;
	}

	public Tree addProjectTree(int uid, String treeTitle)
			throws Exception {
		ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
		Tree projectTree = new Tree();
		projectTree.setUid(uid);
		projectTree.setTitle(treeTitle);
		projectTree = ptr.createProjectTree(projectTree);
		Directory directory = new Directory();
		directory.setKtrid(projectTree.getId());
		directory.setTitle(treeTitle);
		pdr.createDirectory(directory);
		return projectTree;
	}

	public Tree getProjectTree(int ptreeId) throws UnknownProjectTreeException {
		return ptr.getProjectTree(ptreeId);
	}

	public void addKnowledgeTreeToProjectTree(int ktreeId, int ptreeId)
			throws Exception {
		Map<Integer, Integer> linkMapping = new HashMap<Integer, Integer>();
		NodeManager nm = new NodeManager();
		SubnodeManager snm = new SubnodeManager();
		NodeLinkManager nlm = new NodeLinkManager();
		DirectoryManager dm = new DirectoryManager();
		ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
		
		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		for (Directory directory : dm.getDirectories(ktreeId)) {
			System.out.println("parent: " + directory.getParentid());
			Directory projectDirectory = new Directory();
			projectDirectory.setKtrid(ptreeId);
			projectDirectory.setTitle(directory.getTitle());
			if (directory.getParentid()==0) {
				projectDirectory.setParentid(0);
			} else {
				projectDirectory.setParentid(directoryMapping.get(directory.getId()));
			}
			projectDirectory = pdr.createDirectory(projectDirectory);
			directoryMapping.put(directory.getId(), projectDirectory.getId());
		}

		for (Node node : nm.getNodes(ktreeId)) {
			ProjectNode projectNode = new ProjectNode();
			projectNode.setKtrid(ptreeId);
			projectNode.setTitle(node.getTitle());
			projectNode.setWikiTitle(node.getWikiTitle());
			projectNode.setDid(directoryMapping.get(node.getDid()));
			projectNode = pnr.createProjectNode(projectNode);
			linkMapping.put(node.getId(), projectNode.getId());
		}
		
		for (Subnode subnode : snm.getSubnodesFromTree(ktreeId)) {
			ProjectSubnode projectSubnode = new ProjectSubnode();
			projectSubnode.setTitle(subnode.getTitle());
			projectSubnode.setWikiTitle(subnode.getWikiTitle());
			projectSubnode.setRefProjectNodeId(linkMapping.get(subnode.getKnid()));
			projectSubnode.setPosition(subnode.getPosition());
			psr.createProjectSubnode(projectSubnode);
		}

		for (NodeLink nodelink : nlm.getNodeLinks(ktreeId)) {
			
			NodeLink projectNodeLink = new NodeLink();
			projectNodeLink.setSourceId(linkMapping.get(nodelink.getSourceId()));
			projectNodeLink.setTargetId(linkMapping.get(nodelink.getTargetId()));
			projectNodeLink.setKsnid(nodelink.getKsnid());
			projectNodeLink.setKtrid(ptreeId);
			pnlr.createProjectNodeLink(projectNodeLink);
		}
	}

	public boolean deleteProjectTree(int ptreeId) throws UnknownProjectTreeException {
		return ptr.deleteProjectTree(ptreeId);
	}

	public Tree updateProjectTree(Tree projectTree) throws Exception {
		Tree updatedProjectTree = ptr.getProjectTree(projectTree.getId());
		updatedProjectTree.setTitle(projectTree.getTitle());
		return ptr.updateProjectTree(updatedProjectTree);
	}
}
