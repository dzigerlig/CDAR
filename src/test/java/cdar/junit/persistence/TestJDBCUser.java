package cdar.junit.persistence;

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
		udc.createUser(new UserDao(testUsername, testPassword));
	}
	
	@After
	public void deleteTestUser() {
		udc.deleteUser(udc.getUserByName(testUsername).getId());
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
		udc.updateUser(user);
		assertEquals(newPassword, udc.getUserByName(testUsername).getPassword());
	}
	
	@Test
	public void testDeleteUser() {
		String username = "MyUser";
		String password = "MyPassword";
		UserDao user = udc.createUser(new UserDao(username, password));
		assertEquals(password, udc.getUserByName(username).getPassword());
		udc.deleteUser(udc.getUserById(user.getId()).getId());
		assertNull(udc.getUserById(user.getId()));
	}
}
