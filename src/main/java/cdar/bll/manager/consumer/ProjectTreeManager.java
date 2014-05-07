package cdar.bll.manager.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.consumer.CreationTree;
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
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class ProjectTreeManager {

	private ProjectTreeRepository ptr = new ProjectTreeRepository();
	private ProjectNodeRepository pnr = new ProjectNodeRepository();
	private ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();
	private ProjectSubnodeRepository psr = new ProjectSubnodeRepository();

	public Set<Tree> getProjectTrees(int uid) throws UnknownUserException,
			EntityException {
		Set<Tree> projectTrees = new HashSet<Tree>();
		for (Tree projectTree : ptr.getProjectTrees(uid)) {
			projectTrees.add(projectTree);
		}
		return projectTrees;
	}

	public Tree addProjectTree(Tree projectTree) throws Exception {
		ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
		projectTree = ptr.createProjectTree(projectTree);
		Directory directory = new Directory();
		directory.setTreeId(projectTree.getId());
		directory.setTitle(projectTree.getTitle());
		pdr.createDirectory(directory);
		return projectTree;
	}

	public Tree getProjectTree(int ptreeId) throws UnknownProjectTreeException,
			EntityException {
		return ptr.getProjectTree(ptreeId);
	}

	public void addKnowledgeTreeToProjectTree(int ktreeId, int ptreeId)
			throws EntityException, UnknownUserException, CreationException,
			UnknownTreeException, UnknownProjectTreeException,
			UnknownNodeException, UnknownProjectNodeException {
		Map<Integer, Integer> linkMapping = new HashMap<Integer, Integer>();
		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		NodeManager nm = new NodeManager();
		SubnodeManager snm = new SubnodeManager();
		NodeLinkManager nlm = new NodeLinkManager();
		DirectoryManager dm = new DirectoryManager();
		ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
		Directory rootDirectory = new Directory();
		for (Directory directory : pdr.getDirectories(ptreeId)) {
			if (directory.getParentId() == 0) {
				rootDirectory = directory;
				break;
			}
		}

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();
		List<Directory> directoryList = new ArrayList<>();
		directoryList.addAll(dm.getDirectories(ktreeId));
		Collections.sort(directoryList);
		for (Directory directory : directoryList) {
			Directory projectDirectory = new Directory();
			projectDirectory.setTreeId(ptreeId);
			projectDirectory.setTitle(directory.getTitle());
			if (directory.getParentId() != 0) {
				// projectDirectory.setParentId(0);
				projectDirectory.setParentId(directoryMapping.get(directory
						.getParentId()));
				projectDirectory = pdr.createDirectory(projectDirectory);
				directoryMapping.put(directory.getId(),
						projectDirectory.getId());
			} else {
				directoryMapping.put(directory.getId(),
						rootDirectory.getId());
			}

		}

		for (Node node : nm.getNodes(ktreeId)) {
			ProjectNode projectNode = new ProjectNode();
			projectNode.setTreeId(ptreeId);
			projectNode.setTitle(node.getTitle());
			projectNode.setWikititle(node.getWikititle());
			projectNode.setDirectoryId(directoryMapping.get(node
					.getDirectoryId()));
			projectNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
			projectNode = pnr.createProjectNode(projectNode);
			linkMapping.put(node.getId(), projectNode.getId());
		}

		for (Subnode subnode : snm.getSubnodesFromTree(ktreeId)) {
			ProjectSubnode projectSubnode = new ProjectSubnode();
			projectSubnode.setTitle(subnode.getTitle());
			projectSubnode.setWikititle(subnode.getWikititle());
			projectSubnode.setNodeId(linkMapping.get(subnode.getNodeId()));
			projectSubnode.setPosition(subnode.getPosition());
			projectSubnode= psr.createProjectSubnode(projectSubnode);
			subnodeMapping.put(subnode.getId(), projectSubnode.getId());
		}

		for (NodeLink nodelink : nlm.getNodeLinks(ktreeId)) {

			NodeLink projectNodeLink = new NodeLink();
			projectNodeLink
					.setSourceId(linkMapping.get(nodelink.getSourceId()));
			projectNodeLink
					.setTargetId(linkMapping.get(nodelink.getTargetId()));
			projectNodeLink.setSubnodeId(subnodeMapping.get(nodelink.getSubnodeId()));
			projectNodeLink.setTreeId(ptreeId);
			pnlr.createProjectNodeLink(projectNodeLink);
		}
	}

	public void deleteProjectTree(int ptreeId)
			throws UnknownProjectTreeException {
		ptr.deleteProjectTree(ptreeId);
	}

	public Tree updateProjectTree(Tree projectTree)
			throws UnknownProjectTreeException, EntityException {
		Tree updatedProjectTree = ptr.getProjectTree(projectTree.getId());
		if (projectTree.getTitle() != null) {
			updatedProjectTree.setTitle(projectTree.getTitle());
		}
		return ptr.updateProjectTree(updatedProjectTree);
	}
}
