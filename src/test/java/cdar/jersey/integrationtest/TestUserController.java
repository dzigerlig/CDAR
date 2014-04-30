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

import cdar.bll.user.User;
import cdar.pl.controller.UserController;

public class TestUserController extends JerseyTest {
	final String USERNAME = "testuser";
	final String PASSWORD = "testpassword";

	@Override
	protected Application configure() {
		return new ResourceConfig(UserController.class);
	}

	@Before
	public void createUser() {
		int users = target("users").request().get(Set.class).size();

		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);

		Response newUserResponse = target("users/registration").request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON),
						Response.class);
		User newUser = newUserResponse.readEntity(User.class);
		int usersNew = target("users").request().get(Response.class)
				.readEntity(Set.class).size();
		assertEquals(201, newUserResponse.getStatus());

		assertEquals(USERNAME, newUser.getUsername());
		assertEquals(USERNAME, newUser.getUsername());
		assertEquals(users + 1, usersNew);
	}

	@After
	public void testDeleteUser() {
		int users = target("users").request().get(Set.class).size();
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		User loginUser = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class)
				.readEntity(User.class);
		Response deleteStatus = target("users/delete").request().post(
				Entity.entity(loginUser.getId(), MediaType.APPLICATION_JSON),
				Response.class);
		int usersNew = target("users").request().get(Response.class)
				.readEntity(Set.class).size();

		assertEquals(200, deleteStatus.getStatus());
		assertEquals(users - 1, usersNew);
	}

	@Test
	public void testRegisteruserTwice() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		Response postResponse = target("users/registration").request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON),
						Response.class);
		assertEquals(409, postResponse.getStatus());
	}

	@Test
	public void testUserLogin() {
		Response loginUserResponse = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class);
		User loginUser = loginUserResponse.readEntity(User.class);
		assertEquals(200, loginUserResponse.getStatus());
		assertEquals(USERNAME, loginUser.getUsername());
	}

	@Test
	public void testUserWrongLogin() {
		Response loginUserResponse = target("users/login")
				.queryParam("password", "wrongPassword")
				.queryParam("username", USERNAME).request().get(Response.class);
		assertEquals(401, loginUserResponse.getStatus());
	}

	@Test
	public void testUserEdit() {
		int quantityOfUsersBefore = target("users").request().get(Response.class).readEntity(Set.class)
				.size();

		User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		Response beforUserResponse = target("users/login").queryParam("password", PASSWORD)
				.queryParam("username", USERNAME).request().get(Response.class);
		User beforUser =beforUserResponse.readEntity(User.class);
		beforUser.setUsername("hans");
		beforUser.setPassword("123456");

		Response afterUserResponse = target("users/edit").request().post(
				Entity.entity(beforUser, MediaType.APPLICATION_JSON),
				Response.class);
		User afterUser = afterUserResponse.readEntity(User.class);

		assertEquals(beforUser.getUsername(), afterUser.getUsername());
		assertEquals(beforUser.getPassword(), afterUser.getPassword());

		beforUser.setUsername(USERNAME);
		beforUser.setPassword(PASSWORD);

		target("users/edit").request().post(
				Entity.entity(beforUser, MediaType.APPLICATION_JSON),
				Response.class);
		int quantityOfUsersAfter = target("users").request().get(Set.class)
				.size();
		assertEquals(200, afterUserResponse.getStatus());
		assertEquals(quantityOfUsersBefore, quantityOfUsersAfter);

	}

}
