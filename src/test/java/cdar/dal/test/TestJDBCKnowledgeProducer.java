package cdar.dal.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;
import cdar.dal.persistence.jdbc.producer.TemplateDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;
import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class TestJDBCKnowledgeProducer {
	private UserDaoController udc = new UserDaoController();
	private ProducerDaoController kpdc = new ProducerDaoController();
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
		TreeDao tree = new TreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(testTreeName, kpdc.getTree(tree.getId()).getTitle());
		tree.setTitle(newTreeName);
		tree.update();
		assertEquals(newTreeName, kpdc.getTree(tree.getId()).getTitle());
	}
	
	@Test
	public void TestGetTreeById() {
		TreeDao tree = new TreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree = tree.create();
		assertEquals(testTreeName, kpdc.getTree(tree.getId()).getTitle());
	}
	 
	@Test
	public void TestDeleteTree() {
		final String treeName = "mytree";
		TreeDao tree = new TreeDao(udc.getUserByName(testUsername).getId(), treeName).create();
		assertEquals(treeName, kpdc.getTree(tree.getId()).getTitle());
		tree.delete();
		assertEquals(-1, kpdc.getTree(tree.getId()).getId());
	}
	
	@Test
	public void testGetTrees() {
		int currentTreeCount = kpdc.getTrees().size();
		TreeDao tree = new TreeDao(udc.getUserByName(testUsername).getId(), testTreeName);
		tree.create();
		assertEquals(currentTreeCount+1, kpdc.getTrees().size());
	}
	
	@Test
	public void testGetTreesByUid() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(0, kpdc.getTrees(user.getId()).size());
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(1, kpdc.getTrees(user.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateCreate() {
		final String templateName = "MyTemplate";
		final String templateText = "MyText";
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		TemplateDao template = new TemplateDao(tree.getId(), templateName, templateText);
		template.create();
		assertEquals(templateName, kpdc.getTemplate(template.getId()).getTitle());
		assertEquals(templateText, kpdc.getTemplate(template.getId()).getTemplatetext());
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeTemplateUpdate() {
		final String templateName = "MyTemplate";
		final String updatedTemplateName = "MyTemplate2";
		final String templateText = "MyText";
		final String updatedTemplateText = "My New Text";
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		TemplateDao template = new TemplateDao(tree.getId(), templateName, templateText);
		template.create();
		assertEquals(templateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
		assertEquals(templateText, kpdc.getTemplates(tree.getId()).get(0).getTemplatetext());
		template.setTitle(updatedTemplateName);
		template.setTemplatetext(updatedTemplateText);
		template.update();
		assertEquals(updatedTemplateName, kpdc.getTemplates(tree.getId()).get(0).getTitle());
		assertEquals(updatedTemplateText, kpdc.getTemplates(tree.getId()).get(0).getTemplatetext());
	}
	
	@Test
	public void TestKnowledgeTemplateDelete() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
		TemplateDao template = new TemplateDao(tree.getId(), "MyTemplate", "TEXT");
		template.create();
		assertEquals(1, kpdc.getTemplates(tree.getId()).size());
		template.delete();
		assertEquals(0, kpdc.getTemplates(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeNodeCreate() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode", directory.getId());
		node.create();
		assertEquals(1, kpdc.getNodes(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeNodeUpdate() {
		final String title = "MyNode";
		final String newTitle = "NewTitle";
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		NodeDao node = new NodeDao(tree.getId(), title, directory.getId());
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
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();

		assertEquals(0, kpdc.getNodes(tree.getId()).size());
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode", directory.getId());
		node.create();
		assertEquals(1, kpdc.getNodes(tree.getId()).size());
		node.delete();
		assertEquals(0, kpdc.getNodes(tree.getId()).size());
	}
	
	@Test
	public void TestKnowledgeSubnodeCreate() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		assertEquals(0, kpdc.getSubnodes(node.getId()).size());
		SubnodeDao subnode = new SubnodeDao(node.getId(), kpdc.getNextSubnodePosition(node.getId()), "MyKnowledgeSubnode");
		subnode.create();
		SubnodeDao subnode2 = new SubnodeDao(node.getId(), kpdc.getNextSubnodePosition(node.getId()), "MyKnowledgeSubnode");
		subnode2.create();
		assertEquals(2, kpdc.getSubnodes(node.getId()).size());
		assertEquals(1, kpdc.getSubnode(subnode.getId()).getPosition());
		assertEquals(2, kpdc.getSubnode(subnode2.getId()).getPosition());
	}
	
	@Test
	public void TestKnowledgeSubnodeUpdate() {
		final String subnodeTitle = "MySubnode";
		final String newSubnodeTitle = "NewSubnode";
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		assertEquals(0, kpdc.getSubnodes(node.getId()).size());
		SubnodeDao subnode = new SubnodeDao(node.getId(), kpdc.getNextSubnodePosition(node.getId()), subnodeTitle);
		subnode.create();
		assertEquals(1, kpdc.getSubnodes(node.getId()).size());
		assertEquals(subnodeTitle, kpdc.getSubnode(subnode.getId()).getTitle());
		subnode.setTitle(newSubnodeTitle);
		subnode.update();
		assertEquals(newSubnodeTitle, kpdc.getSubnode(subnode.getId()).getTitle());
	}
	
	@Test
	public void TestKnowledgeSubnodeDelete() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		assertEquals(0, kpdc.getSubnodes(node.getId()).size());
		SubnodeDao subnode = new SubnodeDao(node.getId(), kpdc.getNextSubnodePosition(node.getId()), "MySubnode");
		subnode.create();
		assertEquals(1, kpdc.getSubnodes(node.getId()).size());
		assertEquals(1, kpdc.getSubnode(subnode.getId()).getPosition());
		subnode.delete();
		assertEquals(0, kpdc.getSubnodes(node.getId()).size());
	}
	
	@Test
	public void TestLinkConnectionCreate() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		NodeDao node2 = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node2.create();
		assertEquals(0, kpdc.getNodeLinks(tree.getId()).size());
		NodeLinkDao nodelink = new NodeLinkDao(node.getId(), node2.getId(), tree.getId());
		nodelink.create();
		assertEquals(node.getId(), kpdc.getNodeLink(nodelink.getId()).getSourceid());
		assertEquals(node2.getId(), kpdc.getNodeLink(nodelink.getId()).getTargetid());
		assertEquals(tree.getId(), kpdc.getNodeLink(nodelink.getId()).getKtrid());
		assertEquals(1, kpdc.getNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void TestLinkConnectionUpdate() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		NodeDao node2 = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node2.create();
		NodeLinkDao nodelink = new NodeLinkDao(node.getId(), node2.getId(), tree.getId());
		nodelink.create();
		assertEquals(node.getId(), nodelink.getSourceid());
		assertEquals(node2.getId(), nodelink.getTargetid());
		nodelink.setTargetid(node.getId());
		nodelink.setSourceid(node2.getId());
		nodelink.update();
		assertEquals(node2.getId(), nodelink.getSourceid());
		assertEquals(node.getId(), nodelink.getTargetid());
	}
	
	@Test
	public void TestLinkConnectionDelete() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("TestDirectory");
		directory.create();
		NodeDao node = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node.create();
		NodeDao node2 = new NodeDao(tree.getId(), "MyKnowledgeNode",directory.getId());
		node2.create();
		assertEquals(0, kpdc.getNodeLinks(tree.getId()).size());
		NodeLinkDao nodelink = new NodeLinkDao(node.getId(), node2.getId(), tree.getId());
		nodelink.create();
		assertEquals(1, kpdc.getNodeLinks(tree.getId()).size());
		nodelink.delete();
		assertEquals(0, kpdc.getNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void TestDirectoryUpdate() {
		final String directoryName = "MyDirectory";
		final String newDirectoryName = "MyNewDirectory";
		
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getDirectories(tree.getId()).size());
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle(directoryName);
		directory.create();
		assertEquals(directoryName, kpdc.getDirectory(directory.getId()).getTitle());
		directory.setTitle(newDirectoryName);
		directory.update();
		assertEquals(newDirectoryName, kpdc.getDirectory(directory.getId()).getTitle());
	}
	
	@Test
	public void TestDirectoryDelete() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getDirectories(tree.getId()).size());
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("MyDirectory");
		directory.create();
		assertEquals(1, kpdc.getDirectories(tree.getId()).size());
		directory.delete();
		assertEquals(0, kpdc.getDirectories(tree.getId()).size());
	}
	
	@Test
	public void TestDirectoryWithoutNode() {
		UserDao user = udc.getUserByName(testUsername);
		TreeDao tree = new TreeDao(user.getId(), "TestKnowledgeTree");
		tree.create();
		assertEquals(0, kpdc.getDirectories(tree.getId()).size());
		DirectoryDao directory = new DirectoryDao(tree.getId());
		directory.setTitle("MyDirectory");
		directory.create();
		assertEquals(1, kpdc.getDirectories(tree.getId()).size());
	}
	
	@Test
	public void TestDirectoryCreateWithNode() {
		
	}
}
