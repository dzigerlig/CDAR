package cdar.bll.manager;

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
import cdar.bll.entity.User;
import cdar.bll.entity.UserRole;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.dal.consumer.ProjectNodeLinkRepository;
import cdar.dal.consumer.ProjectDirectoryRepository;
import cdar.dal.consumer.ProjectNodeRepository;
import cdar.dal.consumer.ProjectSubnodeRepository;
import cdar.dal.consumer.ProjectTreeRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.interfaces.IDirectoryRepository;
import cdar.dal.interfaces.ITreeRepository;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.TreeRepository;
import cdar.dal.user.UserRepository;

public class TreeManager {
	private ITreeRepository treeRepository;
	private UserRole userRole;

	public TreeManager(UserRole userRole) {
		setUserRole(userRole);
		if (getUserRole() == UserRole.CONSUMER) {
			treeRepository = new ProjectTreeRepository();
		} else {
			treeRepository = new TreeRepository();
		}
	}

	public Set<Tree> getTrees(int uid) throws UnknownUserException,
			EntityException {
		Set<Tree> trees = new HashSet<Tree>();
		for (Tree tree : treeRepository.getTrees(uid)) {
			trees.add(tree);
		}
		return trees;
	}

	public Tree addTree(int uid, Tree tree) throws UnknownUserException,
			EntityException, CreationException, UnknownDirectoryException {
		User user = new UserRepository().getUser(uid);
		tree.setUserId(user.getId());
		tree = treeRepository.createTree(tree);
		Directory directory = new Directory();
		directory.setTreeId(tree.getId());
		directory.setTitle(tree.getTitle());
		if (getUserRole() == UserRole.CONSUMER) {
			ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
			pdr.createDirectory(directory);
		} else {
			DirectoryRepository dr = new DirectoryRepository();
			dr.createDirectory(directory);
		}
		return tree;
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
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
		ProjectNodeLinkRepository pnlr = new ProjectNodeLinkRepository();
		ProjectNodeRepository pnr = new ProjectNodeRepository();
		ProjectSubnodeRepository psr = new ProjectSubnodeRepository();
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
				projectDirectory.setParentId(directoryMapping.get(directory
						.getParentId()));
				projectDirectory = pdr.createDirectory(projectDirectory);
				directoryMapping.put(directory.getId(),
						projectDirectory.getId());
			} else {
				directoryMapping.put(directory.getId(), rootDirectory.getId());
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
			projectNode.setInheritedTreeId(node.getTreeId());
			projectNode = pnr.createNode(projectNode);
			linkMapping.put(node.getId(), projectNode.getId());
		}

		for (Subnode subnode : snm.getSubnodesFromTree(ktreeId)) {
			ProjectSubnode projectSubnode = new ProjectSubnode();
			projectSubnode.setTitle(subnode.getTitle());
			projectSubnode.setWikititle(subnode.getWikititle());
			projectSubnode.setNodeId(linkMapping.get(subnode.getNodeId()));
			projectSubnode.setPosition(subnode.getPosition());
			projectSubnode.setInheritedTreeId(ktreeId);
			projectSubnode = psr.createSubnode(projectSubnode);
			subnodeMapping.put(subnode.getId(), projectSubnode.getId());
		}

		for (NodeLink nodelink : nlm.getNodeLinks(ktreeId)) {
			NodeLink projectNodeLink = new NodeLink();
			projectNodeLink
					.setSourceId(linkMapping.get(nodelink.getSourceId()));
			projectNodeLink
					.setTargetId(linkMapping.get(nodelink.getTargetId()));
			if (nodelink.getSubnodeId() != 0) {
				projectNodeLink.setSubnodeId(subnodeMapping.get(nodelink
						.getSubnodeId()));
			}
			projectNodeLink.setTreeId(ptreeId);
			pnlr.createNodeLink(projectNodeLink);
		}
	}

	public void deleteTree(int ktreeId) throws UnknownTreeException,
			UnknownProjectTreeException {
		treeRepository.deleteTree(ktreeId);
	}

	public Tree getTree(int treeId) throws UnknownTreeException,
			UnknownProjectTreeException, EntityException {
		return treeRepository.getTree(treeId);
	}

	public Tree updateTree(Tree tree) throws UnknownTreeException,
			UnknownProjectTreeException, EntityException,
			UnknownDirectoryException {
		Tree updatedTree = treeRepository.getTree(tree.getId());

		if (tree.getTitle() != null) {
			updateDirectory(tree);
			updatedTree.setTitle(tree.getTitle());
		}
		return treeRepository.updateTree(updatedTree);
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	private void updateDirectory(Tree tree) throws EntityException,
			UnknownDirectoryException {
		IDirectoryRepository directoryRepository;
		Directory directory;
		if (getUserRole() == UserRole.CONSUMER) {
			directoryRepository = new ProjectDirectoryRepository();
		} else {
			directoryRepository = new DirectoryRepository();
		}
		directory = directoryRepository.getRootDirectory(tree.getId());
		directory.setTitle(tree.getTitle());
		directoryRepository.updateDirectory(directory);
	}

}
