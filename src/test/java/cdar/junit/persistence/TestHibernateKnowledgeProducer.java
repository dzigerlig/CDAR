package cdar.junit.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeLinkDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTemplateDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;
import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class TestHibernateKnowledgeProducer {
	private UserDaoController udc = new UserDaoController();
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
	private String testUsername = "UnitTestUser";
	private final String testPassword = "UnitTestPassword";
	
	@Before
	public void createTestUser() {
		udc.createUser(testUsername, testPassword);
	}
	
	@After
	public void deleteTestUser() {
		udc.deleteUserById(udc.getUserByName(testUsername).getId());
	}
	
	// TEST USERS
	@Test
	public void TestGetUserList() {
		assertTrue(udc.getUsers().size()!=0);
	}

	@Test
	public void TestSingleUserCRUD() {
		String newPassword = "ThisIsANewPassword";
		assertTrue(udc.getUserByName(testUsername).getPassword().equals(testPassword));
		udc.updateUserPasswordById(udc.getUserByName(testUsername).getId(),newPassword);
		assertTrue(udc.getUserByName(testUsername).getPassword().equals(newPassword));
	}

	
	//TEST KNOWLEDGE TREE CRUD
	@Test
	public void TestUserKnowledgeTreesZeroEntries() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
	}

	@Test
	public void TestKnowledgeTreesOneEntry() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
	}

	@Test
	public void TestKnowledgeTreesMultipleEntries() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree1");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree2");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree3");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree4");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree5");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 5);
	}
	
	@Test
	public void TestKnowledgeTreeById() {
		String treeName = "TestTree1";
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(user.getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUsername(user.getUsername(), treeName);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getName(),treeName);
	}
	
	@Test
	public void TestRemoveTreeById() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree1");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree2");
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree3");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 3);
		UserDao user = udc.getUserByName(testUsername);
		int treeId = ((KnowledgeTreeDao)user.getKnowledgeTrees().toArray()[2]).getId();
		kpdc.removeKnowledgeTreeById(user.getId(), treeId);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 2);
		assertNull(kpdc.getKnowledgeTreeById(treeId));
	}
	
	
	//TEST KNOWLEDGE TREE TEMPLATE
	@Test
	public void TestKnowledgeTemplateOneEntry() {
		kpdc.addKnowledgeTreeByUsername(testUsername, "TestTree1");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeTemplates().size(), 0);
		kpdc.addKnowledgeTemplate(treeId, "Template1");
		KnowledgeTemplateDao knowledgeTemplate = (KnowledgeTemplateDao)kpdc.getKnowledgeTreeById(treeId).getKnowledgeTemplates().toArray()[0];
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeTemplates().size(), 1);
		kpdc.removeKnowledgeTemplate(treeId, knowledgeTemplate.getId());
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeTemplates().size(), 0);
	}
	
	//TEST KNOWLEDGE NODE
	@Test
	public void TestKnowledgeNode() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 0);
		kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 1);
		KnowledgeNodeDao knowledgeNode = (KnowledgeNodeDao) ((KnowledgeTreeDao)kpdc.getKnowledgeTreeById(treeId)).getKnowledgeNodes().toArray()[0];
		assertEquals(0, knowledgeNode.getDynamicTreeFlag());
		kpdc.removeKnowledgeNode(treeId, knowledgeNode.getId());
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 0);
	}
	
	@Test
	public void TestKnowledgeNodeWithoutRemove() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 0);
		kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 1);
	}
	
	//TEST KNOWLEDGE SUB-NODE
	@Test
	public void TestKnowledgeSubNode() {
		int uid = udc.getUserByName(testUsername).getId();
		kpdc.addKnowledgeTreeByUid(uid, "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(0, kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size());
		kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals(kpdc.getKnowledgeTreeById(treeId).getKnowledgeNodes().size(), 1);
		int nodeId = ((KnowledgeNodeDao) ((KnowledgeTreeDao)kpdc.getKnowledgeTreeById(treeId)).getKnowledgeNodes().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeNodeById(nodeId).getKnowledgeSubNodes().size(), 0);
		kpdc.addKnowledgeSubNode(nodeId, "SubNode1");
		assertEquals(kpdc.getKnowledgeNodeById(nodeId).getKnowledgeSubNodes().size(), 1);
	}
	
	//REMOVE KNOWLEDGE PROJECT SUB NODE
	
	@Test
	public void TestLinkConnection() {
		int uid = udc.getUserByName(testUsername).getId();
		kpdc.addKnowledgeTreeByUid(uid, "TestTree");
		int treeid = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size(), 0);
		kpdc.addKnowledgeNode(treeid, "TestNode 1");
		kpdc.addKnowledgeNode(treeid, "TestNode 2");
		assertEquals(kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size(), 2);
		int nodeid1 = ((KnowledgeNodeDao)kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().toArray()[0]).getId();
		int nodeid2 = ((KnowledgeNodeDao)kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().toArray()[1]).getId();
		
		KnowledgeNodeLinkDao knld = kpdc.addKnowledgeNodeLink(nodeid1, nodeid2, treeid);
		
		assertEquals(treeid, kpdc.getKnowledgeNodeLink(knld.getId()).getKnowledgeTree().getId());
	}
	
	@Test
	public void TestLinkConnectionRemove() {
		int uid = udc.getUserByName(testUsername).getId();
		kpdc.addKnowledgeTreeByUid(uid, "TestTree");
		int treeid = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size(), 0);
		kpdc.addKnowledgeNode(treeid, "TestNode 1");
		kpdc.addKnowledgeNode(treeid, "TestNode 2");
		assertEquals(kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size(), 2);
		int nodeid1 = ((KnowledgeNodeDao)kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().toArray()[0]).getId();
		int nodeid2 = ((KnowledgeNodeDao)kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().toArray()[1]).getId();
		
		KnowledgeNodeLinkDao knld = kpdc.addKnowledgeNodeLink(nodeid1, nodeid2, treeid);
		assertEquals(treeid, kpdc.getKnowledgeNodeLink(knld.getId()).getKnowledgeTree().getId());
		assertEquals(1, kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodeLinks().size());
		kpdc.removeKnowledgeNodeLink(knld.getId());
		assertEquals(0, kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodeLinks().size());
	}
	
	@Test
	public void TestDictionaryEntry() {
		int uid = udc.getUserByName(testUsername).getId();
		kpdc.addKnowledgeTreeByUid(uid, "TestTree");
		int treeid = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size(), 0);
		KnowledgeNodeDao myNode = kpdc.addKnowledgeNode(treeid, "TestNode 1");
		assertEquals(1, kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size());
		assertNull(myNode.getDictionary());
		DictionaryDao dic = kpdc.addDictionary(treeid, "TestDictionary", myNode.getId());
		assertNotNull(kpdc.getKnowledgeNodeById(myNode.getId()).getDictionary());
		assertEquals("TestDictionary", kpdc.getKnowledgeNodeById(myNode.getId()).getDictionary().getTitle());
		assertEquals("TestNode 1", ((KnowledgeNodeDao)kpdc.getDictionaryById(dic.getId()).getKnowledgeNodes().toArray()[0]).getTitle());
		assertEquals("TestTree", kpdc.getDictionaryById(dic.getId()).getKnowledgeTree().getName());
	}
	
	@Test
	public void TestDictionaryEntryWithoutNode() {
		int uid = udc.getUserByName(testUsername).getId();
		kpdc.addKnowledgeTreeByUid(uid, "TestTree");
		int treeid = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(0, kpdc.getKnowledgeTreeById(treeid).getKnowledgeNodes().size());
		DictionaryDao dic = kpdc.addDictionary(treeid, "My Dictionary Title");
		assertEquals(1, kpdc.getKnowledgeTreeById(treeid).getDictionaries().size());
		assertEquals(0, kpdc.getDictionaryById(dic.getId()).getKnowledgeNodes().size());
		assertEquals("TestTree", kpdc.getDictionaryById(dic.getId()).getKnowledgeTree().getName());
	}
	
	@Test
	public void TestDropNode() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		KnowledgeNodeDao node = kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals(0, node.getDynamicTreeFlag());
		node.setDynamicTreeFlag(1);
		kpdc.updateNode(node);
		assertEquals(1, kpdc.getKnowledgeNodeById(node.getId()).getDynamicTreeFlag());
	}
	
	@Test
	public void TestRenameNode() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		KnowledgeNodeDao node = kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals("TestNode", node.getTitle());
		node.setTitle("TestNode2");
		kpdc.updateNode(node);
		assertEquals("TestNode2", node.getTitle());
	}
	
	@Test
	public void TestUndropNode() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		KnowledgeNodeDao node = kpdc.addKnowledgeNode(treeId, "TestNode");
		assertEquals(0, node.getDynamicTreeFlag());
		node.setDynamicTreeFlag(1);
		kpdc.updateNode(node);
		assertEquals(1, kpdc.getKnowledgeNodeById(node.getId()).getDynamicTreeFlag());
		node.setDynamicTreeFlag(0);
		kpdc.updateNode(node);
		assertEquals(0, kpdc.getKnowledgeNodeById(node.getId()).getDynamicTreeFlag());
	}
	
	@Test
	public void TestMoveNode() {
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		KnowledgeNodeDao node = kpdc.addKnowledgeNode(treeId, "TestNode");
		DictionaryDao dic = kpdc.addDictionary(treeId, "TestDic1");
		DictionaryDao dic2 = kpdc.addDictionary(treeId, "TestDic2");
		DictionaryDao dic3 = kpdc.addDictionary(treeId, dic2.getId(), "TestDic3");
		assertNull(node.getDictionary());
		node.setDictionary(dic);
		kpdc.updateNode(node);
		assertEquals("TestDic1", kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
		node.setDictionary(dic2);
		kpdc.updateNode(node);
		assertEquals("TestDic2", kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
		node.setDictionary(dic3);
		kpdc.updateNode(node);
		assertEquals("TestDic3", kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
		node.setDictionary(dic);
		kpdc.updateNode(node);
		assertEquals("TestDic1", kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
		node.setDictionary(kpdc.getDictionaryById(dic2.getId()));
		kpdc.updateNode(node);
		assertEquals(dic2.getTitle(), kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
		KnowledgeNodeDao node4 = kpdc.getKnowledgeNodeById(node.getId());
		node4.setDictionary(kpdc.addDictionary(treeId, "ok"));
		kpdc.updateNode(node4);
		assertEquals("ok", kpdc.getKnowledgeNodeById(node.getId()).getDictionary().getTitle());
	}
	
	@Test
	public void TestExistingNodeMoving() {
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		KnowledgeNodeDao node = kpdc.addKnowledgeNode(treeId, "TestNode");
		DictionaryDao dic = kpdc.addDictionary(treeId, "TestDic1");
		DictionaryDao dic2 = kpdc.addDictionary(treeId, "TestDic2");
		assertNull(node.getDictionary());
		KnowledgeNodeDao node2 = kpdc.getKnowledgeNodeById(node.getId());
		node2.setDictionary(dic2);
		kpdc.updateNode(node2);
	}
}
