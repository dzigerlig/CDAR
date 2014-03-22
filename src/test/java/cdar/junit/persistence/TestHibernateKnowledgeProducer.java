package cdar.junit.persistence;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		//NODE STILL THERE!
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
	

	//TEST  NodeLink
	@Test
	public void TestKnowledgeNodeLink() {
//		Set<KnowledgeNodeLinkDao>  kl= kpdc.getKnowledgeTreeById(1).getKnowledgeNodeLinks();
//		assertEquals(4, kl.size());

	}
	
	
}
