package cdar.bll.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.producer.Tree;
import cdar.bll.producer.TreeModel;
import cdar.bll.user.UserModel;

public class TestBLLKnowledgeProducer {
	private UserModel um = new UserModel();
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	@Before
	public void createUser() {
		um.createUser(username, password);
	}
	
	@After
	public void deleteUser() {
		um.deleteUser(um.getUser(username));
	}
	
	@Test
	public void testKnowledgeTree() {
		final String treename = "MyTreeName";
		TreeModel tm = new TreeModel();
		int treeCount = tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size();
		Tree tree = tm.addKnowledgeTreeByUid(um.getUser(username).getId(), treename);
		assertEquals(treeCount+1, tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size());
		assertEquals(treename, tm.getKnowledgeTree(tree.getId()).getName());
		tm.deleteKnowledgeTree(tree.getId());
		assertEquals(treeCount, tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size());
		
	}

}
