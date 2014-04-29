package cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.consumer.Comment;
import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectNodeLink;
import cdar.bll.consumer.ProjectSubnode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.consumer.managers.CommentManager;
import cdar.bll.consumer.managers.ProjectNodeLinkManager;
import cdar.bll.consumer.managers.ProjectNodeManager;
import cdar.bll.consumer.managers.ProjectSubnodeManager;
import cdar.bll.consumer.managers.ProjectTreeManager;
import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Tree;
import cdar.bll.producer.managers.DirectoryManager;
import cdar.bll.producer.managers.NodeLinkManager;
import cdar.bll.producer.managers.NodeManager;
import cdar.bll.producer.managers.SubnodeManager;
import cdar.bll.producer.managers.TreeManager;
import cdar.bll.user.UserManager;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class TestBLLKnowledgeConsumer {
	
	private UserManager um = new UserManager();
	private ProjectTreeManager ptm = new ProjectTreeManager();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	private final int unknownId = -13;
	
	@Before
	public void createUser() throws Exception {
		um.createUser(username, password);
	}
	
	@After
	public void deleteUser() throws UnknownUserException, Exception {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testProjectTree() throws Exception {
		final String treeName = "Project Tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getTitle());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}

	@Test
	public void testProjectTreeUpdate() throws Exception {
		final String treeName = "Project Tree";
		final String newTreeName = "My new project tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getTitle());
		tree.setTitle(newTreeName);
		ptm.updateProjectTree(tree);
		assertEquals(newTreeName, ptm.getProjectTree(tree.getId()).getTitle());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testGetProjectTreesUnknownUserId() throws SQLException {
		ptm.getProjectTrees(unknownId).size();
	}
	
	@Test
	public void testGetUnknownProjectTree() throws SQLException {
		ptm.getProjectTrees(unknownId).size();
	}
	
	@Test (expected = UnknownProjectTreeException.class)
	public void testUpdateUnknownProjectTree() throws Exception {
		ProjectTree tree = ptm.getProjectTree(unknownId);
		tree.setTitle("Unknown Tree");
		ptm.updateProjectTree(tree);
	}
	
	@Test
	public void testDeleteUnknownProjectTree() throws UnknownProjectTreeException {
		ptm.deleteProjectTree(unknownId);
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeZeroEntries() throws Exception {
		NodeManager nm = new NodeManager();
		TreeManager tm = new TreeManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "My Knowledge Tree");
		ProjectTree projectTree = ptm.addProjectTree(um.getUser(username).getId(), "My Project Tree");
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
		DirectoryManager dm = new DirectoryManager();
		TreeManager tm = new TreeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		NodeLinkManager nlm = new NodeLinkManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "My Knowledge Tree");
		ProjectTree projectTree = ptm.addProjectTree(um.getUser(username).getId(), "My Project Tree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node1 = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle1, directoryId);
		Node node2 = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle2, directoryId);
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node1.getId(), node2.getId(), 0);
		assertEquals(2, nm.getNodes(tree.getId()).size());
		assertEquals(node1.getId(), nlm.getNodeLink(nodelink.getId()).getSourceId());
		assertEquals(node2.getId(), nlm.getNodeLink(nodelink.getId()).getTargetId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(tree.getId(), projectTree.getId());
		assertEquals(2, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		int projectNodeLinkId = ((ProjectNodeLink)pnlm.getProjectNodeLinks(projectTree.getId()).toArray()[0]).getId();
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
		DirectoryManager dm = new DirectoryManager();
		TreeManager tm = new TreeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		NodeLinkManager nlm = new NodeLinkManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "My Knowledge Tree");
		ProjectTree projectTree = ptm.addProjectTree(um.getUser(username).getId(), "My Project Tree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node1 = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle1, directoryId);
		Node node2 = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle2, directoryId);
		snm.addSubnode(node1.getId(), subnodeTitle1);
		snm.addSubnode(node1.getId(), subnodeTitle2);
		snm.addSubnode(node2.getId(), subnodeTitle3);
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node1.getId(), node2.getId(), 0);
		assertEquals(2, nm.getNodes(tree.getId()).size());
		assertEquals(3, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(2, snm.getSubnodesFromNode(node1.getId()).size());
		assertEquals(1, snm.getSubnodesFromNode(node2.getId()).size());
		assertEquals(node1.getId(), nlm.getNodeLink(nodelink.getId()).getSourceId());
		assertEquals(node2.getId(), nlm.getNodeLink(nodelink.getId()).getTargetId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(0, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(tree.getId(), projectTree.getId());
		assertEquals(2, pnm.getProjectNodes(projectTree.getId()).size());
		assertEquals(1, pnlm.getProjectNodeLinks(projectTree.getId()).size());
		int projectNodeLinkId = ((ProjectNodeLink)pnlm.getProjectNodeLinks(projectTree.getId()).toArray()[0]).getId();
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
		ProjectTree projectTree = ptm.addProjectTree(um.getUser(username).getId(), "My Project Tree");
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(unknownId, projectTree.getId());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectNode() throws Exception {
		final String projectNodeName = "My project node";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
		ProjectNode node = pnm.addProjectNode(tree.getId(), projectNodeName);
		assertEquals(1, pnm.getProjectNodes(tree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(tree.getId(), pnm.getProjectNode(node.getId()).getRefProjectTreeId());
		pnm.deleteProjectNode(node.getId());
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
	}
	
	@Test
	public void testProjectNodeUpdate() throws Exception {
		final String projectNodeName = "My project node";
		final String newProjectNodeName = "My new project node name";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
		ProjectNode node = pnm.addProjectNode(tree.getId(), projectNodeName);
		assertEquals(1, pnm.getProjectNodes(tree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(tree.getId(), pnm.getProjectNode(node.getId()).getRefProjectTreeId());
		assertEquals(0, pnm.getProjectNode(node.getId()).getNodeStatus());
		node.setTitle(newProjectNodeName);
		node.setNodeStatus(2);
		pnm.updateProjectNode(node);
		assertEquals(newProjectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(2, pnm.getProjectNode(node.getId()).getNodeStatus());
		pnm.deleteProjectNode(node.getId());
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
	}
	
	@Test
	public void testGetProjectNodesUnknownProjectTree() throws UnknownProjectTreeException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.getProjectNodes(unknownId).size();
	}
	
	@Test (expected = UnknownProjectNodeException.class)
	public void testGetUnknownProjectNode() throws UnknownProjectNodeException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.getProjectNode(unknownId).getId();
	}
	
	@Test (expected = UnknownProjectNodeException.class)
	public void testUpdateUnknownProjectNode() throws UnknownProjectNodeException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNode projectNode = pnm.getProjectNode(unknownId);
		projectNode.setTitle("Unknown project node");
		pnm.updateProjectNode(projectNode);
	}
	
	@Test
	public void testDeleteUnknownProjectNode() throws UnknownProjectNodeException {
		ProjectNodeManager pnm = new ProjectNodeManager();
		pnm.deleteProjectNode(unknownId);
	}
	
	@Test
	public void testProjectNodeLink() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode projectNode1 = pnm.addProjectNode(tree.getId(), nameNode1);
		ProjectNode projectNode2 = pnm.addProjectNode(tree.getId(), nameNode2);
		assertEquals(0, pnlm.getProjectNodeLinks(tree.getId()).size());
		ProjectNodeLink projectnodelink = pnlm.addProjectNodeLink(tree.getId(), projectNode1.getId(), projectNode2.getId(), 0);
		assertEquals(1, pnlm.getProjectNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getTargetId()).getTitle());
		pnlm.removeProjectNodeLink(projectnodelink.getId());
		assertEquals(0, pnlm.getProjectNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void testProjectNodeLinkUpdate() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode projectNode1 = pnm.addProjectNode(tree.getId(), nameNode1);
		ProjectNode projectNode2 = pnm.addProjectNode(tree.getId(), nameNode2);
		assertEquals(0, pnlm.getProjectNodeLinks(tree.getId()).size());
		ProjectNodeLink projectnodelink = pnlm.addProjectNodeLink(tree.getId(), projectNode1.getId(), projectNode2.getId(), 0);
		assertEquals(1, pnlm.getProjectNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getTargetId()).getTitle());
		projectnodelink.setSourceId(projectNode2.getId());
		projectnodelink.setTargetId(projectNode1.getId());
		pnlm.updateLink(projectnodelink);
		assertEquals(nameNode2, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode1, pnm.getProjectNode(pnlm.getProjectNodeLink(projectnodelink.getId()).getTargetId()).getTitle());
		pnlm.removeProjectNodeLink(projectnodelink.getId());
		assertEquals(0, pnlm.getProjectNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void testGetProjectNodeLinksUnknownProjectTree() throws UnknownProjectTreeException {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.getProjectNodeLinks(unknownId);
	}
	
	@Test(expected = UnknownProjectNodeLinkException.class)
	public void testGetUnknownProjectNodeLink() throws UnknownProjectNodeLinkException {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.getProjectNodeLink(unknownId);
	}
	
	@Test(expected = UnknownProjectNodeLinkException.class)
	public void testUpdateUnknownProjectNodeLink() throws UnknownProjectNodeLinkException {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		ProjectNodeLink projectNodeLink = pnlm.getProjectNodeLink(unknownId);
		projectNodeLink.setSourceId(12);
		pnlm.updateLink(projectNodeLink);
	}
	
	@Test
	public void testDeleteUnknownProjectNodeLink() throws Exception {
		ProjectNodeLinkManager pnlm = new ProjectNodeLinkManager();
		pnlm.removeProjectNodeLink(unknownId);
	}
	
	@Test
	public void testProjectSubnode() throws Exception {
		final String projectSubnodeName = "My project subnode";
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
		ProjectSubnode subnode = psm.addProjectSubnode(pnode.getId(), projectSubnodeName);
		assertEquals(1, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(1, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
		assertEquals(projectSubnodeName, psm.getProjectSubnode(subnode.getId()).getTitle());
		psm.removeProjectSubnode(subnode.getId());
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
	}
	
	@Test
	public void testProjectSubnodeUpdate() throws Exception {
		final String projectSubnodeName = "My project subnode";
		final String newProjectSubnodeName = "Another project subnode name";
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
		ProjectSubnode subnode = psm.addProjectSubnode(pnode.getId(), projectSubnodeName);
		assertEquals(1, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(1, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
		assertEquals(projectSubnodeName, psm.getProjectSubnode(subnode.getId()).getTitle());
		subnode.setTitle(newProjectSubnodeName);
		psm.updateProjectSubnode(subnode);
		assertEquals(newProjectSubnodeName, psm.getProjectSubnode(subnode.getId()).getTitle());
		psm.removeProjectSubnode(subnode.getId());
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(pnode.getId()).size());
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(tree.getId()).size());
	}
	
	@Test
	public void testGetProjectSubnodesUnknownProjectNode() throws UnknownProjectNodeLinkException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.getProjectSubnodesFromProjectNode(unknownId);
	}
	
	@Test
	public void testGetProjectSubnodesUnknownProjectTree() throws UnknownProjectTreeException, UnknownProjectNodeLinkException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.getProjectSubnodesFromProjectTree(unknownId);
	}
	
	@Test(expected = UnknownProjectSubnodeException.class)
	public void testGetUnknownProjectSubnode() throws UnknownProjectSubnodeException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.getProjectSubnode(unknownId);
	}
	
	@Test(expected = UnknownProjectSubnodeException.class)
	public void testUpdateUnknownProjectSubnode() throws UnknownProjectSubnodeException, UnknownProjectNodeLinkException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		ProjectSubnode projectSubnode = psm.getProjectSubnode(unknownId);
		projectSubnode.setTitle("My unknown project subnode");
		psm.updateProjectSubnode(projectSubnode);
	}
	
	@Test
	public void testDeleteUnknownProjectSubnode() throws UnknownProjectSubnodeException {
		ProjectSubnodeManager psm = new ProjectSubnodeManager();
		psm.removeProjectSubnode(unknownId);
	}
	
	@Test
	public void testComment() throws Exception {
		final String commentString = "This is a comment";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(pnode.getId()).size());
		Comment comment = cm.addComment(um.getUser(username).getId(), pnode.getId(), commentString);
		assertEquals(1, cm.getComments(pnode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		cm.removeComment(comment.getId());
		assertEquals(0, cm.getComments(pnode.getId()).size());
	}
	
	@Test
	public void testCommentUpdate() throws Exception {
		final String commentString = "This is a comment";
		final String newCommentString = "This is another comment";
		ProjectNodeManager pnm = new ProjectNodeManager();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(pnode.getId()).size());
		Comment comment = cm.addComment(um.getUser(username).getId(), pnode.getId(), commentString);
		assertEquals(1, cm.getComments(pnode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		comment.setComment(newCommentString);
		cm.updateComment(comment);
		assertEquals(newCommentString, cm.getComment(comment.getId()).getComment());
		cm.removeComment(comment.getId());
		assertEquals(0, cm.getComments(pnode.getId()).size());
	}
	
	@Test
	public void testGetCommentsUnknownProjectNode() throws SQLException {
		CommentManager cm = new CommentManager();
		assertEquals(0, cm.getComments(unknownId).size());
	}
	
	@Test(expected = UnknownCommentException.class)
	public void testGetUnknownComment() throws UnknownCommentException {
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
	
	@Test
	public void testDeleteUnknownComment() throws Exception {
		CommentManager cm = new CommentManager();
		cm.removeComment(unknownId);
	}
}

