package cdar.jersey.integrationtest;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.pl.controller.UserController;
import cdar.pl.controller.consumer.ProjectTreeController;

public class TestProjectTreeController extends JerseyTest  {
	private final String USERNAME = "testuser";
	private final String PASSWORD = "testpassword";
	private final String TREENAME = "testtree";
	private final String UID = "uid";
	private final String ACCESSTOKEN = "accesstoken";
	private final int NOTEXISTINGID = 999999999;
	private int userId;
	private int treeid;
	private String accesstoken;
	
	
	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class,ProjectTreeController.class);
	}
	
	@Before
	public void createUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user = target("users")
				.request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON),
						Response.class).readEntity(User.class);
		user = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class)
				.readEntity(User.class);
		userId = user.getId();
		accesstoken = user.getAccesstoken();
		Response quantityOfTreesBeforeAddRequest = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesBeforeAdd = quantityOfTreesBeforeAddRequest
				.readEntity(Set.class).size();
		Tree addTree = new Tree();
		addTree.setTitle(TREENAME);
		Response createdTreeResponse = target("ptrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree tree = createdTreeResponse.readEntity(Tree.class);
		treeid = tree.getId();
		int quantityOfTreesAfterAdd = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		assertEquals(201, createdTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 1, quantityOfTreesAfterAdd);
	}

	@After
	public void testDeleteUserTree() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		int quantityOfTreesBeforeDelete = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		Response deletedTreeResponse = target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		int quantityOfTreesAfterDelete = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Set.class).size();
		User deleteUser = new User();
		deleteUser.setId(userId);
		target("users/delete")
				.queryParam("password", PASSWORD)
				.queryParam("username", USERNAME)
				.request()
				.post(Entity.entity(deleteUser, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(200, deletedTreeResponse.getStatus());
		assertEquals(quantityOfTreesBeforeDelete - 1,
				quantityOfTreesAfterDelete);
	}
	
	@Test
	public void testDelteNotExistingTree() {
		Tree deleteTree = new Tree();
		deleteTree.setId(NOTEXISTINGID);
		Response deletedTreeResponse = target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(400, deletedTreeResponse.getStatus());
	}
	
	@Test
	public void testGetTree() {
		Response testTreeResponse = target("ptrees/" + treeid).request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		Tree testTree = testTreeResponse.readEntity(Tree.class);
		assertEquals(TREENAME, testTree.getTitle());
		assertEquals(200, testTreeResponse.getStatus());
	}

	@Test
	public void testEditTree() {
		Tree editTree = new Tree();
		editTree.setTitle("editTree");
		Response editedTreeResponse = target("ptrees/" + treeid)
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(editTree, MediaType.APPLICATION_JSON),
						Response.class);
		Tree editedTree = getTree(treeid);
		assertEquals(200, editedTreeResponse.getStatus());
		assertEquals("editTree", editedTree.getTitle());
	}

	@Test
	public void testGetAllTrees() {
		int quantityOfTreesBeforeAdd = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class).readEntity(Set.class).size();
		Tree addedtree1 = addTree(TREENAME);
		Tree addedtree2 = addTree(TREENAME);

		Response quantityOfTreesAfterAddResponse = target("ptrees").request()
				.header(UID, userId).header(ACCESSTOKEN, accesstoken)
				.get(Response.class);
		int quantityOfTreesAfterAdd = quantityOfTreesAfterAddResponse
				.readEntity(Set.class).size();
		deleteTree(addedtree1.getId());
		deleteTree(addedtree2.getId());
		assertEquals(200, quantityOfTreesAfterAddResponse.getStatus());
		assertEquals(quantityOfTreesBeforeAdd + 2, quantityOfTreesAfterAdd);
	}
	
	
	private Tree addTree(String treename) {
		Tree addTree = new Tree();
		addTree.setTitle(treename);
		return target("ptrees")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(addTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(Tree.class);
	}

	private boolean deleteTree(int treeid) {
		Tree deleteTree = new Tree();
		deleteTree.setId(treeid);
		return target("ptrees/delete")
				.request()
				.header(UID, userId)
				.header(ACCESSTOKEN, accesstoken)
				.post(Entity.entity(deleteTree, MediaType.APPLICATION_JSON),
						Response.class).readEntity(boolean.class);
	}

	private Tree getTree(int id){
		Response testTreeResponse = target("ptrees/" + id).request()
			.header(UID, userId).header(ACCESSTOKEN, accesstoken)
			.get(Response.class);
	return testTreeResponse.readEntity(Tree.class);
}
}
