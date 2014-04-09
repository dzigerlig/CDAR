package cdar.dal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class TestJDBCUser {
	private String testUsername = "UnitTestUser";
	private final String testPassword = "UnitTestPassword";
	private UserDaoController udc = new UserDaoController();
	
	@Before
	public void createTestUser() {
		new UserDao(testUsername, testPassword).create();
	}
	
	@After
	public void deleteTestUser() {
		udc.getUserByName(testUsername).delete();
	}
	
	@Test
	public void testGetUserById() {
		assertEquals(testUsername, udc.getUserById(udc.getUserByName(testUsername).getId()).getUsername());
	}
	
	@Test
	public void testGetUserByUsername() {
		assertEquals(testUsername, udc.getUserByName(testUsername).getUsername());
	}
	
	@Test
	public void testChangePassword() {
		UserDao user = udc.getUserByName(testUsername);
		assertEquals(testPassword, user.getPassword());
		String newPassword = "newPassword";
		user.setPassword(newPassword);
		user.update();
		assertEquals(newPassword, udc.getUserByName(testUsername).getPassword());
	}
	
	@Test
	public void testDeleteUser() {
		final String username = "MyUser";
		final String password = "MyPassword";
		UserDao user = new UserDao(username, password).create();
		assertEquals(password, udc.getUserByName(username).getPassword());
		user.delete();
		assertNull(udc.getUserById(user.getId()));
	}
	
	@Test
	public void testAccessToken() {
		final String accesstoken = "myAccessToken";
		UserDao user = udc.getUserByName(testUsername);
		user.setAccesstoken(accesstoken);
		user.update();
		user = udc.getUserById(user.getId());
		assertEquals(accesstoken, user.getAccesstoken());
	}
	
	@Test
	public void testGetUsers() {
		final int currentUserCount = udc.getUsers().size();
		UserDao user = new UserDao("MyUser", "MyPassword").create();
		assertEquals(currentUserCount+1, udc.getUsers().size());
		user.delete();
	}
}
