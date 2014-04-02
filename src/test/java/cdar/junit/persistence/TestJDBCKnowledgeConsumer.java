package cdar.junit.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectNodeDao;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;
import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class TestJDBCKnowledgeConsumer {
	private UserDaoController udc = new UserDaoController();
	private ConsumerDaoController cdc = new ConsumerDaoController();
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
		assertEquals(testTreeName, cdc.getProjectTreeById(projecttree.getId()).getName());
		projecttree.setName(newTreeName);
		projecttree.update();
		assertEquals(newTreeName, cdc.getProjectTreeById(projecttree.getId()).getName());
	}
	
	@Test
	public void TestGetProjectTreeById() {
		ProjectTreeDao projecttree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		projecttree = projecttree.create();
		assertEquals(testTreeName, cdc.getProjectTreeById(projecttree.getId()).getName());
	}
	 
	@Test
	public void TestDeleteProjectTree() {
		final String treeName = "mytree";
		ProjectTreeDao tree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), treeName).create();
		assertEquals(treeName, cdc.getProjectTreeById(tree.getId()).getName());
		tree.delete();
		assertNull(cdc.getProjectTreeById(tree.getId()));
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
		
	}
	
	@Test
	public void testProjectNodeLinkUpdate() {
		
	}
	
	@Test
	public void testProjectNodeLinkDelete() {
		
	}
	
	@Test
	public void testProjectSubNodeCreate() {
		
	}
	
	@Test
	public void testProjectSubNodeUpdate() {
		
	}
	
	@Test
	public void testProjectSubNodeDelete() {
		
	}
	
	@Test
	public void testUserCommentCreate() {
		
	}
	
	@Test
	public void testUserCommentUpdate() {
		
	}
	
	@Test
	public void testUserCommentDelete() {
		
	}
}
