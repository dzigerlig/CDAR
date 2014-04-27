package cdar.dal.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.consumer.CommentDao;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeLinkDao;
import cdar.dal.persistence.jdbc.consumer.ProjectSubnodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;
import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoRepository;

public class TestJDBCKnowledgeConsumer {
	private UserDaoRepository udc = new UserDaoRepository();
	private ConsumerDaoRepository cdc = new ConsumerDaoRepository();
	private final String testUsername = "UnitTestUser";
	private final String testPassword = "UnitTestPassword";
	private final String testTreeName = "UnitTestTreeName";
	 
	@Before
	public void createTestUser() {
		new UserDao(testUsername, testPassword).create();
	}
	
	@After
	public void deleteTestUser() {
		udc.getUserByName(testUsername).delete();
	}

	@Test
	public void TestProjectTreeNameChange() {
		final String newTreeName = "newTreeName";
		ProjectTreeDao projecttree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		projecttree.create();
		assertEquals(testTreeName, cdc.getProjectTree(projecttree.getId()).getTitle());
		projecttree.setTitle(newTreeName);
		projecttree.update();
		assertEquals(newTreeName, cdc.getProjectTree(projecttree.getId()).getTitle());
	}
	
	@Test
	public void TestGetProjectTreeById() {
		ProjectTreeDao projecttree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		projecttree = projecttree.create();
		assertEquals(testTreeName, cdc.getProjectTree(projecttree.getId()).getTitle());
	}
	 
	@Test
	public void TestDeleteProjectTree() {
		final String treeName = "mytree";
		ProjectTreeDao tree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), treeName).create();
		assertEquals(treeName, cdc.getProjectTree(tree.getId()).getTitle());
		tree.delete();
		assertEquals(-1, cdc.getProjectTree(tree.getId()).getId());
	}
	
	@Test
	public void testGetProjectTrees() {
		int currentTreeCount = cdc.getProjectTrees().size();
		ProjectTreeDao tree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(currentTreeCount+1, cdc.getProjectTrees().size());
	}
	
	@Test
	public void testGetProjectTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, cdc.getProjectTrees(user.getId()).size());
		ProjectTreeDao tree = new ProjectTreeDao(user.getId(), "TestProjectTree");
		tree.create();
		assertEquals(1, cdc.getProjectTrees(user.getId()).size());
	}
	
	@Test
	public void testProjectNodeCreate() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestProjectTree");
		projecttree.create();
		assertEquals(0, cdc.getProjectNodes(projecttree.getId()).size());
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode.create();
		assertEquals(1, cdc.getProjectNodes(projecttree.getId()).size());
	}
	
	@Test
	public void testProjectNodeUpdate() {
		final String title = "MyNode";
		final String newTitle = "NewTitle";
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestProjectTree");
		projecttree.create();
		assertEquals(0, cdc.getProjectNodes(projecttree.getId()).size());
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), title);
		projectnode.create();
		assertEquals(1, cdc.getProjectNodes(projecttree.getId()).size());
		assertEquals(title, cdc.getProjectNode(projectnode.getId()).getTitle());
		assertEquals(0, cdc.getProjectNode(projectnode.getId()).getNodestatus());
		projectnode.setTitle(newTitle);
		projectnode.setNodestatus(1);
		projectnode.update();
		assertEquals(newTitle, cdc.getProjectNode(projectnode.getId()).getTitle());
		assertEquals(1, cdc.getProjectNode(projectnode.getId()).getNodestatus());
	}
	
	@Test
	public void testProjectNodeDelete() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestProjectTree");
		projecttree.create();
		assertEquals(0, cdc.getProjectNodes(projecttree.getId()).size());
		ProjectNodeDao node = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		node.create();
		assertEquals(1, cdc.getProjectNodes(projecttree.getId()).size());
		node.delete();
		assertEquals(0, cdc.getProjectNodes(projecttree.getId()).size());
	}
	
	@Test
	public void testProjectNodeLinkCreate() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode.create();
		ProjectNodeDao projectnode2 = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode2.create();
		assertEquals(0, cdc.getProjectNodeLinks(projecttree.getId()).size());
		ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(projectnode.getId(), projectnode2.getId(), projecttree.getId());
		projectnodelink.create();
		assertEquals(projectnode.getId(), cdc.getProjectNodeLink(projectnodelink.getId()).getSourceid());
		assertEquals(projectnode2.getId(), cdc.getProjectNodeLink(projectnodelink.getId()).getTargetid());
		assertEquals(projecttree.getId(), cdc.getProjectNodeLink(projectnodelink.getId()).getKptid());
		assertEquals(1, cdc.getProjectNodeLinks(projecttree.getId()).size());
	}
	
	@Test
	public void testProjectNodeLinkUpdate() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode.create();
		ProjectNodeDao projectnode2 = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode2.create();
		ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(projectnode.getId(), projectnode2.getId(), projecttree.getId());
		projectnodelink.create();
		assertEquals(projectnode.getId(), projectnodelink.getSourceid());
		assertEquals(projectnode2.getId(), projectnodelink.getTargetid());
		projectnodelink.setTargetid(projectnode.getId());
		projectnodelink.setSourceid(projectnode2.getId());
		projectnodelink.update();
		assertEquals(projectnode2.getId(), projectnodelink.getSourceid());
		assertEquals(projectnode.getId(), projectnodelink.getTargetid());
	}
	
	@Test
	public void testProjectNodeLinkDelete() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode.create();
		ProjectNodeDao projectnode2 = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeNode");
		projectnode2.create();
		assertEquals(0, cdc.getProjectNodeLinks(projecttree.getId()).size());
		ProjectNodeLinkDao nodelink = new ProjectNodeLinkDao(projectnode.getId(), projectnode2.getId(), projecttree.getId());
		nodelink.create();
		assertEquals(1, cdc.getProjectNodeLinks(projecttree.getId()).size());
		nodelink.delete();
		assertEquals(0, cdc.getProjectNodeLinks(projecttree.getId()).size());
	}
	
	@Test
	public void testProjectSubNodeCreate() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeProjectTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeProjectNode");
		projectnode.create();
		assertEquals(0, cdc.getProjectSubnodes(projectnode.getId()).size());
		ProjectSubnodeDao projectsubnode = new ProjectSubnodeDao(projectnode.getId(), cdc.getNextProjectSubnodePosition(projectnode.getId()), "MyKnowledgeProjectSubNode");
		projectsubnode.create();
		assertEquals(1, cdc.getProjectSubnodes(projectnode.getId()).size());
	}
	
	@Test
	public void testProjectSubNodeUpdate() {
		final String subnodeTitle = "MyProjectubnode";
		final String newSubnodeTitle = "NewProjectSubnode";
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao tree = new ProjectTreeDao(user.getId(), "TestKnowledgeProkectTree");
		tree.create();
		ProjectNodeDao node = new ProjectNodeDao(tree.getId(), "MyKnowledgeProjectNode");
		node.create();
		assertEquals(0, cdc.getProjectSubnodes(node.getId()).size());
		ProjectSubnodeDao subnode = new ProjectSubnodeDao(node.getId(), cdc.getNextProjectSubnodePosition(node.getId()), subnodeTitle);
		subnode.create();
		assertEquals(1, cdc.getProjectSubnodes(node.getId()).size());
		assertEquals(subnodeTitle, cdc.getProjectSubnode(subnode.getId()).getTitle());
		assertEquals(1, cdc.getProjectSubnode(subnode.getId()).getPosition());
		subnode.setTitle(newSubnodeTitle);
		subnode.update();
		assertEquals(newSubnodeTitle, cdc.getProjectSubnode(subnode.getId()).getTitle());
	}
	
	@Test
	public void testProjectSubNodeDelete() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao tree = new ProjectTreeDao(user.getId(), "TestKnowledgeProjectTree");
		tree.create();
		ProjectNodeDao node = new ProjectNodeDao(tree.getId(), "MyKnowledgeProjectNode");
		node.create();
		assertEquals(0, cdc.getProjectSubnodes(node.getId()).size());
		ProjectSubnodeDao subnode = new ProjectSubnodeDao(node.getId(), cdc.getNextProjectSubnodePosition(node.getId()), "MyProjectSubNode");
		subnode.create();
		assertEquals(1, cdc.getProjectSubnodes(node.getId()).size());
		subnode.delete();
		assertEquals(0, cdc.getProjectSubnodes(node.getId()).size());
	}
	
	@Test
	public void testUserCommentCreate() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeProjectTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeProjectNode");
		projectnode.create();
		assertEquals(0, cdc.getComments(projectnode.getId()).size());
		CommentDao usercomment = new CommentDao(projectnode.getId(), user.getId(), "This is a comment");
		usercomment.create();
		assertEquals(1, cdc.getComments(projectnode.getId()).size());
	}
	
	@Test
	public void testUserCommentUpdate() {
		final String comment1 = "This is a comment";
		final String comment2 = "This is another comment";
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeProjectTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeProjectNode");
		projectnode.create();
		CommentDao usercomment = new CommentDao(projectnode.getId(), user.getId(), comment1);
		usercomment.create();
		assertEquals(comment1, cdc.getComment(usercomment.getId()).getComment());
		usercomment.setComment(comment2);
		usercomment.update();
		assertEquals(comment2, cdc.getComment(usercomment.getId()).getComment());
	}
	
	@Test
	public void testUserCommentDelete() {
		UserDao user = udc.getUserByName(testUsername);
		ProjectTreeDao projecttree = new ProjectTreeDao(user.getId(), "TestKnowledgeProjectTree");
		projecttree.create();
		ProjectNodeDao projectnode = new ProjectNodeDao(projecttree.getId(), "MyKnowledgeProjectNode");
		projectnode.create();
		assertEquals(0, cdc.getComments(projectnode.getId()).size());
		CommentDao usercomment = new CommentDao(projectnode.getId(), user.getId(), "This is a comment");
		usercomment.create();
		assertEquals(1, cdc.getComments(projectnode.getId()).size());
		usercomment.delete();
		assertEquals(0, cdc.getComments(projectnode.getId()).size());
	}
}
