package cdar.bll.test;

import static org.junit.Assert.assertEquals;

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
import cdar.bll.producer.models.TreeModel;
import cdar.bll.user.UserModel;

public class TestBLLKnowledgeConsumer {
	
	private UserModel um = new UserModel();
	private ProjectTreeModel ptm = new ProjectTreeModel();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	@Before
	public void createUser() {
		um.createUser(username, password);
	}
	
	@After
	public void deleteUser() {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testProjectTree() {
		final String treeName = "Project Tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getName());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}

	@Test
	public void testProjectTreeUpdate() {
		final String treeName = "Project Tree";
		final String newTreeName = "My new project tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getName());
		tree.setName(newTreeName);
		ptm.updateProjectTree(tree);
		assertEquals(newTreeName, ptm.getProjectTree(tree.getId()).getName());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeZeroEntries() {
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
	public void testKnowledgeTreeToProjectTreeWithoutSubnodes() {
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
		Node node1 = nm.addNode(tree.getId(), nodeTitle1, directoryId);
		Node node2 = nm.addNode(tree.getId(), nodeTitle2, directoryId);
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
	public void testKnowledgeTreeToProjectTreeCompleteMultiple() {
		
	}
	
	@Test
	public void testProjectNode() {
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
	public void testProjectNodeUpdate() {
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
	public void testProjectNodeLink() {
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
	public void testProjectNodeLinkUpdate() {
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
	public void testProjectSubnode() {
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
	public void testProjectSubnodeUpdate() {
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
	public void testComment() {
		final String commentString = "This is a comment";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		ProjectNode pnode = pnm.addProjectNode(tree.getId(), "node");
		CommentModel cm = new CommentModel();
		assertEquals(0, cm.getComments(pnode.getId()).size());
		Comment comment = cm.addComment(um.getUser(username).getId(), pnode.getId(), commentString);
		assertEquals(1, cm.getComments(pnode.getId()).size());
		assertEquals(commentString, cm.getComment(comment.getId()).getComment());
		cm.deleteComment(comment.getId());
		assertEquals(0, cm.getComments(pnode.getId()).size());
	}
	
	@Test
	public void testCommentUpdate() {
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
		cm.deleteComment(comment.getId());
		assertEquals(0, cm.getComments(pnode.getId()).size());
	}
}

