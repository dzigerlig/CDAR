package ch.cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;

import org.glassfish.jersey.internal.util.PropertiesHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.Node;
import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.manager.DirectoryManager;
import ch.cdar.bll.manager.TreeManager;
import ch.cdar.bll.manager.UserManager;
import ch.cdar.bll.manager.producer.NodeManager;
import ch.cdar.bll.wiki.MediaWikiManager;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.helpers.PropertyHelper;

public class TestBLLWiki {
	private UserManager um = new UserManager();
	private TreeManager tm = new TreeManager(UserRole.PRODUCER);
	private NodeManager nm = new NodeManager();
	private DirectoryManager dm = new DirectoryManager(UserRole.PRODUCER);
	
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
		PropertyHelper helper = new PropertyHelper();
		String content = helper.getProperty("NODE_DESCRIPTION").toUpperCase();
		assertEquals("== "+content+" ==", mwm.getKnowledgeNodeWikiEntry(node.getId()).getWikiContentPlain());
	}
}
