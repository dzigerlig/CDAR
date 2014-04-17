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
import cdar.bll.producer.XmlTree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeLinkModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.SubnodeModel;
import cdar.bll.producer.models.TemplateModel;
import cdar.bll.producer.models.TreeModel;
import cdar.bll.producer.models.XmlTreeModel;
import cdar.bll.user.UserModel;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;
import cdar.dal.persistence.jdbc.user.UserDao;

public class TestBLLKnowledgeProducer {
	private UserModel um = new UserModel();
	private TreeModel tm = new TreeModel();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	
	private final int unknownId = -13;
	
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
		assertEquals(treeName, tm.getTree(tree.getId()).getTitle());
		tm.deleteTree(tree.getId());
		assertEquals(treeCount, tm.getTrees(um.getUser(username).getId()).size());
	}
	
	@Test
	public void testGetTreesUnknownUserId() {
		assertEquals(0, tm.getTrees(unknownId).size());
	}
	
	@Test
	public void testGetUknownTree() {
		assertEquals(-1, tm.getTree(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownTree() {
		Tree tree = tm.addTree(unknownId, "MyTree");
		tree.setTitle("My unknown tree");
		Tree updatedTree = tm.updateTree(tree);
		assertEquals(-1, updatedTree.getId());
	}
	
	@Test
	public void testDeleteUnknownTree() {
		assertFalse(tm.deleteTree(unknownId));
	}
	
	@Test
	public void testTreeUpdate() {
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
	public void testTemplateIsDefault() {
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text");
		assertFalse(template.getIsDefault());
	}
	
	@Test
	public void testTemplateDefaultChangeMultipleTemplates() {
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template1 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text");
		Template template2 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text");
		assertFalse(template1.getIsDefault());
		assertFalse(template2.getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeid(), template1.getId());
		assertTrue(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeid(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertTrue(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
	}
	
	@Test
	public void testTemplateDefaultChangeMultipleTemplatesNoDefault() {
		TemplateModel tplm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		Template template1 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text");
		Template template2 = tplm.addKnowledgeTemplate(tree.getId(), "My Template Title", "My Template Text");
		assertFalse(template1.getIsDefault());
		assertFalse(template2.getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeid(), template1.getId());
		assertTrue(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template2.getTreeid(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertTrue(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
		tplm.setDefaultTemplate(template1.getTreeid(), template2.getId());
		assertFalse(tplm.getKnowledgeTemplate(template1.getId()).getIsDefault());
		assertFalse(tplm.getKnowledgeTemplate(template2.getId()).getIsDefault());
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
	public void testGetTemplatesUnknownTreeId() {
		TemplateModel tplm = new TemplateModel();
		assertEquals(0, tplm.getKnowledgeTemplates(unknownId).size());
	}
	
	@Test
	public void testGetUnknownTemplate() {
		TemplateModel tplm = new TemplateModel();
		assertEquals(-1, tplm.getKnowledgeTemplate(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownTemplate() {
		TemplateModel tplm = new TemplateModel();
		Template template = tplm.addKnowledgeTemplate(unknownId, "Template title", "Template text");
		template.setTitle("New title");
		Template updatedTemplate = tplm.updateTemplate(template);
		assertEquals(-1, updatedTemplate.getId());
	}
	
	@Test
	public void testDeleteUnknownTemplate() {
		TemplateModel tplm = new TemplateModel();
		assertFalse(tplm.deleteTemplate(unknownId));
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
		assertEquals(tree.getId(), nm.getNode(node.getId()).getKtrid());
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
	public void testGetNodesUnknownTreeId() {
		NodeModel nm = new NodeModel();
		assertEquals(0, nm.getNodes(unknownId).size());
	}
	
	@Test
	public void testGetUnknownNode() {
		NodeModel nm = new NodeModel();
		assertEquals(-1, nm.getNode(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownNode() {
		NodeModel nm = new NodeModel();
		Node node = nm.addNode(unknownId, "Node title", 0);
		node.setTitle("Updated title");
		Node updatedNode = nm.updateNode(node);
		assertEquals(-1, updatedNode.getId());
	}
	
	@Test
	public void testDeleteUnknownNode() {
		NodeModel nm = new NodeModel();
		assertFalse(nm.deleteNode(unknownId));
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
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		nlm.deleteNodeLink(nodelink.getId());
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
	}
	
	@Test
	public void testNodeLinkUpdate() {
		final String nameNode1 = "Node1";
		final String nameNode2 = "Node2";
		final String nameSubnode1 = "Subnode1";
		final String nameSubnode2 = "Subnode2";
		NodeModel nm = new NodeModel();
		SubnodeModel snm = new SubnodeModel();
		DirectoryModel dm = new DirectoryModel();
		NodeLinkModel nlm = new NodeLinkModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nameNode1, directoryId);
		Node node2 = nm.addNode(tree.getId(), nameNode2, directoryId);
		Subnode subnode = snm.addSubnode(node.getId(), nameSubnode1);
		Subnode subnode2 = snm.addSubnode(node.getId(), nameSubnode2);
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		NodeLink nodelink = nlm.addNodeLink(tree.getId(), node.getId(), node2.getId(), subnode.getId());
		assertEquals(1, nlm.getNodeLinks(tree.getId()).size());
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		assertEquals(nameSubnode1, snm.getSubnode(nlm.getNodeLink(nodelink.getId()).getKsnid()).getTitle());
		nodelink.setKsnid(subnode2.getId());
		nlm.updateNodeLink(nodelink);
		assertEquals(nameNode1, nm.getNode(nlm.getNodeLink(nodelink.getId()).getSourceId()).getTitle());
		assertEquals(nameNode2, nm.getNode(nlm.getNodeLink(nodelink.getId()).getTargetId()).getTitle());
		assertEquals(nameSubnode2, snm.getSubnode(nlm.getNodeLink(nodelink.getId()).getKsnid()).getTitle());
	}
	
	@Test
	public void testGetNodeLinksUnknownTreeId() {
		NodeLinkModel nlm = new NodeLinkModel();
		assertEquals(0, nlm.getNodeLinks(unknownId).size());
	}
	
	@Test
	public void testGetUnknownNodeLink() {
		NodeLinkModel nlm = new NodeLinkModel();
		assertEquals(-1, nlm.getNodeLink(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownNodeLink() {
		NodeLinkModel nlm = new NodeLinkModel();
		NodeLink nodeLink = nlm.addNodeLink(unknownId, unknownId, unknownId, unknownId);
		nodeLink.setSourceId(unknownId);
		NodeLink updatedNodeLink = nlm.updateNodeLink(nodeLink);
		assertEquals(-1, updatedNodeLink.getId());
	}
	
	@Test
	public void testDeleteUnknownNodeLink() {
		NodeLinkModel nlm = new NodeLinkModel();
		assertFalse(nlm.deleteNodeLink(unknownId));
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
		snm.deleteSubnode(subnode.getId());
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
	public void TestKnowledgeSubnodePositionChange() {
		final String subnodename = "My Subnode";
		SubnodeModel snm = new SubnodeModel();
		NodeModel nm = new NodeModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		DirectoryModel dm = new DirectoryModel();
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), "Node", directoryId);
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
	public void testGetSubnodesUnknownTree() {
		SubnodeModel snm = new SubnodeModel();
		assertEquals(0, snm.getSubnodesFromTree(unknownId).size());
	}
	
	@Test
	public void testGetSubnodesUnknownNode() {
		SubnodeModel snm = new SubnodeModel();
		assertEquals(0, snm.getSubnodesFromNode(unknownId).size());
	}
	
	@Test
	public void testGetUnknownSubnode() {
		SubnodeModel snm = new SubnodeModel();
		assertEquals(-1, snm.getSubnode(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownSubnode() {
		SubnodeModel snm = new SubnodeModel();
		Subnode subnode = snm.addSubnode(unknownId, "Subnode Title");
		subnode.setTitle("New subnode title");
		Subnode updatedSubnode = snm.updateSubnode(subnode);
		assertEquals(-1, updatedSubnode.getId());
	}
	
	@Test
	public void testDeleteUnknownSubnode() {
		SubnodeModel snm = new SubnodeModel();
		assertFalse(snm.deleteSubnode(unknownId));
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
		dm.deleteDirectory(newDirectory.getId());
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
	
	public void testDirectoryUnknownTreeId() {
		DirectoryModel dm = new DirectoryModel();
		assertEquals(0, dm.getDirectories(unknownId).size());
	}
	
	@Test
	public void testGetUnknownDirectory() {
		DirectoryModel dm = new DirectoryModel();
		assertEquals(-1, dm.getDirectory(unknownId).getId());
	}
	
	@Test
	public void testUpdateUnknownDirectory() {
		DirectoryModel dm = new DirectoryModel();
		Directory directory = dm.addDirectory(unknownId, 0, "My directory");
		directory.setTitle("My new directory");
		Directory updatedDirectory = dm.updateDirectory(directory);
		assertEquals(-1, updatedDirectory.getId());
	}
	
	@Test
	public void testDeleteUnknownDirectory() {
		DirectoryModel dm = new DirectoryModel();
		assertFalse(dm.deleteDirectory(unknownId));
	}
	
	@Test
	public void testSimpleTreeExportAndImport() {
		XmlTreeModel xtm = new XmlTreeModel();
		NodeModel nm = new NodeModel();
		SubnodeModel snm = new SubnodeModel();
		DirectoryModel dm = new DirectoryModel();
		NodeLinkModel nlm = new NodeLinkModel();
		TemplateModel tmm = new TemplateModel();
		Tree tree = tm.addTree(um.getUser(username).getId(), "MyTree");
		int directoryId = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		//before copy
		Template template1 = tmm.addKnowledgeTemplate(tree.getId(), "Title", "Text 1");
		Template template2 = tmm.addKnowledgeTemplate(tree.getId(), "Title", "Text 2");
		Directory directory1 = dm.addDirectory(tree.getId(), directoryId, "Directory 1");
		Directory directory2 = dm.addDirectory(tree.getId(), directoryId, "Directory 2");
		Directory directory3 = dm.addDirectory(tree.getId(), directory2.getId(), "Directory 3");
		Node node1 = nm.addNode(tree.getId(), "Node 1", directoryId);
		Node node2 = nm.addNode(tree.getId(), "Node 2", directoryId);
		Node node3 = nm.addNode(tree.getId(), "Node 3", directoryId);
		Node node4 = nm.addNode(tree.getId(), "Node 4", directoryId);
		Node node5 = nm.addNode(tree.getId(), "Node 5", directoryId);
		Node node6 = nm.addNode(tree.getId(), "Node 6", directoryId);
		Subnode subnode1 = snm.addSubnode(node1.getId(), "Subnode 1");
		Subnode subnode2 = snm.addSubnode(node4.getId(), "Subnode 2");
		Subnode subnode3 = snm.addSubnode(node4.getId(), "Subnode 3");
		Subnode subnode4 = snm.addSubnode(node5.getId(), "Subnode 4");
		NodeLink nodeLink1 = nlm.addNodeLink(tree.getId(), node1.getId(), node2.getId(), subnode1.getId());
		NodeLink nodeLink2 = nlm.addNodeLink(tree.getId(), node3.getId(), node4.getId(), subnode2.getId());
		NodeLink nodeLink3 = nlm.addNodeLink(tree.getId(), node6.getId(), node5.getId(), 0);
		assertEquals(2, tmm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(4, dm.getDirectories(tree.getId()).size());
		assertEquals(6, nm.getNodes(tree.getId()).size());
		assertEquals(4, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(3, nlm.getNodeLinks(tree.getId()).size());
		XmlTree xmlTree = xtm.addXmlTree(um.getUser(username).getId(), tree.getId());
		//delete
		assertTrue(xtm.cleanTree(tree.getId()));
		assertEquals(0, tmm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(0, dm.getDirectories(tree.getId()).size());
		assertEquals(0, nm.getNodes(tree.getId()).size());
		assertEquals(0, snm.getSubnodesFromTree(tree.getId()).size());
		assertEquals(0, nlm.getNodeLinks(tree.getId()).size());
		//after copy
		xtm.setXmlTree(xmlTree.getId());
		System.out.println("Tree id test: " + tree.getId());
		assertEquals(2, tmm.getKnowledgeTemplates(tree.getId()).size());
		assertEquals(4, dm.getDirectories(tree.getId()).size());
//		assertEquals(6, nm.getNodes(tree.getId()).size());
//		assertEquals(4, snm.getSubnodesFromTree(tree.getId()).size());
//		assertEquals(3, nlm.getNodeLinks(tree.getId()).size());
	}
}
