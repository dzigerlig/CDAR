package cdar.bll.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.consumer.ProjectTree;
import cdar.bll.consumer.models.ProjectTreeModel;
import cdar.bll.user.UserModel;

public class TestBLLKnowledgeConsumer {
	
	private UserModel um = new UserModel();
	private ProjectTreeModel ptm = new ProjectTreeModel();
	
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
	public void testProjectTree() {
		final String treeName = "Project Tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getName());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}

	@Test
	public void testProjectTreeUpdate() {
		final String treeName = "Project Tree";
		final String newTreeName = "My new project tree";
		int projectTreeCount = ptm.getProjectTrees(um.getUser(username).getId()).size();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), treeName);
		assertEquals(projectTreeCount + 1, ptm.getProjectTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, ptm.getProjectTree(tree.getId()).getName());
		tree.setName(newTreeName);
		ptm.updateProjectTree(tree);
		assertEquals(newTreeName, ptm.getProjectTree(tree.getId()).getName());
		ptm.deleteProjectTree(tree.getId());
		assertEquals(projectTreeCount, ptm.getProjectTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeZeroEntries() {
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		

	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeNodes() {
		
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeLinks() {
		
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeSubnodes() {
		
	}
	
	@Test
	public void testKnowledgeTreeToProjectTreeMultipleCopies() {
		
	}
}
