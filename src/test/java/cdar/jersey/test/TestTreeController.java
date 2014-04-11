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

import cdar.bll.producer.Directory;
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
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
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
		Set<?> testTree = target(userId + "/ktree").request()
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
	public void testaddAndDeleteTemplate() {		
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		int quantityOfTemplatesBefore = target(userId + "/ktree/templates/"+treeid).request()
				.get(Set.class).size();
		
		Template addedTemplate = target(userId + "/ktree/templates/add/"+treeid).request()
				.post(Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Template.class);	
		int quantityOfTemplatesAfter= target(userId + "/ktree/templates/"+treeid).request()
				.get(Set.class).size();
		assertEquals(quantityOfTemplatesBefore+1, quantityOfTemplatesAfter);
	
		boolean isTemplateDeleted = target(userId + "/ktree/templates/delete/"+treeid).request()
				.post(Entity.entity(addedTemplate.getId(), MediaType.APPLICATION_JSON),
						boolean.class);		
		assertTrue(isTemplateDeleted);
		int quantityOfTemplatesAfterDelete = target(userId + "/ktree/templates/"+treeid).request()
				.get(Set.class).size();
		assertEquals(quantityOfTemplatesBefore, quantityOfTemplatesAfterDelete);
	}
	
	@Test
	public void testDeleteNotExistingTemplate() {		
		boolean isTemplateDeleted = target(userId + "/ktree/templates/delete/"+treeid).request()
				.post(Entity.entity(999999999, MediaType.APPLICATION_JSON),
						boolean.class);		
		assertFalse(isTemplateDeleted);
	}
	
	@Test
	public void testGetAllTemplates() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Set<?> template = target(userId + "/ktree/templates/"+treeid).request()
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
	
	@Test
	public void testGetTemplate() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Template addedTemplate = target(userId + "/ktree/templates/add/"+treeid).request()
				.post(Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Template.class);	
		Template getTemplate=target(userId + "/ktree/templates/"+treeid+"/"+addedTemplate.getId()).request()
				.get(Template.class);	
		assertNotEquals(-1,getTemplate.getId());
		boolean isTemplateDeleted = target(userId + "/ktree/templates/delete/"+treeid).request()
				.post(Entity.entity(addedTemplate.getId(), MediaType.APPLICATION_JSON),
						boolean.class);
		assertTrue(isTemplateDeleted);
	}
	
	
	@Test
	public void testEditTemplate() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Template addedTemplate = target(userId + "/ktree/templates/add/"+treeid).request()
				.post(Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
						Template.class);
		addedTemplate.setTemplatetext(TREENAME);
		addedTemplate.setTitle(USERNAME);		
		Template editTemplate=target(userId + "/ktree/templates/edit/"+addedTemplate.getId()).request()
				.post(Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
						Template.class);
		assertEquals(TREENAME, editTemplate.getTemplatetext());
		assertEquals(USERNAME, editTemplate.getTitle());
		boolean isTemplateDeleted = target(userId + "/ktree/templates/delete/"+treeid).request()
				.post(Entity.entity(addedTemplate.getId(), MediaType.APPLICATION_JSON),
						boolean.class);
		assertTrue(isTemplateDeleted);
	}
	
	@Test
	public void testGetDirectories() {
		int quantityOfDirectoriesBefore = target(userId + "/ktree/directories/"+treeid).request()
				.get(Set.class).size();
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(0);
		Directory addedDirectory = target(userId + "/ktree/directories/add/"+treeid).request()
				.post(Entity.entity(addTestDirectory, MediaType.APPLICATION_JSON),
						Directory.class);
		int quantityOfDirectoriesAfter= target(userId + "/ktree/directories/"+treeid).request()
				.get(Set.class).size();
		assertEquals(quantityOfDirectoriesBefore+1, quantityOfDirectoriesAfter);
		assertTrue(addedDirectory.getId()>0);
	}
}
