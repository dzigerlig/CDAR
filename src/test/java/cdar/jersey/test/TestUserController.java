package cdar.jersey.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import cdar.bll.user.User;
import cdar.pl.controller.UserController;

public class TestUserController extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class);
	}

	/*
	 * @Test public void testGetUser() { final int users =
	 * target("users").request().get(Set.class).size();
	 * assertEquals("Hello World!", users); }
	 */

	@Test
	public void testRegisterUser() {
		int users = target("users").request().get(Set.class).size();

		User user = new User();
		user.setUsername("testuser");
		user.setPassword("testpassword");

		User postResponse = target("users/registration").request().post(
				Entity.entity(user, MediaType.APPLICATION_JSON), User.class);
		int usersNew = target("users").request().get(Set.class).size();
		assertEquals("testuser", postResponse.getUsername());

		assertEquals("testpassword", postResponse.getPassword());
		assertEquals(users + 1, usersNew);
	}
}
