package cdar.junit.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeTemplateDao;
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
		udc.getUserByName(testUsername).delete();
	}

	@Test
	public void TestTreeNameChange() {
		final String newTreeName = "newTreeName";
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		tree.create(udc.getUserByName(testUsername).getId());
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
		tree.setName(newTreeName);
		tree.update();
		assertEquals(newTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	
	@Test
	public void TestGetTreeById() {
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		tree = tree.create(udc.getUserByName(testUsername).getId());
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	 
	@Test
	public void TestDeleteTree() {
		final String treeName = "mytree";
		
		KnowledgeTreeDao tree = new KnowledgeTreeDao(treeName).create(udc.getUserByName(testUsername).getId());
		assertEquals(treeName, kpdc.getTreeById(tree.getId()).getName());
		tree.delete();
		assertNull(kpdc.getTreeById(tree.getId()));
	}
	
	@Test
	public void testGetTrees() {
		int currentTreeCount = kpdc.getTrees().size();
		KnowledgeTreeDao tree = new KnowledgeTreeDao(testTreeName);
		tree.create(udc.getUserByName(testUsername).getId());
		assertEquals(currentTreeCount+1, kpdc.getTrees().size());
	}
	
	@Test
	public void testGetTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, kpdc.getTrees(user.getId()).size());
		KnowledgeTreeDao tree = new KnowledgeTreeDao("TestKnowledgeTree");
		tree.create(user.getId());
		assertEquals(1, kpdc.getTrees(user.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateCreate() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao("TestKnowledgeTree");
		tree.create(user.getId());
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao("MyTemplate");
		template.create(tree.getId());
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateUpdate() {
		final String templateName = "MyTemplate";
		final String updatedTemplateName = "MyTemplate2";
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao("TestKnowledgeTree");
		tree.create(user.getId());
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao(templateName);
		template.create(tree.getId());
		assertEquals(templateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
		template.setTitle(updatedTemplateName);
		template.update();
		assertEquals(updatedTemplateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
	}
	
	@Test
	public void TestKnowledgeTemplateDelete() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao("TestKnowledgeTree");
		tree.create(user.getId());
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao("MyTemplate");
		template.create(tree.getId());
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
		template.delete();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
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
