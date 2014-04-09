package cdar.dal.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeConsumerDaoController;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectNodeDao;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectTreeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;
import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class TestHibernateKnowledgeConsumer {
/*
	private UserDaoController udc = new UserDaoController();
	private KnowledgeConsumerDaoController kcdc = new KnowledgeConsumerDaoController();
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
	
	@Test
	public void TestUserKnowledgeTreesZeroEntries() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 0);
	}

	@Test
	public void TestKnowledgeTreesOneEntry() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 0);
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestProjectTree");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 1);
	}

	@Test
	public void TestKnowledgeTreesMultipleEntries() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 0);
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree1");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree2");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree3");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree4");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree5");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 5);
	}
	
	@Test
	public void TestKnowledgeTreeById() {
		String treeName = "TestTree1";
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(user.getKnowledgeProjectTrees().size(), 0);
		kcdc.addKnowledgeProjectTreeByUsername(user.getUsername(), treeName);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 1);
		int treeId = ((KnowledgeProjectTreeDao)udc.getUserByName(testUsername).getKnowledgeProjectTrees().toArray()[0]).getId();
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getName(),treeName);
	}
	
	@Test
	public void TestRemoveProjectTreeById() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree1");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree2");
		kcdc.addKnowledgeProjectTreeByUsername(testUsername, "TestTree3");
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 3);
		UserDao user = udc.getUserByName(testUsername);
		int treeId = ((KnowledgeProjectTreeDao)user.getKnowledgeProjectTrees().toArray()[2]).getId();
		kcdc.removeKnowledgeProjectTreeById(user.getId(), treeId);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 2);
		assertNull(kcdc.getKnowledgeProjectTreeById(treeId));
	}
	
	@Test
	public void TestKnowledgeProjectNode() {
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 0);
		kcdc.addKnowledgeProjectTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int treeId = ((KnowledgeProjectTreeDao)udc.getUserByName(testUsername).getKnowledgeProjectTrees().toArray()[0]).getId();
		assertEquals(udc.getUserByName(testUsername).getKnowledgeProjectTrees().size(), 1);
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getKnowledgeProjectNodes().size(), 0);
		kcdc.addKnowledgeProjectNode(treeId, "TestNode");
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getKnowledgeProjectNodes().size(), 1);
		KnowledgeProjectNodeDao knowledgeProjectNode = (KnowledgeProjectNodeDao) ((KnowledgeProjectTreeDao)kcdc.getKnowledgeProjectTreeById(treeId)).getKnowledgeProjectNodes().toArray()[0];
		kcdc.removeKnowledgeProjectNode(treeId, knowledgeProjectNode.getId());
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getKnowledgeProjectNodes().size(), 0);
	}
	
	
	
	@Test
	public void TestKnowledgeProjectSubNode() {
		int uid = udc.getUserByName(testUsername).getId();
		kcdc.addKnowledgeProjectTreeByUid(uid, "TestTree");
		int treeId = ((KnowledgeProjectTreeDao)udc.getUserByName(testUsername).getKnowledgeProjectTrees().toArray()[0]).getId();
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getKnowledgeProjectNodes().size(), 0);
		kcdc.addKnowledgeProjectNode(treeId, "TestNode");
		assertEquals(kcdc.getKnowledgeProjectTreeById(treeId).getKnowledgeProjectNodes().size(), 1);
		int nodeId = ((KnowledgeProjectNodeDao) ((KnowledgeProjectTreeDao)kcdc.getKnowledgeProjectTreeById(treeId)).getKnowledgeProjectNodes().toArray()[0]).getId();
		assertEquals(kcdc.getKnowledgeProjectNodeById(nodeId).getKnowledgeProjectSubNodes().size(), 0);
		kcdc.addKnowledgeProjectSubNode(nodeId, "SubNode1");
		assertEquals(kcdc.getKnowledgeProjectNodeById(nodeId).getKnowledgeProjectSubNodes().size(), 1);
	}
	
	//REMOVE KNOWLEDGE PROJECT SUB NODE
	
	@Test
	public void TestKnowledgeTreeCopy() {
		KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
		int uid = udc.getUserByName(testUsername).getId();
		kcdc.addKnowledgeProjectTreeByUid(uid, "TestTree");
		int projectTreeId = ((KnowledgeProjectTreeDao)udc.getUserByName(testUsername).getKnowledgeProjectTrees().toArray()[0]).getId();
		assertEquals(kcdc.getKnowledgeProjectTreeById(projectTreeId).getKnowledgeProjectNodes().size(), 0);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int knowledgeTreeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
		assertEquals(kpdc.getKnowledgeTreeById(knowledgeTreeId).getKnowledgeNodes().size(), 0);
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 1");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 2");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 3");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 4");
		assertEquals(kpdc.getKnowledgeTreeById(knowledgeTreeId).getKnowledgeNodes().size(), 4);
		kcdc.addKnowledgeTreeToProjectTree(knowledgeTreeId, projectTreeId);
		assertEquals(kcdc.getKnowledgeProjectTreeById(projectTreeId).getKnowledgeProjectNodes().size(), 4);
		kcdc.addKnowledgeTreeToProjectTree(knowledgeTreeId, projectTreeId);
		assertEquals(kcdc.getKnowledgeProjectTreeById(projectTreeId).getKnowledgeProjectNodes().size(), 8);
	}
	
	@Test
	public void TestProjectTreeDeletionAfterKnowledgeTreeCopy() {
		KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
		int uid = udc.getUserByName(testUsername).getId();
		kcdc.addKnowledgeProjectTreeByUid(uid, "TestTree");
		int projectTreeId = ((KnowledgeProjectTreeDao)udc.getUserByName(testUsername).getKnowledgeProjectTrees().toArray()[0]).getId();
		assertEquals(kcdc.getKnowledgeProjectTreeById(projectTreeId).getKnowledgeProjectNodes().size(), 0);
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 0);
		kpdc.addKnowledgeTreeByUid(udc.getUserByName(testUsername).getId(), "TestTree");
		int knowledgeTreeId = ((KnowledgeTreeDao)udc.getUserByName(testUsername).getKnowledgeTrees().toArray()[0]).getId();
		assertEquals(udc.getUserByName(testUsername).getKnowledgeTrees().size(), 1);
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 1");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 2");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 3");
		kpdc.addKnowledgeNode(knowledgeTreeId, "TestNode 4");
		assertEquals(kpdc.getKnowledgeTreeById(knowledgeTreeId).getKnowledgeNodes().size(), 4);
		kcdc.addKnowledgeTreeToProjectTree(knowledgeTreeId, projectTreeId);
		kcdc.addKnowledgeTreeToProjectTree(knowledgeTreeId, projectTreeId);
		assertEquals(kcdc.getKnowledgeProjectTreeById(projectTreeId).getKnowledgeProjectNodes().size(), 8);
		kcdc.removeKnowledgeProjectTreeById(uid, projectTreeId);
	}
	*/
}
