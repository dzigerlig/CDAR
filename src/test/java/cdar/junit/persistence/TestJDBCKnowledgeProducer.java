package cdar.junit.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeTreeDao;

public class TestJDBCKnowledgeProducer {
	private UserDaoController udc = new UserDaoController();
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
	private final String testUsername = "UnitTestUser";
	private final String testPassword = "UnitTestPassword";
	private final String testTreeName = "UnitTestTreeName";
	
	@Before
	public void createTestUser() {
		new UserDao(testUsername, testPassword).create();
	}
	
	@After
	public void deleteTestUser() {
		udc.deleteUser(udc.getUserByName(testUsername).getId());
	}

	@Test
	public void TestTreeNameChange() {
		final String newTreeName = "newTreeName";
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		kpdc.createTree(udc.getUserByName(testUsername).getId(), tree);
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
		tree.setName(newTreeName);
		kpdc.updateTree(tree);
		assertEquals(newTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	
	@Test
	public void TestGetTreeById() {
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		tree = kpdc.createTree(udc.getUserByName(testUsername).getId(), tree);
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	 
	@Test
	public void TestDeleteTree() {
		final String treeName = "mytree";
		KnowledgeTreeDao tree = kpdc.createTree(udc.getUserByName(testUsername).getId(), new KnowledgeTreeDao(treeName));
		assertEquals(treeName, kpdc.getTreeById(tree.getId()).getName());
		kpdc.deleteTree(tree.getId());
		assertNull(kpdc.getTreeById(tree.getId()));
	}
	
	@Test
	public void testGetTrees() {
		int currentTreeCount = kpdc.getTrees(0).size();
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		kpdc.createTree(udc.getUserByName(testUsername).getId(), tree);
		assertEquals(currentTreeCount+1, kpdc.getTrees(0).size());
	}
	
	@Test
	public void testGetTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, kpdc.getTrees(user.getId()).size());
		KnowledgeTreeDao tree = new KnowledgeTreeDao("TestKnowledgeTree");
		kpdc.createTree(user.getId(), tree);
		assertEquals(1, kpdc.getTrees(user.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateCreate() {
		
	}
	
	@Test
	public void TestKnowledgeTemplateUpdate() {
		
	}
	
	@Test
	public void TestKnowledgeTemplateDelete() {
		
	}
	
	@Test
	public void TestKnowledgeNodeCreate() {
		
	}
	
	@Test
	public void TestKnowledgeNodeUpdate() {
		
	}
	
	@Test
	public  void TestKnowledgeNodeDelete() {
		
	}
	
	@Test
	public void TestKnowledgeSubNodeCreate() {
		
	}
	
	@Test
	public void TestKnowledgeSubNodeUpdate() {
		
	}
	
	@Test
	public void TestKnowledgeSubNodeDelete() {
		
	}
	
	@Test
	public void TestLinkConnectionCreate() {
		
	}
	
	@Test
	public void TestLinkConnectionUpdate() {
		//?
	}
	
	@Test
	public void TestLinkConnectionDelete() {
		
	}
	
	@Test
	public void TestDictionaryCreate() {
		
	}
	
	@Test
	public void TestDictionaryUpdate() {
		
	}
	
	@Test
	public void TestDictionaryDelete() {
		
	}
	
	@Test
	public void TestDictionaryWithoutNode() {
		
	}
	
	@Test
	public void TestDropNode() {
		
	}
	
	@Test
	public void TestUndropNode() {
		
	}
	
	@Test
	public void TestNodeMoving() {
		
	}
}
