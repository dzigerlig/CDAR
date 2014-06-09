package ch.cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.NodeLink;
import ch.cdar.bll.entity.Subnode;
import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.entity.consumer.Comment;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.bll.manager.consumer.CommentManager;
import ch.cdar.bll.manager.consumer.ProjectNodeLinkManager;
import ch.cdar.bll.manager.consumer.ProjectNodeManager;
import ch.cdar.bll.manager.consumer.ProjectSubnodeManager;
import ch.cdar.bll.manager.producer.NodeLinkManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.manager.producer.SubnodeManager;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownCommentException;
import ch.cdar.dal.exception.UnknownProjectNodeException;
import ch.cdar.dal.exception.UnknownProjectNodeLinkException;
import ch.cdar.dal.exception.UnknownProjectSubnodeException;
import ch.cdar.dal.exception.UnknownProjectTreeException;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.repository.consumer.ProjectDirectoryRepository;

public class TestBLLKnowledgeConsumer {
	
	private UserManager um = new UserManager();
	private TreeManager ptm = new TreeManager(UserRole.CONSUMER);
	private ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	private final int unknownId = -13;
	private int uid;
	
	@Before
	public void createUser() throws Exception {
		User user = um.createUser(new User(username, password), false);
		uid=user.getId();
	}
	
	@After
	public void deleteUser() throws UnknownUserException, Exception {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testProjectTree() throws Exception {
		final String treeName = "Project Tree";
		int projectTreeCount = ptm.getTrees(um.getUser(username).getId()).size();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle(treeName);
		projectTree = ptm.addTree(uid,projectTree);
		assertEquals(projectTreeCount + 1, ptm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getTree(projectTree.getId()).getTitle());
		ptm.deleteTree(projectTree.getId());
		assertEquals(projectTreeCount, ptm.getTrees(um.getUser(username).getId()).size());
	}

	@Test
	public void testProjectTreeUpdate() throws Exception {
		final String treeName = "Project Tree";
		final String newTreeName = "My new project tree";
		int projectTreeCount = ptm.getTrees(um.getUser(username).getId()).size();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle(treeName);
		projectTree = ptm.addTree(uid,projectTree);
		assertEquals(projectTreeCount + 1, ptm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getTree(projectTree.getId()).getTitle());
		projectTree.setTitle(newTreeName);
		ptm.updateTree(projectTree);
		assertEquals(newTreeName, ptm.getTree(projectTree.getId()).getTitle());
		ptm.deleteTree(projectTree.getId());
		assertEquals(projectTreeCount, ptm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testGetProjectTreesUnknownUserId() throws UnknownUserException, EntityException {
		ptm.getTrees(unknownId);
	}
	
	@Test
	public void testGetUnknownProjectTree() throws Exception {
		ptm.getTrees(unknownId).size();
	}
	
	@Test (expected = UnknownProjectTreeException.class)
	public void testUpdateUnknownProjectTree() throws Exception {
		Tree tree = ptm.getTree(unknownId);
		tree.setTitle("Unknown Tree");
		ptm.updateTree(tree);
	}
	
	@Test (expected = UnknownProjectTreeException.class)
	public void testDeleteUnknownProjectTree() throws Exception {
		ptm.deleteTree(unknownId);
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeZeroEntries() throws Exception {
		NodeManager nm = new NodeManager();
		TreeManager tm = new TreeManager(UserRole.CONSUMER);
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = new Tree();
		tree.setTitle("My Knowledge Tree");
		tree = tm.addTree(um.getUser(username).getId(), tree);
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		assertEquals(0, nm.getNodes(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(tree.getId(), projectTree.getId());
		assertEquals(0, nm.getNodes(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeWithoutSubnodes() throws Exception {
		final String nodeTitle1 = "MyNode 1";
		final String nodeTitle2 = "MyNode 2";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		TreeManager tm = new TreeManager(UserRole.PRODUCER);
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		NodeLinkManager nlm = new NodeLinkManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = new Tree();
		tree.setTitle("My Knowledge Tree");
		tree = tm.addTree(um.getUser(username).getId(), tree);
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node1 = new Node();
		node1.setTreeId(tree.getId());
		node1.setTitle(nodeTitle1);
		node1.setDirectoryId(directoryId);
		Node node2 = new Node();
		node2.setTreeId(tree.getId());
		node2.setTitle(nodeTitle2);
		node2.setDirectoryId(directoryId);
		node1 = nm.addNode(um.getUser(username).getId(), node1, null);
		node2 = nm.addNode(um.getUser(username).getId(), node2, null);
		NodeLink nodeLink = new NodeLink();
		nodeLink.setTreeId(tree.getId());
		nodeLink.setSourceId(node1.getId());
		nodeLink.setTargetId(node2.getId());
		nodeLink.setSubnodeId(0);
		nodeLink = nlm.addNodeLink(nodeLink);
		assertEquals(2, nm.getNodes(tree.getId()).size());
		assertEquals(node1.getId(), nlm.getNodeLink(nodeLink.getId()).getSourceId());
		assertEquals(node2.getId(), nlm.getNodeLink(nodeLink.getId()).getTargetId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(tree.getId(), projectTree.getId());
		assertEquals(2, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		int projectNodeLinkId = ((NodeLink)pnlm.getProjectNodeLinks(projectTree.getId()).toArray()[0]).getId();
		int projectNodeId1 = pnlm.getProjectNodeLink(projectNodeLinkId).getSourceId();
		int projectNodeId2 = pnlm.getProjectNodeLink(projectNodeLinkId).getTargetId();
		assertEquals(nodeTitle1, pnm.getProjectNode(projectNodeId1).getTitle());
		assertEquals(nodeTitle2, pnm.getProjectNode(projectNodeId2).getTitle());
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeComplete() throws Exception {
		final String nodeTitle1 = "My Node 1";
		final String nodeTitle2 = "My Node 2";
		final String subnodeTitle1 = "My Subnode 1";
		final String subnodeTitle2 = "My Subnode 2";
		final String subnodeTitle3 = "My Subnode 3";
		NodeManager nm = new NodeManager();
		SubnodeManager snm = new SubnodeManager();
		ProjectSubnodeManager psnm = new ProjectSubnodeManager();
		DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
		TreeManager tm = new TreeManager(UserRole.PRODUCER);
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		NodeLinkManager nlm = new NodeLinkManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = new Tree();
		tree.setTitle("My Knowledge Tree");
		tree = tm.addTree(um.getUser(username).getId(), tree);
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid,projectTree);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node1 = new Node();
		node1.setTreeId(tree.getId());
		node1.setTitle(nodeTitle1);
		node1.setDirectoryId(directoryId);
		Node node2 = new Node();
		node2.setTreeId(tree.getId());
		node2.setTitle(nodeTitle2);
		node2.setDirectoryId(directoryId);
		node1 = nm.addNode(um.getUser(username).getId(), node1, null);
		node2 = nm.addNode(um.getUser(username).getId(), node2, null);
		Subnode subnode1 = new Subnode();
		subnode1.setNodeId(node1.getId());
		subnode1.setTitle(subnodeTitle1);
		Subnode subnode2 = new Subnode();
		subnode2.setNodeId(node1.getId());
		subnode2.setTitle(subnodeTitle2);
		Subnode subnode3 = new Subnode();
		subnode3.setNodeId(node2.getId());
		subnode3.setTitle(subnodeTitle3);
		snm.addSubnode(uid,tree.getId(),subnode1, null, null);
		snm.addSubnode(uid,tree.getId(),subnode2, null, null);
		snm.addSubnode(uid,tree.getId(),subnode3, null, null);
		NodeLink nodeLink = new NodeLink();
		nodeLink.setTreeId(tree.getId());
		nodeLink.setSourceId(node1.getId());
		nodeLink.setTargetId(node2.getId());
		nodeLink.setSubnodeId(0);
		nodeLink = nlm.addNodeLink(nodeLink);
		assertEquals(2, nm.getNodes(tree.getId()).size());
		assertEquals(3, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(2, snm.getSubnodesFromNode(node1.getId()).size());
		assertEquals(1, snm.getSubnodesFromNode(node2.getId()).size());
		assertEquals(node1.getId(), nlm.getNodeLink(nodeLink.getId()).getSourceId());
		assertEquals(node2.getId(), nlm.getNodeLink(nodeLink.getId()).getTargetId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(tree.getId(), projectTree.getId());
		assertEquals(2, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		int projectNodeLinkId = ((NodeLink)pnlm.getProjectNodeLinks(projectTree.getId()).toArray()[0]).getId();
		int projectNodeId1 = pnlm.getProjectNodeLink(projectNodeLinkId).getSourceId();
		int projectNodeId2 = pnlm.getProjectNodeLink(projectNodeLinkId).getTargetId();
		assertEquals(nodeTitle1, pnm.getProjectNode(projectNodeId1).getTitle());
		assertEquals(nodeTitle2, pnm.getProjectNode(projectNodeId2).getTitle());
		assertEquals(3, psnm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
		assertEquals(2, psnm.getProjectSubnodesFromProjectNode(projectNodeId1).size());
		assertEquals(1, psnm.getProjectSubnodesFromProjectNode(projectNodeId2).size());
	}
	
	@Test
	public void testUnknownKnowledgeTreeToProjectTree() throws Exception {
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(unknownId, projectTree.getId());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectNode() throws Exception {
		final String projectNodeName = "My project node";
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle(projectNodeName);
		projectNode.setDirectoryId(directoryId);
		projectNode = pnm.addProjectNode(uid, projectNode, null);
		assertEquals(1, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(projectNode.getId()).getTitle());
		assertEquals(projectTree.getId(), pnm.getProjectNode(projectNode.getId()).getTreeId());
		assertEquals(0, pnm.getProjectNode(projectNode.getId()).getStatus());
		pnm.deleteProjectNode(projectNode.getId());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectNodeUpdate() throws Exception {
		final String projectNodeName = "My project node";
		final String newProjectNodeName = "My new project node name";
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle(projectNodeName);
		projectNode.setDirectoryId(directoryId);
		projectNode = pnm.addProjectNode(uid, projectNode, null);
		assertEquals(1, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(projectNode.getId()).getTitle());
		assertEquals(projectTree.getId(), pnm.getProjectNode(projectNode.getId()).getTreeId());
		assertEquals(0, pnm.getProjectNode(projectNode.getId()).getStatus());
		projectNode.setTitle(newProjectNodeName);
		projectNode.setStatus(2);
		pnm.updateProjectNode(um.getUser(username).getId(), projectNode);
		assertEquals(newProjectNodeName, pnm.getProjectNode(projectNode.getId()).getTitle());
		assertEquals(2, pnm.getProjectNode(projectNode.getId()).getStatus());
		pnm.deleteProjectNode(projectNode.getId());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testGetProjectNodesUnknownProjectTree() throws Exception {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.getProjectNodes(unknownId).size();
	}
	
	@Test (expected = UnknownProjectNodeException.class)
	public void testGetUnknownProjectNode() throws Exception {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.getProjectNode(unknownId).getId();
	}
	
	@Test (expected = UnknownProjectNodeException.class)
	public void testUpdateUnknownProjectNode() throws Exception {
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNode projectNode = pnm.getProjectNode(unknownId);
		projectNode.setTitle("Unknown project node");
		pnm.updateProjectNode(um.getUser(username).getId(), projectNode);
	}
	
	@Test (expected = UnknownProjectNodeException.class)
	public void testDeleteUnknownProjectNode() throws Exception {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.deleteProjectNode(unknownId);
	}
	
	@Test
	public void testProjectNodeLink() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode1 = new ProjectNode();
		projectNode1.setTreeId(projectTree.getId());
		projectNode1.setTitle(nameNode1);
		projectNode1.setDirectoryId(directoryId);
		ProjectNode projectNode2 = new ProjectNode();
		projectNode2.setTreeId(projectTree.getId());
		projectNode2.setTitle(nameNode2);
		projectNode2.setDirectoryId(directoryId);
		projectNode1 = pnm.addProjectNode(uid, projectNode1, null);
		projectNode2 = pnm.addProjectNode(uid, projectNode2, null);
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		NodeLink projectNodeLink = new NodeLink();
		projectNodeLink.setTreeId(projectTree.getId());
		projectNodeLink.setSourceId(projectNode1.getId());
		projectNodeLink.setTargetId(projectNode2.getId());
		projectNodeLink.setSubnodeId(0);
		projectNodeLink = pnlm.addProjectNodeLink(projectNodeLink);
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectNodeLink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectNodeLink.getId()).getTargetId()).getTitle());
		pnlm.deleteProjectNodeLink(projectNodeLink.getId());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectNodeLinkUpdate() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode1 = new ProjectNode();
		projectNode1.setTreeId(projectTree.getId());
		projectNode1.setTitle(nameNode1);
		projectNode1.setDirectoryId(directoryId);
		ProjectNode projectNode2 = new ProjectNode();
		projectNode2.setTreeId(projectTree.getId());
		projectNode2.setTitle(nameNode2);
		projectNode2.setDirectoryId(directoryId);
		projectNode1 = pnm.addProjectNode(uid, projectNode1, null);
		projectNode2 = pnm.addProjectNode(uid, projectNode2, null);
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		NodeLink projectNodeLink = new NodeLink();
		projectNodeLink.setTreeId(projectTree.getId());
		projectNodeLink.setSourceId(projectNode1.getId());
		projectNodeLink.setTargetId(projectNode2.getId());
		projectNodeLink.setSubnodeId(0);
		NodeLink projectnodelink = pnlm.addProjectNodeLink(projectNodeLink);
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getTargetId()).getTitle());
		projectnodelink.setSourceId(projectNode2.getId());
		projectnodelink.setTargetId(projectNode1.getId());
		pnlm.updateLink(projectnodelink);
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getTargetId()).getTitle());
		pnlm.deleteProjectNodeLink(projectnodelink.getId());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
	}
	
	@Test
	public void testGetProjectNodeLinksUnknownProjectTree() throws Exception {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.getProjectNodeLinks(unknownId);
	}
	
	@Test(expected = UnknownProjectNodeLinkException.class)
	public void testGetUnknownProjectNodeLink() throws Exception {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.getProjectNodeLink(unknownId);
	}
	
	@Test(expected = UnknownProjectNodeLinkException.class)
	public void testUpdateUnknownProjectNodeLink() throws Exception {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		NodeLink projectNodeLink = pnlm.getProjectNodeLink(unknownId);
		projectNodeLink.setSourceId(12);
		pnlm.updateLink(projectNodeLink);
	}
	
	@Test(expected = UnknownProjectNodeLinkException.class)
	public void testDeleteUnknownProjectNodeLink() throws Exception {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.deleteProjectNodeLink(unknownId);
	}
	
	@Test
	public void testProjectSubnode() throws Exception {
		final String projectSubnodeName = "My project subnode";
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid,projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle("node");
		projectNode.setDirectoryId(directoryId);
		projectNode = pnm.addProjectNode(uid, projectNode, null);
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(projectNode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
		ProjectSubnode projectSubnode = new ProjectSubnode();
		projectSubnode.setNodeId(projectNode.getId());
		projectSubnode.setTitle(projectSubnodeName);
		projectSubnode = psm.addProjectSubnode(uid,projectSubnode, null, null);
		assertEquals(1, psm.getProjectSubnodesFromProjectNode(projectNode.getId()).size());
		assertEquals(1, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
		assertEquals(projectSubnodeName, psm.getProjectSubnode(projectSubnode.getId()).getTitle());
		assertEquals(0, psm.getProjectSubnode(projectSubnode.getId()).getStatus());
		psm.deleteProjectSubnode(projectSubnode.getId());
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(projectNode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectSubnodeUpdate() throws Exception {
		final String projectSubnodeName = "My project subnode";
		final String newProjectSubnodeName = "Another project subnode name";
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle("node");
		projectNode.setDirectoryId(directoryId);
		ProjectNode pnode = pnm.addProjectNode(uid, projectNode, null);
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
		ProjectSubnode projectSubnode = new ProjectSubnode();
		projectSubnode.setNodeId(pnode.getId());
		projectSubnode.setTitle(projectSubnodeName);
		projectSubnode = psm.addProjectSubnode(uid, projectSubnode, null, null);
		assertEquals(1, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(1, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
		assertEquals(projectSubnodeName, psm.getProjectSubnode(projectSubnode.getId()).getTitle());
		assertEquals(0, psm.getProjectSubnode(projectSubnode.getId()).getStatus());
		projectSubnode.setTitle(newProjectSubnodeName);
		projectSubnode.setStatus(3);
		psm.updateProjectSubnode(projectSubnode);
		assertEquals(newProjectSubnodeName, psm.getProjectSubnode(projectSubnode.getId()).getTitle());
		assertEquals(3, psm.getProjectSubnode(projectSubnode.getId()).getStatus());
		psm.deleteProjectSubnode(projectSubnode.getId());
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(projectTree.getId()).size());
	}
	
	@Test
	public void testGetProjectSubnodesUnknownProjectNode() throws Exception {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(unknownId).size());
	}
	
	@Test
	public void testGetProjectSubnodesUnknownProjectTree() throws Exception {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.getProjectSubnodesFromProjectTree(unknownId);
	}
	
	@Test(expected = UnknownProjectSubnodeException.class)
	public void testGetUnknownProjectSubnode() throws Exception {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.getProjectSubnode(unknownId);
	}
	
	@Test(expected = UnknownProjectSubnodeException.class)
	public void testUpdateUnknownProjectSubnode() throws Exception {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectSubnode projectSubnode = psm.getProjectSubnode(unknownId);
		projectSubnode.setTitle("My unknown project subnode");
		psm.updateProjectSubnode(projectSubnode);
	}
	
	@Test (expected = UnknownProjectSubnodeException.class)
	public void testDeleteUnknownProjectSubnode() throws Exception {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.deleteProjectSubnode(unknownId);
	}
	
	@Test
	public void testComment() throws Exception {
		final String commentString = "This is a comment";
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle("node");
		projectNode.setDirectoryId(directoryId);
		projectNode = pnm.addProjectNode(uid, projectNode, null);
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(projectNode.getId()).size());
		Comment comment = new Comment();
		comment.setUserId(um.getUser(username).getId());
		comment.setNodeId(projectNode.getId());
		comment.setComment(commentString);
		comment = cm.addComment(comment);
		assertEquals(1, cm.getComments(projectNode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		cm.deleteComment(comment.getId());
		assertEquals(0, cm.getComments(projectNode.getId()).size());
	}
	
	@Test
	public void testCommentUpdate() throws Exception {
		final String commentString = "This is a comment";
		final String newCommentString = "This is another comment";
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree projectTree = new Tree();
		projectTree.setUserId(um.getUser(username).getId());
		projectTree.setTitle("My Project Tree");
		projectTree = ptm.addTree(uid, projectTree);
		int directoryId = ((Directory)pdr.getDirectories(projectTree.getId()).toArray()[0]).getId();
		ProjectNode projectNode = new ProjectNode();
		projectNode.setTreeId(projectTree.getId());
		projectNode.setTitle("node");
		projectNode.setDirectoryId(directoryId);
		projectNode = pnm.addProjectNode(uid, projectNode, null);
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(projectNode.getId()).size());
		Comment comment = new Comment();
		comment.setUserId(um.getUser(username).getId());
		comment.setNodeId(projectNode.getId());
		comment.setComment(commentString);
		comment = cm.addComment(comment);
		assertEquals(1, cm.getComments(projectNode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		comment.setComment(newCommentString);
		cm.updateComment(comment);
		assertEquals(newCommentString, cm.getComment(comment.getId()).getComment());
		cm.deleteComment(comment.getId());
		assertEquals(0, cm.getComments(projectNode.getId()).size());
	}
	
	@Test
	public void testGetCommentsUnknownProjectNode() throws EntityException {
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(unknownId).size());
	}
	
	@Test(expected = UnknownCommentException.class)
	public void testGetUnknownComment() throws Exception {
		CommentManager cm = new CommentManager();
		cm.getComment(unknownId).getId();
	}
	
	@Test(expected = UnknownCommentException.class)
	public void testUpdateUnknownComment() throws Exception {
		CommentManager cm = new CommentManager();
		Comment comment = cm.getComment(unknownId);
		comment.setComment("Unknown comment");
		Comment updatedComment = cm.updateComment(comment);
		updatedComment.getId();
	}
	
	@Test(expected = UnknownCommentException.class)
	public void testDeleteUnknownComment() throws UnknownCommentException {
		CommentManager cm = new CommentManager();
		cm.deleteComment(unknownId);
	}
}

