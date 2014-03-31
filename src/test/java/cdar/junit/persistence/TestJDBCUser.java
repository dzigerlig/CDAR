package cdar.junit.persistence;

import static org.junit.Assert.assertEquals;

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
}
