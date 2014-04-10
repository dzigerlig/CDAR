package cdar.jersey.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		boolean isTreeDeleted = target(userId + "/ktree/delete")
				.request()
				.post(Entity.entity(treeid, MediaType.APPLICATION_JSON),
						boolean.class);
		int quantityOfTreesAfter = target(userId + "/ktree").request()
				.get(Set.class).size();
assertEquals(true, isTreeDeleted);
		assertEquals(quantityOfTreesBefore - 1, quantityOfTreesAfter);

	}

	@Test
	public void testAddTree() {
		// Tree tree = target(userId+"/ktree/tree/add").queryParam("uid",
		// userId).request().post(
		// Entity.entity(TREENAME, MediaType.APPLICATION_JSON), Tree.class);
	}

}
