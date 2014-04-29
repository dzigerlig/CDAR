package cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.Tree;
import cdar.bll.producer.managers.DirectoryManager;
import cdar.bll.producer.managers.NodeManager;
import cdar.bll.producer.managers.TreeManager;
import cdar.bll.user.UserManager;
import cdar.bll.wiki.MediaWikiModel;
import cdar.dal.exceptions.UnknownUserException;

public class TestBLLWiki {
	private UserManager um = new UserManager();
	private TreeManager tm = new TreeManager();
	private NodeManager nm = new NodeManager();
	private DirectoryManager dm = new DirectoryManager();
	
	private MediaWikiModel mwm = new MediaWikiModel();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	private final String treeName = "Test Tree";
	private final String nodeTitle = "Test Node";
	
	@Before
	public void createUser() throws Exception {
		um.createUser(username, password);
	}
	
	@After
	public void deleteUser() throws UnknownUserException, Exception {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testAddNode() throws Exception {
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int did = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle, did);
		assertEquals("== CDAR ==", mwm.getKnowledgeNodeWikiEntry(node.getId()).getWikiContentPlain());
	}
	
	@Test
	public void testAddNodeWithTemplate() throws Exception {
		Tree tree = tm.addTree(um.getUser(username).getId(), treeName);
		int did = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = nm.addNode(um.getUser(username).getId(), tree.getId(), nodeTitle, did);
	}
}
