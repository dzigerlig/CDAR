package cdar.jersey.test;

import static org.junit.Assert.*;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.security.krb5.internal.tools.Ktab;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
import cdar.bll.user.User;
import cdar.pl.controller.TreeController;
import cdar.pl.controller.UserController;

public class TestTreeController extends JerseyTest {

	final String USERNAME = "testuser";
	final String PASSWORD = "testpassword";
	final String TREENAME = "testtree";
	int userId;
	int treeid;

	@Override
	protected Application configure() {
		return new ResourceConfig(TreeController.class, UserController.class);
	}

	@Before
	public void createUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		User postResponse = target("users/registration").request().post(
				Entity.entity(user, MediaType.APPLICATION_JSON), User.class);
		userId = postResponse.getId();
		int quantityOfTreesBefore = target(userId + "/ktree").request()
				.get(Set.class).size();
		Tree tree = target(userId + "/ktree/tree/add").request()
				.post(Entity.entity(TREENAME, MediaType.APPLICATION_JSON),
						Tree.class);
		treeid = tree.getId();

		int quantityOfTreesAfter = target(userId + "/ktree").request()
				.get(Set.class).size();

		assertEquals(quantityOfTreesBefore + 1, quantityOfTreesAfter);
	}

	@After
	public void testDeleteUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		int quantityOfTreesBefore = target(userId + "/ktree").request()
				.get(Set.class).size();
		boolean isTreeDeleted = target(userId + "/ktree/delete").request()
				.post(Entity.entity(treeid, MediaType.APPLICATION_JSON),
						boolean.class);
		int quantityOfTreesAfter = target(userId + "/ktree").request()
				.get(Set.class).size();

		target("users/delete").request().post(
				Entity.entity(userId, MediaType.APPLICATION_JSON),
				Boolean.class);

		assertEquals(true, isTreeDeleted);
		assertEquals(quantityOfTreesBefore - 1, quantityOfTreesAfter);
	}
	
	@Test
	public void testDeleteNotExistingTree() {
		boolean isTreeDeleted = target(userId + "/ktree/delete").request()
				.post(Entity.entity(999999, MediaType.APPLICATION_JSON),
						boolean.class);
		assertEquals(false, isTreeDeleted);
	}

	@Test
	public void testGetTree() {
		Tree testTree = target(userId + "/ktree/"+treeid).request()
				.get(Tree.class);
		assertEquals(TREENAME, testTree.getName());
		assertNotEquals(testTree.getId(), -1);
	}
	
	@Test
	public void testAllTrees() {		
		Set testTree = target(userId + "/ktree").request()
				.get(Set.class);
		int quantityOfTreesBefore = testTree.size(); 
		Tree tree = target(userId + "/ktree/tree/add").request()
				.post(Entity.entity(TREENAME, MediaType.APPLICATION_JSON),
						Tree.class);		
		int quantityOfTreesAfter = target(userId + "/ktree").request()
				.get(Set.class).size();
		assertEquals(quantityOfTreesBefore+1, quantityOfTreesAfter);
		boolean isTreeDeleted = target(userId + "/ktree/delete").request()
				.post(Entity.entity(tree.getId(), MediaType.APPLICATION_JSON),
						boolean.class);
		assertTrue(isTreeDeleted);
		assertTrue(testTree.size()>0);
	}
	
	@Test
	public void testGetTemplates() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Set template = target(userId + "/ktree/templates/"+treeid).request()
				.get(Set.class);
		int quantityOfTemplatesBefore = template.size(); 
		Template addedTemplates = target(userId + "/ktree/templates/add/"+treeid).request()
				.post(Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Template.class);	
		int quantityOfTemplateAfter=target(userId + "/ktree/templates/"+treeid).request()
				.get(Set.class).size();		
		assertEquals(quantityOfTemplatesBefore+1, quantityOfTemplateAfter);
		boolean isTemplateDeleted = target(userId + "/ktree/templates/delete/"+treeid).request()
				.post(Entity.entity(addedTemplates.getId(), MediaType.APPLICATION_JSON),
						boolean.class);
		assertTrue(isTemplateDeleted);
	}

}
