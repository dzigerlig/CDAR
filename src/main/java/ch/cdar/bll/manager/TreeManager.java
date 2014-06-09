package ch.cdar.bll.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.bll.manager.producer.NodeLinkManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownDirectoryException;
import ch.cdar.dal.exceptions.UnknownNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.repository.consumer.ProjectDirectoryRepository;
import ch.cdar.dal.repository.consumer.ProjectNodeLinkRepository;
import ch.cdar.dal.repository.consumer.ProjectNodeRepository;
import ch.cdar.dal.repository.consumer.ProjectSubnodeRepository;
import ch.cdar.dal.repository.consumer.ProjectTreeRepository;
import ch.cdar.dal.repository.interfaces.IDirectoryRepository;
import ch.cdar.dal.repository.interfaces.ITreeRepository;
import ch.cdar.dal.repository.producer.DirectoryRepository;
import ch.cdar.dal.repository.producer.TreeRepository;
import ch.cdar.dal.repository.user.UserRepository;

/**
 * The Class TreeManager.
 */
public class TreeManager {
	/** The tree repository. */
	private ITreeRepository treeRepository;
	
	/** The user role. */
	private UserRole userRole;

	/**
	 * Instantiates a new tree manager.
	 *
	 * @param userRole the user role
	 */
	public TreeManager(UserRole userRole) {
		setUserRole(userRole);
		if (getUserRole() == UserRole.CONSUMER) {
			treeRepository = new ProjectTreeRepository();
		} else {
			treeRepository = new TreeRepository();
		}
	}

	/**
	 * Gets the trees.
	 *
	 * @param uid the uid
	 * @return the trees
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public Set<Tree> getTrees(int uid) throws UnknownUserException,
			EntityException {
		Set<Tree> trees = new HashSet<Tree>();
		for (Tree tree : treeRepository.getTrees(uid)) {
			trees.add(tree);
		}
		return trees;
	}

	/**
	 * Adds the tree.
	 *
	 * @param uid the uid
	 * @param tree the tree
	 * @return the tree
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws CreationException the creation exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
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

	/**
	 * Adds the knowledge tree to project tree.
	 *
	 * @param ktreeId the ktree id
	 * @param ptreeId the ptree id
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws CreationException the creation exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 */
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
		Map<Integer, Integer> directoryMapping = addDirectories(ktreeId,
				ptreeId, dm, pdr);
		addNodes(ktreeId, ptreeId, linkMapping, nm, pnr, directoryMapping);
		addSubnodes(ktreeId, linkMapping, subnodeMapping, snm, psr);
		addNodeLinks(ktreeId, ptreeId, linkMapping, subnodeMapping, nlm, pnlr);
	}

	/**
	 * Adds the node links.
	 *
	 * @param ktreeId the ktree id
	 * @param ptreeId the ptree id
	 * @param linkMapping the link mapping
	 * @param subnodeMapping the subnode mapping
	 * @param nlm the nlm
	 * @param pnlr the pnlr
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	private void addNodeLinks(int ktreeId, int ptreeId,
			Map<Integer, Integer> linkMapping,
			Map<Integer, Integer> subnodeMapping, NodeLinkManager nlm,
			ProjectNodeLinkRepository pnlr) throws EntityException,
			UnknownTreeException, UnknownProjectTreeException {
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

	/**
	 * Adds the subnodes to the tree.
	 *
	 * @param ktreeId the ktree id
	 * @param linkMapping the link mapping
	 * @param subnodeMapping the subnode mapping
	 * @param snm the snm
	 * @param psr the psr
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownNodeException the unknown node exception
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws CreationException the creation exception
	 */
	private void addSubnodes(int ktreeId, Map<Integer, Integer> linkMapping,
			Map<Integer, Integer> subnodeMapping, SubnodeManager snm,
			ProjectSubnodeRepository psr) throws EntityException,
			UnknownTreeException, UnknownNodeException,
			UnknownProjectNodeException, CreationException {
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
	}

	/**
	 * Adds the nodes to the tree.
	 *
	 * @param ktreeId the ktree id
	 * @param ptreeId the ptree id
	 * @param linkMapping the link mapping
	 * @param nm the nm
	 * @param pnr the pnr
	 * @param directoryMapping the directory mapping
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws CreationException the creation exception
	 */
	private void addNodes(int ktreeId, int ptreeId,
			Map<Integer, Integer> linkMapping, NodeManager nm,
			ProjectNodeRepository pnr, Map<Integer, Integer> directoryMapping)
			throws EntityException, UnknownTreeException,
			UnknownProjectTreeException, CreationException {
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
	}

	/**
	 * Adds the directories to the tree.
	 *
	 * @param ktreeId the ktree id
	 * @param ptreeId the ptree id
	 * @param dm the dm
	 * @param pdr the pdr
	 * @return the map
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 * @throws CreationException the creation exception
	 */
	private Map<Integer, Integer> addDirectories(int ktreeId, int ptreeId,
			DirectoryManager dm, ProjectDirectoryRepository pdr)
			throws EntityException, UnknownUserException, CreationException {
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
		return directoryMapping;
	}

	/**
	 * Delete tree.
	 *
	 * @param ktreeId the ktree id
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public void deleteTree(int ktreeId) throws UnknownTreeException,
			UnknownProjectTreeException {
		treeRepository.deleteTree(ktreeId);
	}

	/**
	 * Gets the tree.
	 *
	 * @param treeId the tree id
	 * @return the tree
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Tree getTree(int treeId) throws UnknownTreeException,
			UnknownProjectTreeException, EntityException {
		return treeRepository.getTree(treeId);
	}

	/**
	 * Update tree.
	 *
	 * @param tree the tree
	 * @return the tree
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
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

	/**
	 * Gets the user role.
	 *
	 * @return the user role
	 */
	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * Sets the user role.
	 *
	 * @param userRole the new user role
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	/**
	 * Update directory.
	 *
	 * @param tree the tree
	 * @throws EntityException the entity exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
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
