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

import cdar.bll.user.User;
import cdar.pl.controller.UserController;

public class TestUserController extends JerseyTest {
	final String USERNAME = "testuser";
	final String PASSWORD = "testpassword";	

	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class);
	}

	/*
	 * @Test public void testGetUser() { final int users =
	 * target("users").request().get(Set.class).size();
	 * assertEquals("Hello World!", users); }
	 */
	
	@Before
	public void createUser() {
		int users = target("users").request().get(Set.class).size();

		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);

		User postResponse = target("users/registration").request().post(
				Entity.entity(user, MediaType.APPLICATION_JSON), User.class);
		int usersNew = target("users").request().get(Set.class).size();
		assertEquals(USERNAME, postResponse.getUsername());

		assertEquals(USERNAME, postResponse.getUsername());
		assertEquals(users + 1, usersNew);	
		}
	
	@After
	public void deleteUser() {
		int users = target("users").request().get(Set.class).size();
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		User loginUser = target("users/login").queryParam("password", PASSWORD).queryParam("username", USERNAME).request().get(
				User.class);
		Boolean boolDelete = target("users/delete").request().post(
				Entity.entity(loginUser.getId(), MediaType.APPLICATION_JSON), Boolean.class);
		int usersNew = target("users").request().get(Set.class).size();
		assertEquals(true, boolDelete);
		assertEquals(users - 1, usersNew);
		}
	

	@Test
	public void userlogin() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		User loginUser = target("users/login").queryParam("password", PASSWORD).queryParam("username", USERNAME).request().get(
				User.class);
		assertEquals(USERNAME, loginUser.getUsername());
	}
}
