package cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Node;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.bll.manager.producer.DirectoryManager;
import cdar.bll.manager.producer.NodeManager;
import cdar.bll.manager.producer.TreeManager;
import cdar.bll.wiki.MediaWikiManager;
import cdar.dal.exceptions.UnknownUserException;

public class TestBLLWiki {
	private UserManager um = new UserManager();
	private TreeManager tm = new TreeManager();
	private NodeManager nm = new NodeManager();
	private DirectoryManager dm = new DirectoryManager();
	
	private MediaWikiManager mwm = new MediaWikiManager();
	
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";
	private final String treeName = "Test Tree";
	private final String nodeTitle = "Test Node";
	
	@Before
	public void createUser() throws Exception {
		um.createUser(new User(username, password), false);
	}
	
	@After
	public void deleteUser() throws UnknownUserException, Exception {
		um.deleteUser(um.getUser(username).getId());
	}
	
	@Test
	public void testAddNode() throws Exception {
		Tree tree = new Tree();
		tree.setTitle(treeName);
		tree = tm.addTree(um.getUser(username).getId(), tree);
		int did = ((Directory)dm.getDirectories(tree.getId()).toArray()[0]).getId();
		Node node = new Node();
		node.setTreeId(tree.getId());
		node.setTitle(nodeTitle);
		node.setDirectoryId(did);
		node = nm.addNode(um.getUser(username).getId(), node);
		assertEquals("== CDAR ==", mwm.getKnowledgeNodeWikiEntry(node.getId()).getWikiContentPlain());
	}
}
