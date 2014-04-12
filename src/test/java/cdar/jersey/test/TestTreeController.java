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
import cdar.bll.producer.Node;
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
		Tree testTree = target(userId + "/ktree/" + treeid).request().get(
				Tree.class);
		assertEquals(TREENAME, testTree.getName());
		assertNotEquals(testTree.getId(), -1);
	}

	@Test
	public void testAllTrees() {
		Set<?> testTree = target(userId + "/ktree").request().get(Set.class);
		int quantityOfTreesBefore = testTree.size();
		Tree tree = target(userId + "/ktree/tree/add").request()
				.post(Entity.entity(TREENAME, MediaType.APPLICATION_JSON),
						Tree.class);
		int quantityOfTreesAfter = target(userId + "/ktree").request()
				.get(Set.class).size();
		target(userId + "/ktree/delete").request().post(
				Entity.entity(tree.getId(), MediaType.APPLICATION_JSON),
				boolean.class);		
		assertEquals(quantityOfTreesBefore + 1, quantityOfTreesAfter);

	}

	@Test
	public void testAddAndDeleteTemplate() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		int quantityOfTemplatesBeforeAdd = target(
				userId + "/ktree/templates/" + treeid).request().get(Set.class)
				.size();

		Template addedTemplate = target(
				userId + "/ktree/templates/add/" + treeid).request().post(
				Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
				Template.class);
		int quantityOfTemplatesAfterAdd = target(
				userId + "/ktree/templates/" + treeid).request().get(Set.class)
				.size();

		boolean isTemplateDeleted = target(
				userId + "/ktree/templates/delete/" + treeid).request()
				.post(Entity.entity(addedTemplate.getId(),
						MediaType.APPLICATION_JSON), boolean.class);
		int quantityOfTemplatesAfterDelete = target(
				userId + "/ktree/templates/" + treeid).request().get(Set.class)
				.size();			
		assertEquals(quantityOfTemplatesBeforeAdd + 1, quantityOfTemplatesAfterAdd);
		assertTrue(isTemplateDeleted);
		assertEquals(quantityOfTemplatesBeforeAdd, quantityOfTemplatesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingTemplate() {
		boolean isTemplateDeleted = target(
				userId + "/ktree/templates/delete/" + treeid).request().post(
				Entity.entity(999999999, MediaType.APPLICATION_JSON),
				boolean.class);
		assertFalse(isTemplateDeleted);
	}

	@Test
	public void testGetAllTemplates() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Set<?> template = target(userId + "/ktree/templates/" + treeid)
				.request().get(Set.class);
		int quantityOfTemplatesBefore = template.size();
		Template addedTemplates = target(
				userId + "/ktree/templates/add/" + treeid).request().post(
				Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
				Template.class);
		int quantityOfTemplateAfter = target(
				userId + "/ktree/templates/" + treeid).request().get(Set.class)
				.size();
		target(userId + "/ktree/templates/delete/" + treeid).request().post(
				Entity.entity(addedTemplates.getId(),
						MediaType.APPLICATION_JSON), boolean.class);		
		assertEquals(quantityOfTemplatesBefore + 1, quantityOfTemplateAfter);

	}

	@Test
	public void testGetTemplate() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Template addedTemplate = target(
				userId + "/ktree/templates/add/" + treeid).request().post(
				Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
				Template.class);
		Template getTemplate = target(
				userId + "/ktree/templates/" + treeid + "/"
						+ addedTemplate.getId()).request().get(Template.class);
		target(userId + "/ktree/templates/delete/" + treeid).request()
				.post(Entity.entity(addedTemplate.getId(),
						MediaType.APPLICATION_JSON), boolean.class);		
		assertNotEquals(-1, getTemplate.getId());

	}

	@Test
	public void testEditTemplate() {
		Template addTestTemplate = new Template();
		addTestTemplate.setTreeid(treeid);
		addTestTemplate.setTitle("TestTemplate");
		addTestTemplate.setTemplatetext("TemplateText");
		Template addedTemplate = target(
				userId + "/ktree/templates/add/" + treeid).request().post(
				Entity.entity(addTestTemplate, MediaType.APPLICATION_JSON),
				Template.class);
		addedTemplate.setTemplatetext(TREENAME);
		addedTemplate.setTitle(USERNAME);
		Template editTemplate = target(
				userId + "/ktree/templates/edit/" + addedTemplate.getId())
				.request()
				.post(Entity.entity(addedTemplate, MediaType.APPLICATION_JSON),
						Template.class);
		
		target(userId + "/ktree/templates/delete/" + treeid).request()
				.post(Entity.entity(addedTemplate.getId(),
						MediaType.APPLICATION_JSON), boolean.class);
		assertEquals(TREENAME, editTemplate.getTemplatetext());
		assertEquals(USERNAME, editTemplate.getTitle());
	}

	@Test
	public void testAddAndDeleteDirectories() {
		int quantityOfDirectoriesBefore = target(
				userId + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(0);
		Directory addedDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(addTestDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		int quantityOfDirectoriesAfter = target(
				userId + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();
		
		Boolean isDirectoryDeleted = target(
				userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);
		assertEquals(quantityOfDirectoriesBefore + 1,
				quantityOfDirectoriesAfter);
		assertTrue(isDirectoryDeleted);
	}

	@Test
	public void testDeleteNotExistingDirectory() {
		boolean isDirectoryDeleted = target(
				userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(999999999, MediaType.APPLICATION_JSON),
				boolean.class);
		assertEquals(false, isDirectoryDeleted);
	}

	@Test
	public void testGetAllDirectories() {
		int quantityOfDirectoriesBefore = target(
				userId + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(0);
		Directory addedDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(addTestDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		int quantityOfDirectoriesAfter = target(
				userId + "/ktree/directories/" + treeid).request()
				.get(Set.class).size();

		target(userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);		assertEquals(quantityOfDirectoriesBefore + 1,
				quantityOfDirectoriesAfter);
	}

	@Test
	public void testRenameDirectories() {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(0);
		Directory addedDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(addTestDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		addedDirectory.setTitle(USERNAME);
		Directory renamedDirectory = target(
				userId + "/ktree/directories/rename/" + treeid).request().post(
				Entity.entity(addedDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		target(userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);		assertEquals(USERNAME, renamedDirectory.getTitle());

	}

	@Test
	public void testMovedDirectories() {
		Directory parentDirectory = new Directory();
		parentDirectory.setKtrid(treeid);
		parentDirectory.setParentid(0);
		Directory addedParentDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(parentDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		Directory childDirectory = new Directory();
		childDirectory.setKtrid(treeid);
		childDirectory.setParentid(0);
		Directory addedChildDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(childDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		addedChildDirectory.setParentid(addedParentDirectory.getId());
		Directory movedDirectory = target(
				userId + "/ktree/directories/move/" + treeid).request().post(
				Entity.entity(addedChildDirectory, MediaType.APPLICATION_JSON),
				Directory.class);
		target(userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedChildDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);
		target(userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedParentDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);		assertEquals(addedParentDirectory.getId(), movedDirectory.getParentid());

	}

	@Test
	public void testAddAndDeleteNode() {
		Directory addTestDirectory = new Directory();
		addTestDirectory.setKtrid(treeid);
		addTestDirectory.setParentid(0);
		Directory addedDirectory = target(
				userId + "/ktree/directories/add/" + treeid).request().post(
				Entity.entity(addTestDirectory, MediaType.APPLICATION_JSON),
				Directory.class);

		int quantityOfNodesBeforeAdd = target(userId + "/ktree/nodes/" + treeid)
				.request().get(Set.class).size();

		Node addTestNode = new Node();
		addTestNode.setKtrid(treeid);
		addTestNode.setDid(addedDirectory.getId());

		Node addedNode = target(userId + "/ktree/nodes/add/" + treeid)
				.request().post(
						Entity.entity(addTestNode, MediaType.APPLICATION_JSON),
						Node.class);
		int quantityOfNodesAfterAdd = target(userId + "/ktree/nodes/" + treeid)
				.request().get(Set.class).size();

		boolean isNodeDeleted = target(userId + "/ktree/nodes/delete/" + treeid)
				.request().post(
						Entity.entity(addedNode.getId(),
								MediaType.APPLICATION_JSON), boolean.class);
		int quantityOfNodesAfterDelete = target(
				userId + "/ktree/nodes/" + treeid).request().get(Set.class)
				.size();

		target(userId + "/ktree/directories/delete/" + treeid).request().post(
				Entity.entity(addedDirectory.getId(),
						MediaType.APPLICATION_JSON), Boolean.class);

		assertTrue(isNodeDeleted);
		assertEquals(quantityOfNodesBeforeAdd + 1, quantityOfNodesAfterAdd);
		assertEquals(quantityOfNodesBeforeAdd, quantityOfNodesAfterDelete);
	}

	@Test
	public void testDeleteNotExistingNode() {
		boolean isNodeDeleted = target(
				userId + "/ktree/nodes/delete/" + treeid).request().post(
				Entity.entity(999999999, MediaType.APPLICATION_JSON),
				boolean.class);
		assertFalse(isNodeDeleted);
	}
}
