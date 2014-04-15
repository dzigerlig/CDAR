package cdar.bll.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.Tree;
import cdar.bll.producer.models.DirectoryModel;
import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.TreeModel;
import cdar.bll.user.UserModel;
import cdar.bll.wiki.MediaWikiModel;

public class TestBLLWiki {
	private UserModel um = new UserModel();
	private TreeModel tm = new TreeModel();
	private NodeModel nm = new NodeModel();
	private DirectoryModel dm = new DirectoryModel();
	
	private MediaWikiModel mwm = new MediaWikiModel();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	private final String treeName = "Test Tree";
	private final String nodeTitle = "Test Node";
	
	@Before
	public void createUser() {
		um.createUser(username, password);
	}
	
	@After
	public void deleteUser() {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testAddNode() {
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int did = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(tree.getId(), nodeTitle, did);
		assertEquals(node.getWikiTitle(), mwm.getKnowledgeNodeWikiEntry(node.getId()).getWikiContentPlain());
	}
	
	@Test
	public void testAddNodeWithTemplate() {
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int did = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		
		Node node = nm.addNode(tree.getId(), nodeTitle, did);
	}
}
