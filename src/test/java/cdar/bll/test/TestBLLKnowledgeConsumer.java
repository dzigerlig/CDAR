package cdar.bll.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.consumer.ProjectNode;
import cdar.bll.consumer.ProjectTree;
import cdar.bll.consumer.models.ProjectNodeModel;
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
	
	@Test
	public void testProjectNode() {
		final String projectNodeName = "My project node";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
		ProjectNode node = pnm.addProjectNode(tree.getId(), projectNodeName);
		assertEquals(1, pnm.getProjectNodes(tree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(tree.getId(), pnm.getProjectNode(node.getId()).getRefProjectTreeId());
		pnm.deleteProjectNode(node.getId());
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
	}
	
	@Test
	public void testProjectNodeUpdate() {
		final String projectNodeName = "My project node";
		final String newProjectNodeName = "My new project node name";
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
		ProjectNode node = pnm.addProjectNode(tree.getId(), projectNodeName);
		assertEquals(1, pnm.getProjectNodes(tree.getId()).size());
		assertEquals(projectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(tree.getId(), pnm.getProjectNode(node.getId()).getRefProjectTreeId());
		assertEquals(0, pnm.getProjectNode(node.getId()).getNodeStatus());
		node.setTitle(newProjectNodeName);
		node.setNodeStatus(2);
		pnm.updateProjectNode(node);
		assertEquals(newProjectNodeName, pnm.getProjectNode(node.getId()).getTitle());
		assertEquals(2, pnm.getProjectNode(node.getId()).getNodeStatus());
		pnm.deleteProjectNode(node.getId());
		assertEquals(0, pnm.getProjectNodes(tree.getId()).size());
	}
	
	@Test
	public void testProjectNodeLink() {
		ProjectNodeModel pnm = new ProjectNodeModel();
		ProjectTree tree = ptm.addProjectTree(um.getUser(username).getId(), "Project Tree");
	}
	
	@Test
	public void testProjectNodeLinkUpdate() {
		
	}
	
	@Test
	public void testProjectSubnode() {
		
	}
	
	@Test
	public void testProjectSubnodeUpdate() {
		
	}
}

