package cdar.bll.integrationtest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.NodeLink;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.entity.XmlTree;
import cdar.bll.entity.producer.Template;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.manager.producer.NodeLinkManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.SubnodeManager;
import cdar.bll.manager.producer.TemplateManager;
import cdar.bll.manager.producer.TreeManager;
import cdar.bll.manager.producer.XmlTreeManager;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UnknownXmlTreeException;

public class TestBLLKnowledgeProducer {
	private UserManager um = new UserManager();
	private TreeManager tm = new TreeManager();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	private final int unknownId = -13;
	
	@Before
	public void createUser() throws Exception {
		um.createUser(new User(username, password));
	}
	
	@After
	public void deleteUser() throws UnknownUserException, Exception {
		um.deleteUser(um.getUser(username).getId());
	}

	@Test
	public void testTree() throws Exception {
		final String treeName = "MyTreeName";
		int treeCount = tm.getTrees(um.getUser(username).getId()).size();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		assertEquals(treeCount+1, tm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, tm.getTree(tree.getId()).getTitle());
		tm.deleteTree(tree.getId());
		assertEquals(treeCount, tm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testGetTreesUnknownUserId() throws SQLException {
		assertEquals(0, tm.getTrees(unknownId).size());
	}
	
	@Test(expected = UnknownTreeException.class)
	public void testGetUknownTree() throws Exception {
		tm.getTree(unknownId);
	}
	
	@Test(expected = UnknownUserException.class)
	public void testUpdateUnknownTree() throws Exception {
		Tree tree = tm.addTree(unknownId, "MyTree");
		tree.setTitle("My unknown tree");
		Tree updatedTree = tm.updateTree(tree);
		assertEquals(-1, updatedTree.getId());
	}
	
	@Test(expected = UnknownTreeException.class)
	public void testDeleteUnknownTree() throws Exception {
		tm.deleteTree(unknownId);
	}
	
	@Test
	public void testTreeUpdate() throws Exception {
		final String treeName = "MyTreeName";
		final String newTreeName = "My new tree";
		int treeCount = tm.getTrees(um.getUser(username).getId()).size();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		assertEquals(treeCount+1, tm.getTrees(um.getUser(username).getId()).size());
		assertEquals(treeName, tm.getTree(tree.getId()).getTitle());
		tree.setTitle(newTreeName);
		tm.updateTree(tree);
		assertEquals(newTreeName, tm.getTree(tree.getId()).getTitle());
		tm.deleteTree(tree.getId());
		assertEquals(treeCount, tm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testTemplate() throws Exception {
		final String templateName = "MyTemplate";
		final String templateText = "MyTemplateText";
		TemplateManager tplm = new TemplateManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
		Template template = tplm.addKnowledgeTemplate(tree.getId(), templateName, templateText, false);
		assertEquals(1, tplm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(templateName, tplm.getKnowledgeTemplate(template.getId()).getTitle());
		assertEquals(templateText, tplm.getKnowledgeTemplate(template.getId()).getTemplatetext());
		assertTrue(tplm.getKnowledgeTemplate(template.getId()).getTemplatetext().length() < tplm.getKnowledgeTemplate(template.getId()).getTemplatetexthtml().length());
		tplm.deleteTemplate(template.getId());
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
	}
	
	@Test
	public void testTemplateIsDefault() throws Exception {
		TemplateManager tplm = new TemplateManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text", false);
		assertFalse(template.getIsDefault());
	}
	
	@Test
	public void testTemplateDefaultChangeMultipleTemplates() throws Exception {
		TemplateManager tplm = new TemplateManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template1 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text", false);
		Template template2 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text", false);
		assertFalse(template1.getIsDefault());
		assertFalse(template2.getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeId(), template1.getId());
		assertTrue(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeId(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertTrue(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
	}
	
	@Test
	public void testTemplateDefaultChangeMultipleTemplatesNoDefault() throws Exception {
		TemplateManager tplm = new TemplateManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template1 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text", false);
		Template template2 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text", false);
		assertFalse(template1.getIsDefault());
		assertFalse(template2.getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeId(), template1.getId());
		assertTrue(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template2.getTreeId(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertTrue(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeId(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
	}
	
	@Test
	public void testTemplateUpdate() throws Exception {
		final String templateName = "MyTemplate";
		final String newTemplateName = "My new template name";
		final String templateText = "MyTemplateText";
		final String newTemplateText = "My new template text";
		TemplateManager tplm = new TemplateManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		assertEquals(0, tplm.getKnowledgeTemplates(tree.getId()).size());
		Template template = tplm.addKnowledgeTemplate(tree.getId(), templateName, templateText, false);
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
	public void testGetTemplatesUnknownTreeId() throws SQLException {
		TemplateManager tplm = new TemplateManager();
		assertEquals(0, tplm.getKnowledgeTemplates(unknownId).size());
	}
	
	@Test (expected = UnknownTemplateException.class)
	public void testGetUnknownTemplate() throws UnknownTemplateException {
		TemplateManager tplm = new TemplateManager();
		tplm.getKnowledgeTemplate(unknownId).getId();
	}
	
	@Test (expected = UnknownTemplateException.class)
	public void testUpdateUnknownTemplate() throws UnknownXmlTreeException, UnknownTemplateException {
		TemplateManager tplm = new TemplateManager();
		Template template = tplm.addKnowledgeTemplate(unknownId, "Template title", "Template text", false);
		template.setTitle("New title");
		tplm.updateTemplate(template);
	}
	
	@Test(expected = UnknownTemplateException.class)
	public void testDeleteUnknownTemplate() throws UnknownTemplateException {
		TemplateManager tplm = new TemplateManager();
		tplm.deleteTemplate(unknownId);
	}
	
	@Test
	public void testNode() throws Exception {
		final String nodeTitle = "Node";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		assertEquals(0, nm.getNodes(tree.getId()).size());
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle, directoryId);
		assertEquals(1, nm.getNodes(tree.getId()).size());
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		assertEquals(directoryId, nm.getNode(node.getId()).getDirectoryId());
		assertEquals(0, nm.getNode(node.getId()).getDynamicTreeFlag());
		assertEquals(tree.getId(), nm.getNode(node.getId()).getTreeId());
		nm.deleteNode(node.getId());
		assertEquals(0, nm.getNodes(tree.getId()).size());
	}
	
	@Test
	public void testUpdateNode() throws Exception {
		final String nodeTitle = "Node";
		final String newNodeTitle = "MyNewTitle";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle, directoryId);
		assertEquals(nodeTitle, nm.getNode(node.getId()).getTitle());
		node.setTitle(newNodeTitle);
		nm.updateNode(node);
		assertEquals(newNodeTitle, nm.getNode(node.getId()).getTitle());
	}
	
	@Test
	public void testGetNodesUnknownTreeId() throws SQLException {
		NodeManager nm = new NodeManager();
		assertEquals(0, nm.getNodes(unknownId).size());
	}
	
	@Test(expected = UnknownNodeException.class)
	public void testGetUnknownNode() throws UnknownNodeException {
		NodeManager nm = new NodeManager();
		nm.getNode(unknownId).getId();
	}
	
	@Test(expected = UnknownTreeException.class)
	public void testUpdateNodeUnknownTreeId() throws Exception {
		NodeManager nm = new NodeManager();
		Node node = nm.addNode(um.getUser(username).getId(), unknownId, "Node title", 2);
		node.setTitle("Updated title");
		nm.updateNode(node);
	}
	
	@Test(expected = UnknownNodeException.class)
	public void testDeleteUnknownNode() throws UnknownNodeException, Exception {
		NodeManager nm = new NodeManager();
		nm.deleteNode(unknownId);
	}
	
	@Test
	public void testNodeLink() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		NodeLinkManager nlm = new NodeLinkManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nameNode1, directoryId);
		Node node2 = nm.addNode(um.getUser(username).getId(), tree.getId(), nameNode2, directoryId);
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node.getId(), node2.getId(), 0);
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		nlm.deleteNodeLink(nodelink.getId());
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void testNodeLinkUpdate() throws Exception {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		final String nameSubnode1 = "Subnode1";
		final String nameSubnode2 = "Subnode2";
		NodeManager nm = new NodeManager();
		SubnodeManager snm = new SubnodeManager();
		DirectoryManager dm = new DirectoryManager();
		NodeLinkManager nlm = new NodeLinkManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nameNode1, directoryId);
		Node node2 = nm.addNode(um.getUser(username).getId(), tree.getId(), nameNode2, directoryId);
		Subnode subnode = snm.addSubnode(node.getId(), nameSubnode1);
		Subnode subnode2 = snm.addSubnode(node.getId(), nameSubnode2);
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node.getId(), node2.getId(), subnode.getId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		assertEquals(nameSubnode1, snm.getSubnode(nlm.getNodeLink(nodelink.getId()).getSubnodeId()).getTitle());
		nodelink.setSubnodeId(subnode2.getId());
		nlm.updateNodeLink(nodelink);
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		assertEquals(nameSubnode2, snm.getSubnode(nlm.getNodeLink(nodelink.getId()).getSubnodeId()).getTitle());
	}
	
	@Test
	public void testGetNodeLinksUnknownTreeId() throws SQLException {
		NodeLinkManager nlm = new NodeLinkManager();
		nlm.getNodeLinks(unknownId).size();
	}
	
	@Test(expected = UnknownNodeLinkException.class)
	public void testGetUnknownNodeLink() throws UnknownNodeLinkException {
		NodeLinkManager nlm = new NodeLinkManager();
		nlm.getNodeLink(unknownId).getId();
	}
	
	@Test(expected = UnknownTreeException.class)
	public void testUpdateUnknownNodeLink() throws Exception {
		NodeLinkManager nlm = new NodeLinkManager();
		NodeLink nodeLink = nlm.addNodeLink(unknownId, unknownId, unknownId, unknownId);
		nodeLink.setSourceId(unknownId);
		nlm.updateNodeLink(nodeLink);
	}
	
	@Test(expected = UnknownNodeLinkException.class)
	public void testDeleteUnknownNodeLink() throws Exception {
		NodeLinkManager nlm = new NodeLinkManager();
		nlm.deleteNodeLink(unknownId);
	}
	
	@Test
	public void testSubnode() throws Exception {
		final String subnodename = "My Subnode";
		SubnodeManager snm = new SubnodeManager();
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node", directoryId);
		assertEquals(0, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
		Subnode subnode = snm.addSubnode(node.getId(), subnodename);
		assertEquals(1, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(1, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(subnodename, snm.getSubnode(subnode.getId()).getTitle());
		snm.deleteSubnode(subnode.getId());
		assertEquals(0, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
	}
	
	@Test
	public void testSubnodeUpdate() throws Exception {
		final String subnodename = "My Subnode";
		final String newSubnodename = "My New Subnode";
		SubnodeManager snm = new SubnodeManager();
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node", directoryId);
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
	public void TestKnowledgeSubnodePositionChange() throws Exception {
		final String subnodename = "My Subnode";
		SubnodeManager snm = new SubnodeManager();
		NodeManager nm = new NodeManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		DirectoryManager dm = new DirectoryManager();
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node", directoryId);
		Subnode subnode1 = snm.addSubnode(node.getId(), subnodename);
		Subnode subnode2 = snm.addSubnode(node.getId(), subnodename);
		Subnode subnode3 = snm.addSubnode(node.getId(), subnodename);
		Subnode subnode4 = snm.addSubnode(node.getId(), subnodename);
		assertEquals(4, snm.getSubnodesFromNode(node.getId()).size());
		assertEquals(1, snm.getSubnode(subnode1.getId()).getPosition());
		assertEquals(2, snm.getSubnode(subnode2.getId()).getPosition());
		assertEquals(3, snm.getSubnode(subnode3.getId()).getPosition());
		assertEquals(4, snm.getSubnode(subnode4.getId()).getPosition());
		assertTrue(snm.changeSubnodePosition(subnode1.getId(), false));
		assertEquals(2, snm.getSubnode(subnode1.getId()).getPosition());
		assertEquals(1, snm.getSubnode(subnode2.getId()).getPosition());
		assertEquals(3, snm.getSubnode(subnode3.getId()).getPosition());
		assertEquals(4, snm.getSubnode(subnode4.getId()).getPosition());
		assertTrue(snm.changeSubnodePosition(subnode1.getId(), true));
		assertEquals(1, snm.getSubnode(subnode1.getId()).getPosition());
		assertEquals(2, snm.getSubnode(subnode2.getId()).getPosition());
		assertEquals(3, snm.getSubnode(subnode3.getId()).getPosition());
		assertEquals(4, snm.getSubnode(subnode4.getId()).getPosition());
		assertFalse(snm.changeSubnodePosition(subnode1.getId(), true));
		assertFalse(snm.changeSubnodePosition(subnode4.getId(), false));
	}
	
	@Test
	public void testGetSubnodesUnknownTree() throws SQLException {
		SubnodeManager snm = new SubnodeManager();
		assertEquals(0, snm.getSubnodesFromTree(unknownId).size());
	}
	
	@Test
	public void testGetSubnodesUnknownNode() throws SQLException {
		SubnodeManager snm = new SubnodeManager();
		assertEquals(0, snm.getSubnodesFromNode(unknownId).size());
	}
	
	@Test (expected = UnknownSubnodeException.class)
	public void testGetUnknownSubnode() throws UnknownSubnodeException {
		SubnodeManager snm = new SubnodeManager();
		snm.getSubnode(unknownId).getId();
	}
	
	@Test (expected = UnknownNodeException.class)
	public void testUpdateUnknownSubnode() throws Exception {
		SubnodeManager snm = new SubnodeManager();
		Subnode subnode = snm.addSubnode(unknownId, "Subnode Title");
		subnode.setTitle("New subnode title");
		Subnode updatedSubnode = snm.updateSubnode(subnode);
		assertEquals(-1, updatedSubnode.getId());
	}
	
	@Test (expected = UnknownSubnodeException.class)
	public void testDeleteUnknownSubnode() throws UnknownSubnodeException {
		SubnodeManager snm = new SubnodeManager();
		snm.deleteSubnode(unknownId);
	}
	
	@Test
	public void testDirectory() throws Exception {
		final String treeName = "MyTree";
		final String directoryName = "TestDirectory";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		nm.addNode(um.getUser(username).getId(), tree.getId(), "Node", directoryId);
		assertEquals(1, dm.getDirectories(tree.getId()).size());
		Directory newDirectory = dm.addDirectory(tree.getId(), directoryId, directoryName);
		assertEquals(treeName, dm.getDirectory(directoryId).getTitle());
		assertEquals(directoryName, dm.getDirectory(newDirectory.getId()).getTitle());
		dm.deleteDirectory(newDirectory.getId());
		assertEquals(1, dm.getDirectories(tree.getId()).size());
	}

	@Test
	public void testDirectoryUpdate() throws Exception {
		final String treeName = "MyTree";
		final String directoryName = "TestDirectory";
		final String newDirectoryName = "MyNewDirectory";
		NodeManager nm = new NodeManager();
		DirectoryManager dm = new DirectoryManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		nm.addNode(um.getUser(username).getId(), tree.getId(), "Node", directoryId);
		assertEquals(1, dm.getDirectories(tree.getId()).size());
		Directory newDirectory = dm.addDirectory(tree.getId(), directoryId, directoryName);
		assertEquals(treeName, dm.getDirectory(directoryId).getTitle());
		assertEquals(directoryName, dm.getDirectory(newDirectory.getId()).getTitle());
		newDirectory.setTitle(newDirectoryName);
		dm.updateDirectory(newDirectory);
		assertEquals(newDirectoryName, dm.getDirectory(newDirectory.getId()).getTitle());
	}
	
	public void testDirectoryUnknownTreeId() throws SQLException {
		DirectoryManager dm = new DirectoryManager();
		assertEquals(0, dm.getDirectories(unknownId).size());
	}
	@Test(expected = UnknownDirectoryException.class)
	public void testGetUnknownDirectory() throws UnknownDirectoryException {
		DirectoryManager dm = new DirectoryManager();
		dm.getDirectory(unknownId).getId();
	}
	
	@Test(expected = Exception.class)
	public void testUpdateUnknownDirectory() throws Exception {
		DirectoryManager dm = new DirectoryManager();
		Directory directory = dm.addDirectory(unknownId, 0, "My directory");
		directory.setTitle("My new directory");
		Directory updatedDirectory = dm.updateDirectory(directory);
		assertEquals(-1, updatedDirectory.getId());
	}
	
	@Test(expected = UnknownDirectoryException.class)
	public void testDeleteUnknownDirectory() throws Exception {
		DirectoryManager dm = new DirectoryManager();
		dm.deleteDirectory(unknownId);
	}
	
//	@Test
//	public void testSimpleTreeExportAndImport() throws Exception {
//		XmlTreeModel xtm = new XmlTreeModel();
//		NodeModel nm = new NodeModel();
//		SubnodeModel snm = new SubnodeModel();
//		DirectoryModel dm = new DirectoryModel();
//		NodeLinkModel nlm = new NodeLinkModel();
//		TemplateModel tmm = new TemplateModel();
//		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
//		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
//		//before copy
//		Template template1 = tmm.addKnowledgeTemplate(tree.getId(), "Title", "Text 1", false);
//		Template template2 = tmm.addKnowledgeTemplate(tree.getId(), "Title", "Text 2", false);
//		Directory directory1 = dm.addDirectory(tree.getId(), directoryId, "Directory 1");
//		Directory directory2 = dm.addDirectory(tree.getId(), directoryId, "Directory 2");
//		Directory directory3 = dm.addDirectory(tree.getId(), directory2.getId(), "Directory 3");
//		Node node1 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 1", directoryId);
//		Node node2 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 2", directoryId);
//		Node node3 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 3", directoryId);
//		Node node4 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 4", directoryId);
//		Node node5 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 5", directoryId);
//		Node node6 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 6", directoryId);
//		Subnode subnode1 = snm.addSubnode(node1.getId(), "Subnode 1");
//		Subnode subnode2 = snm.addSubnode(node4.getId(), "Subnode 2");
//		Subnode subnode3 = snm.addSubnode(node4.getId(), "Subnode 3");
//		Subnode subnode4 = snm.addSubnode(node5.getId(), "Subnode 4");
//		NodeLink nodeLink1 = nlm.addNodeLink(tree.getId(), node1.getId(), node2.getId(), subnode1.getId());
//		NodeLink nodeLink2 = nlm.addNodeLink(tree.getId(), node3.getId(), node4.getId(), subnode2.getId());
//		NodeLink nodeLink3 = nlm.addNodeLink(tree.getId(), node6.getId(), node5.getId(), 0);
//		assertEquals(2, tmm.getKnowledgeTemplates(tree.getId()).size());
//		assertEquals(4, dm.getDirectories(tree.getId()).size());
//		assertEquals(6, nm.getNodes(tree.getId()).size());
//		assertEquals(4, snm.getSubnodesFromTree(tree.getId()).size());
//		assertEquals(3, nlm.getNodeLinks(tree.getId()).size());
//		XmlTree xmlTree = xtm.addXmlTree(um.getUser(username).getId(), tree.getId());
//		//delete
//		assertTrue(xtm.cleanTree(xmlTree.getId()));
//		assertEquals(0, tmm.getKnowledgeTemplates(tree.getId()).size());
//		assertEquals(1, dm.getDirectories(tree.getId()).size());
//		assertEquals(0, nm.getNodes(tree.getId()).size());
//		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
//		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
//		//after copy
//		xtm.setXmlTree(xmlTree.getId());
//		assertEquals(2, tmm.getKnowledgeTemplates(tree.getId()).size());
//		assertEquals(4, dm.getDirectories(tree.getId()).size());
//		assertEquals(6, nm.getNodes(tree.getId()).size());
//		assertEquals(4, snm.getSubnodesFromTree(tree.getId()).size());
//		assertEquals(3, nlm.getNodeLinks(tree.getId()).size());
//	}
	
	@Test
	public void testSimpleImportExportEmptyTree() throws Exception {
		XmlTreeManager xtm = new XmlTreeManager();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		XmlTree xmlTree = xtm.addXmlTree(um.getUser(username).getId(), tree.getId());
		xtm.cleanTree(xmlTree.getId());
		xtm.setXmlTree(xmlTree.getId());
	}
	
//	@Test
//	public void testSimpleImportExportTreeOneNode() throws Exception {
//		NodeModel nm = new NodeModel();
//		DirectoryModel dm = new DirectoryModel();
//		XmlTreeModel xtm = new XmlTreeModel();
//		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
//		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
//		Node node1 = nm.addNode(um.getUser(username).getId(), tree.getId(), "Node 1", directoryId);
//		assertEquals(1, nm.getNodes(tree.getId()).size());
//		XmlTree xmlTree = xtm.addXmlTree(um.getUser(username).getId(), tree.getId());
//		assertTrue(xtm.cleanTree(xmlTree.getId()));
//		assertEquals(0, nm.getNodes(tree.getId()).size());
//		xtm.setXmlTree(xmlTree.getId());
//		assertEquals(1, nm.getNodes(tree.getId()).size());
//	}
}
