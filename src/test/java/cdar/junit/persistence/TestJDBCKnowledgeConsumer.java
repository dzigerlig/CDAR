package cdar.junit.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;
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
	public void TestTreeNameChange() {
		final String newTreeName = "newTreeName";
		ProjectTreeDao projecttree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		projecttree.create();
		assertEquals(testTreeName, cdc.getTreeById(projecttree.getId()).getName());
		projecttree.setName(newTreeName);
		projecttree.update();
		assertEquals(newTreeName, cdc.getTreeById(projecttree.getId()).getName());
	}
	
	@Test
	public void TestGetTreeById() {
		ProjectTreeDao projecttree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		projecttree = projecttree.create();
		assertEquals(testTreeName, cdc.getTreeById(projecttree.getId()).getName());
	}
	 
	@Test
	public void TestDeleteTree() {
		final String treeName = "mytree";
		ProjectTreeDao tree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), treeName).create();
		assertEquals(treeName, cdc.getTreeById(tree.getId()).getName());
		tree.delete();
		assertNull(cdc.getTreeById(tree.getId()));
	}
	
	@Test
	public void testGetTrees() {
		int currentTreeCount = cdc.getProjectTrees().size();
		ProjectTreeDao tree = new ProjectTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(currentTreeCount+1, cdc.getProjectTrees().size());
	}
	
	@Test
	public void testGetTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, cdc.getProjectTrees(user.getId()).size());
		ProjectTreeDao tree = new ProjectTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(1, cdc.getProjectTrees(user.getId()).size());
	}
}
