package cdar.bll.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeModel;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.TemplateModel;
import cdar.bll.producer.models.TreeModel;
import cdar.bll.user.UserModel;

public class TestBLLKnowledgeProducer {
	private UserModel um = new UserModel();
	private TreeModel tm = new TreeModel();
	
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
	public void testTree() {
		final String treeName = "MyTreeName";
		int treeCount = tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size();
		Tree tree = tm.addKnowledgeTreeByUid(um.getUser(username).getId(), treeName);
		assertEquals(treeCount+1, tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size());
		assertEquals(treeName, tm.getKnowledgeTree(tree.getId()).getName());
		tm.deleteKnowledgeTree(tree.getId());
		assertEquals(treeCount, tm.getKnowledgeTreesByUid(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testTemplate() {
		final String templateName = "MyTemplate";
		final String templateText = "MyTemplateText";
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addKnowledgeTreeByUid(um.getUser(username).getId(), "MyTree");
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
		Template template = tplm.addKnowledgeTemplate(tree.getId(), templateName, templateText);
		assertEquals(1, tplm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(templateName, tplm.getKnowledgeTemplate(template.getId()).getTitle());
		assertEquals(templateText, tplm.getKnowledgeTemplate(template.getId()).getTemplatetext());
		assertTrue(tplm.getKnowledgeTemplate(template.getId()).getTemplatetext().length() < tplm.getKnowledgeTemplate(template.getId()).getTemplatetexthtml().length());
		tplm.deleteTemplate(template.getId());
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
	}

	@Test
	public void testNode() {
		final String nodeTitle = "Node";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addKnowledgeTreeByUid(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		assertEquals(0, nm.getNodes(tree.getId()).size());
		Node node = nm.addNode(tree.getId(), nodeTitle, directoryId);
		assertEquals(1, nm.getNodes(tree.getId()).size());
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		assertEquals(directoryId, nm.getNode(node.getId()).getDid());
		assertEquals(0, nm.getNode(node.getId()).getDynamicTreeFlag());
		assertEquals(tree.getId(), nm.getNode(node.getId()).getRefTreeId());
	}
	
	@Test
	public void testUpdateNode() {
		final String nodeTitle = "Node";
		final String newNodeTitle = "MyNewTitle";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addKnowledgeTreeByUid(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nodeTitle, directoryId);
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		node.setTitle(newNodeTitle);
		nm.updateNode(node);
		assertEquals(newNodeTitle, nm.getNode(node.getId()).getTitle());
	}
}
