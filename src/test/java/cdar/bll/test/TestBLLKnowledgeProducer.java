package cdar.bll.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
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
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testTree() {
		final String treeName = "MyTreeName";
		int treeCount = tm.getTrees(um.getUser(username).getId()).size();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		assertEquals(treeCount+1, tm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, tm.getTree(tree.getId()).getName());
		tm.deleteTree(tree.getId());
		assertEquals(treeCount, tm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testTreeUpdate() {
		final String treeName = "MyTreeName";
		final String newTreeName = "My new tree";
		
		int treeCount = tm.getTrees(um.getUser(username).getId()).size();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		assertEquals(treeCount+1, tm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, tm.getTree(tree.getId()).getName());
		tree.setName(newTreeName);
		tm.updateTree(tree);
		assertEquals(newTreeName, tm.getTree(tree.getId()).getName());
		tm.deleteTree(tree.getId());
		assertEquals(treeCount, tm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testTemplate() {
		final String templateName = "MyTemplate";
		final String templateText = "MyTemplateText";
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
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
	public void testTemplateUpdate() {
		final String templateName = "MyTemplate";
		final String newTemplateName = "My new template name";
		final String templateText = "MyTemplateText";
		final String newTemplateText = "My new template text";
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
		Template template = tplm.addKnowledgeTemplate(tree.getId(), templateName, templateText);
		assertEquals(1, tplm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(templateName, tplm.getKnowledgeTemplate(template.getId()).getTitle());
		assertEquals(templateText, tplm.getKnowledgeTemplate(template.getId()).getTemplatetext());
		assertTrue(tplm.getKnowledgeTemplate(template.getId()).getTemplatetext().length() < tplm.getKnowledgeTemplate(template.getId()).getTemplatetexthtml().length());
		template.setTitle(newTemplateName);
		template.setTemplatetext(newTemplateText);
		tplm.updateTemplate(template);
		assertEquals(newTemplateName, tplm.getKnowledgeTemplate(template.getId()).getTitle());
		assertEquals(newTemplateText, tplm.getKnowledgeTemplate(template.getId()).getTemplatetext());
		assertTrue(tplm.getKnowledgeTemplate(template.getId()).getTemplatetext().length() < tplm.getKnowledgeTemplate(template.getId()).getTemplatetexthtml().length());
		
		tplm.deleteTemplate(template.getId());
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
	}

	@Test
	public void testNode() {
		final String nodeTitle = "Node";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		assertEquals(0, nm.getNodes(tree.getId()).size());
		Node node = nm.addNode(tree.getId(), nodeTitle, directoryId);
		assertEquals(1, nm.getNodes(tree.getId()).size());
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		assertEquals(directoryId, nm.getNode(node.getId()).getDid());
		assertEquals(0, nm.getNode(node.getId()).getDynamicTreeFlag());
		assertEquals(tree.getId(), nm.getNode(node.getId()).getRefTreeId());
		nm.deleteNode(node.getId());
		assertEquals(0, nm.getNodes(tree.getId()).size());
	}
	
	@Test
	public void testUpdateNode() {
		final String nodeTitle = "Node";
		final String newNodeTitle = "MyNewTitle";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nodeTitle, directoryId);
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		node.setTitle(newNodeTitle);
		nm.updateNode(node);
		assertEquals(newNodeTitle, nm.getNode(node.getId()).getTitle());
	}
	
	@Test
	public void testNodeLink() {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		NodeLinkModel nlm = new NodeLinkModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nameNode1, directoryId);
		Node node2 = nm.addNode(tree.getId(), nameNode2, directoryId);
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node.getId(), node2.getId(), 0);
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, nm.getNode(nlm.getLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getLink(nodelink.getId()).getTargetId()).getTitle());
		nlm.removeNodeLink(nodelink.getId());
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void testNodeLinkUpdate() {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		NodeLinkModel nlm = new NodeLinkModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nameNode1, directoryId);
		Node node2 = nm.addNode(tree.getId(), nameNode2, directoryId);
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node.getId(), node2.getId(), 0);
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, nm.getNode(nlm.getLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getLink(nodelink.getId()).getTargetId()).getTitle());
		nodelink.setSourceId(node2.getId());
		nodelink.setTargetId(node.getId());
		nlm.updateLink(nodelink);
		assertEquals(nameNode2, nm.getNode(nlm.getLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode1, nm.getNode(nlm.getLink(nodelink.getId()).getTargetId()).getTitle());
	}
	
	@Test
	public void testSubnode() {
		final String subnodename = "My Subnode";
		SubnodeModel snm = new SubnodeModel();
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), "Node", directoryId);
		assertEquals(0, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
		Subnode subnode = snm.addSubnode(node.getId(), subnodename);
		assertEquals(1, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(1, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(subnodename, snm.getSubnode(subnode.getId()).getTitle());
		snm.removeSubnode(subnode.getId());
		assertEquals(0, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
	}
	
	@Test
	public void testSubnodeUpdate() {
		final String subnodename = "My Subnode";
		final String newSubnodename = "My New Subnode";
		SubnodeModel snm = new SubnodeModel();
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), "Node", directoryId);
		assertEquals(0, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
		Subnode subnode = snm.addSubnode(node.getId(), subnodename);
		assertEquals(1, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(1, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(subnodename, snm.getSubnode(subnode.getId()).getTitle());
		subnode.setTitle(newSubnodename);
		snm.updateSubnode(subnode);
		assertEquals(newSubnodename, snm.getSubnode(subnode.getId()).getTitle());
	}
	
	@Test
	public void testDirectory() {
		final String treeName = "MyTree";
		final String directoryName = "TestDirectory";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		nm.addNode(tree.getId(), "Node", directoryId);
		assertEquals(1, dm.getDirectories(tree.getId()).size());
		Directory newDirectory = dm.addDirectory(tree.getId(), directoryId, directoryName);
		assertEquals(treeName, dm.getDirectory(directoryId).getTitle());
		assertEquals(directoryName, dm.getDirectory(newDirectory.getId()).getTitle());
		dm.removeDirectoryById(newDirectory.getId());
		assertEquals(1, dm.getDirectories(tree.getId()).size());
	}
	
	@Test
	public void testDirectoryUpdate() {
		final String treeName = "MyTree";
		final String directoryName = "TestDirectory";
		final String newDirectoryName = "MyNewDirectory";
		NodeModel nm = new NodeModel();
		DirectoryModel dm = new DirectoryModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		nm.addNode(tree.getId(), "Node", directoryId);
		assertEquals(1, dm.getDirectories(tree.getId()).size());
		Directory newDirectory = dm.addDirectory(tree.getId(), directoryId, directoryName);
		assertEquals(treeName, dm.getDirectory(directoryId).getTitle());
		assertEquals(directoryName, dm.getDirectory(newDirectory.getId()).getTitle());
		newDirectory.setTitle(newDirectoryName);
		dm.updateDirectory(newDirectory);
		assertEquals(newDirectoryName, dm.getDirectory(newDirectory.getId()).getTitle());
	}
}
