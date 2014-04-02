package cdar.junit.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeNodeDao;
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
		KnowledgeTreeDao tree = new KnowledgeTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
		tree.setName(newTreeName);
		tree.update();
		assertEquals(newTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	
	@Test
	public void TestGetTreeById() {
		KnowledgeTreeDao tree = new KnowledgeTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree = tree.create();
		assertEquals(testTreeName, kpdc.getTreeById(tree.getId()).getName());
	}
	 
	@Test
	public void TestDeleteTree() {
		final String treeName = "mytree";
		KnowledgeTreeDao tree = new KnowledgeTreeDao(udc.getUserByName(testUsername).getId(), treeName).create();
		assertEquals(treeName, kpdc.getTreeById(tree.getId()).getName());
		tree.delete();
		assertNull(kpdc.getTreeById(tree.getId()));
	}
	
	@Test
	public void testGetTrees() {
		int currentTreeCount = kpdc.getTrees().size();
		KnowledgeTreeDao tree = new KnowledgeTreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(currentTreeCount+1, kpdc.getTrees().size());
	}
	
	@Test
	public void testGetTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, kpdc.getTrees(user.getId()).size());
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(1, kpdc.getTrees(user.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateCreate() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao(tree.getId(), "MyTemplate");
		template.create();
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateUpdate() {
		final String templateName = "MyTemplate";
		final String updatedTemplateName = "MyTemplate2";
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao(tree.getId(), templateName);
		template.create();
		assertEquals(templateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
		template.setTitle(updatedTemplateName);
		template.update();
		assertEquals(updatedTemplateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
	}
	
	@Test
	public void TestKnowledgeTemplateDelete() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		KnowledgeTemplateDao template = new KnowledgeTemplateDao(tree.getId(), "MyTemplate");
		template.create();
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
		template.delete();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeNodeCreate() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		KnowledgeNodeDao node = new KnowledgeNodeDao(tree.getId(), "MyKnowledgeNode");
		node.create();
		assertEquals(1, kpdc.getNodes(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeNodeUpdate() {
		final String title = "MyNode";
		final String newTitle = "NewTitle";
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		KnowledgeNodeDao node = new KnowledgeNodeDao(tree.getId(), title);
		node.create();
		assertEquals(1, kpdc.getNodes(tree.getId()).size());
		assertEquals(title, kpdc.getNode(node.getId()).getTitle());
		assertEquals(0, kpdc.getNode(node.getId()).getDynamicTreeFlag());
		node.setTitle(newTitle);
		node.setDynamicTreeFlag(1);
		node.update();
		assertEquals(newTitle, kpdc.getNode(node.getId()).getTitle());
		assertEquals(1, kpdc.getNode(node.getId()).getDynamicTreeFlag());
	}
	
	@Test
	public  void TestKnowledgeNodeDelete() {
		UserDao user = udc.getUserByName(testUsername);
		KnowledgeTreeDao tree = new KnowledgeTreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		KnowledgeNodeDao node = new KnowledgeNodeDao(tree.getId(), "MyKnowledgeNode");
		node.create();
		assertEquals(1, kpdc.getNodes(tree.getId()).size());
		node.delete();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
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
	public void TestDirectoryCreate() {
		
	}
	
	@Test
	public void TestDirectoryUpdate() {
		
	}
	
	@Test
	public void TestDirectoryDelete() {
		
	}
	
	@Test
	public void TestDirectoryWithoutNode() {
		
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
