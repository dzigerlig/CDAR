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
import cdar.bll.consumer.models.CommentModel;
import cdar.bll.consumer.models.ProjectNodeLinkModel;
import cdar.bll.consumer.models.ProjectNodeModel;
import cdar.bll.consumer.models.ProjectSubnodeModel;
import cdar.bll.consumer.models.ProjectTreeModel;
import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Tree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.producer.models.TreeModel;
import cdar.bll.user.UserModel;
import cdar.dal.exceptions.UnknownUserException;

public class TestBLLKnowledgeConsumer {
	
	private UserModel um = new UserModel();
	private ProjectTreeModel ptm = new ProjectTreeModel();
	
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
	public void testProjectTree() throws UnknownUserException {
		final String treeName = "Project Tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getTitle());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}

	@Test
	public void testProjectTreeUpdate() throws UnknownUserException {
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
	public void testGetProjectTreesUnknownUserId() {
		assertEquals(0, ptm.getProjectTrees(unknownId).size());
	}
	
	@Test
	public void testGetUnknownProjectTree() {
		assertEquals(0, ptm.getProjectTrees(unknownId).size());
	}
	
	@Test
	public void testUpdateUnknownProjectTree() {
		ProjectTree tree = ptm.getProjectTree(unknownId);
		tree.setTitle("Unknown Tree");
		ProjectTree updatedTree = ptm.updateProjectTree(tree);
		assertEquals(-1, updatedTree.getId());
	}
	
	@Test
	public void testDeleteUnknownProjectTree() {
		assertFalse(ptm.deleteProjectTree(unknownId));
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeZeroEntries() throws Exception {
		NodeModel nm = new NodeModel();
		TreeModel tm = new TreeModel();
		ProjectNodeModel pnm = new ProjectNodeModel();
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
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		TreeModel tm = new TreeModel();
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		NodeLinkModel nlm = new NodeLinkModel();
		ProjectNodeModel pnm = new ProjectNodeModel();
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
		NodeModel nm = new NodeModel();
		SubnodeModel snm = new SubnodeModel();
		ProjectSubnodeModel psnm = new ProjectSubnodeModel();
		DirectoryModel dm = new DirectoryModel();
		TreeModel tm = new TreeModel();
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		NodeLinkModel nlm = new NodeLinkModel();
		ProjectNodeModel pnm = new ProjectNodeModel();
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
	public void testUnknownKnowledgeTreeToProjectTree() throws UnknownUserException, SQLException {
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree projectTree = ptm.addProjectTree(um.getUser(username).getId(), "My Project Tree");
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
		ptm.addKnowledgeTreeToProjectTree(unknownId, projectTree.getId());
		assertEquals(0, pnm.getProjectNodes(projectTree.getId()).size());
	}
	
	@Test
	public void testProjectNode() throws UnknownUserException {
		final String projectNodeName = "My project node";
		ProjectNodeModel pnm = new ProjectNodeModel();
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
	public void testProjectNodeUpdate() throws UnknownUserException {
		final String projectNodeName = "My project node";
		final String newProjectNodeName = "My new project node name";
		ProjectNodeModel pnm = new ProjectNodeModel();
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
	public void testGetProjectNodesUnknownProjectTree() {
		ProjectNodeModel pnm = new ProjectNodeModel();
		assertEquals(0, pnm.getProjectNodes(unknownId).size());
	}
	
	@Test
	public void testGetUnknownProjectNode() {
		ProjectNodeModel pnm = new ProjectNodeModel();
		assertEquals(-1, pnm.getProjectNode(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownProjectNode() {
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectNode projectNode = pnm.getProjectNode(unknownId);
		projectNode.setTitle("Unknown project node");
		ProjectNode updatedProjectNode = pnm.updateProjectNode(projectNode);
		assertEquals(-1, updatedProjectNode.getId());
	}
	
	@Test
	public void testDeleteUnknownProjectNode() {
		ProjectNodeModel pnm = new ProjectNodeModel();
		assertFalse(pnm.deleteProjectNode(unknownId));
	}
	
	@Test
	public void testProjectNodeLink() throws UnknownUserException {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
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
	public void testProjectNodeLinkUpdate() throws UnknownUserException {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
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
	public void testGetProjectNodeLinksUnknownProjectTree() {
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		assertEquals(0, pnlm.getProjectNodeLinks(unknownId).size());
	}
	
	@Test
	public void testGetUnknownProjectNodeLink() {
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		assertEquals(-1, pnlm.getProjectNodeLink(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownProjectNodeLink() {
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		ProjectNodeLink projectNodeLink = pnlm.getProjectNodeLink(unknownId);
		projectNodeLink.setSourceId(12);
		ProjectNodeLink updatedProjectNodeLink = pnlm.updateLink(projectNodeLink);
		assertEquals(-1, updatedProjectNodeLink.getId());
	}
	
	@Test
	public void testDeleteUnknownProjectNodeLink() {
		ProjectNodeLinkModel pnlm = new ProjectNodeLinkModel();
		assertFalse(pnlm.removeProjectNodeLink(unknownId));
	}
	
	@Test
	public void testProjectSubnode() throws UnknownUserException {
		final String projectSubnodeName = "My project subnode";
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		ProjectNodeModel pnm = new ProjectNodeModel();
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
	public void testProjectSubnodeUpdate() throws UnknownUserException {
		final String projectSubnodeName = "My project subnode";
		final String newProjectSubnodeName = "Another project subnode name";
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		ProjectNodeModel pnm = new ProjectNodeModel();
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
	public void testGetProjectSubnodesUnknownProjectNode() {
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		assertEquals(0, psm.getProjectSubnodesFromProjectNode(unknownId).size());
	}
	
	@Test
	public void testGetProjectSubnodesUnknownProjectTree() {
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		assertEquals(0, psm.getProjectSubnodesFromProjectTree(unknownId).size());
	}
	
	@Test
	public void testGetUnknownProjectSubnode() {
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		assertEquals(-1, psm.getProjectSubnode(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownProjectSubnode() {
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		ProjectSubnode projectSubnode = psm.getProjectSubnode(unknownId);
		projectSubnode.setTitle("My unknown project subnode");
		ProjectSubnode updatedProjectSubnode = psm.updateProjectSubnode(projectSubnode);
		assertEquals(-1, updatedProjectSubnode.getId());
	}
	
	@Test
	public void testDeleteUnknownProjectSubnode() {
		ProjectSubnodeModel psm = new ProjectSubnodeModel();
		assertFalse(psm.removeProjectSubnode(unknownId));
	}
	
	@Test
	public void testComment() throws UnknownUserException {
		final String commentString = "This is a comment";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		CommentModel cm = new CommentModel();
		assertEquals(0, cm.getComments(pnode.getId()).size());
		Comment comment = cm.addComment(um.getUser(username).getId(), pnode.getId(), commentString);
		assertEquals(1, cm.getComments(pnode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		cm.removeComment(comment.getId());
		assertEquals(0, cm.getComments(pnode.getId()).size());
	}
	
	@Test
	public void testCommentUpdate() throws UnknownUserException {
		final String commentString = "This is a comment";
		final String newCommentString = "This is another comment";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		CommentModel cm = new CommentModel();
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
	public void testGetCommentsUnknownProjectNode() {
		CommentModel cm = new CommentModel();
		assertEquals(0, cm.getComments(unknownId).size());
	}
	
	@Test
	public void testGetUnknownComment() {
		CommentModel cm = new CommentModel();
		assertEquals(-1, cm.getComment(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownComment() {
		CommentModel cm = new CommentModel();
		Comment comment = cm.getComment(unknownId);
		comment.setComment("Unknown comment");
		Comment updatedComment = cm.updateComment(comment);
		assertEquals(-1, updatedComment.getId());
	}
	
	@Test
	public void testDeleteUnknownComment() {
		CommentModel cm = new CommentModel();
		assertFalse(cm.removeComment(unknownId));
	}
}

