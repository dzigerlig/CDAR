package cdar.junit.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cdar.dal.persistence.hibernate.user.UserDaoController;

public class TestHibernateUser {
	
	private UserDaoController udc = new UserDaoController();
	private String testUsername = "UnitTestUser";
	private final String testPassword = "UnitTestPassword";
	
	@Before
	public void createTestUser() {
		udc.createUser(testUsername, testPassword);
	}
	
	@After
	public void deleteTestUser() {
		udc.deleteUserById(udc.getUserByName(testUsername).getId());
	}
	
	@Test
	public void TestUserCreation() {
		String testUsername2 = "JUnitTestUser";
		int userCount = udc.getUsers().size();
		udc.createUser(testUsername2, "ok");
		assertEquals(userCount + 1, udc.getUsers().size());
		udc.deleteUserById(udc.getUserByName(testUsername2).getId());
		assertEquals(userCount, udc.getUsers().size());
	}

	@Test
	public void TestPasswordChange() {
		String newPassword = "newPassword";
		assertEquals(testPassword, udc.getUserByName(testUsername).getPassword());
		udc.setPassword(udc.getUserByName(testUsername).getId(), "newPassword");
		assertEquals(newPassword, udc.getUserByName(testUsername).getPassword());
	}
	
	@Test
	public void TestSetAccessTokenHardcoded() {
		String accessToken = "NEW ACCESS TOKEN";
		assertNull(udc.getUserByName(testUsername).getAccesstoken());
		udc.setAccessToken(udc.getUserByName(testUsername).getId(), accessToken);
		assertEquals(accessToken, udc.getUserByName(testUsername).getAccesstoken());
	}
	
	@Test
	public void TestSetAccessToken() {
		assertNull(udc.getUserByName(testUsername).getAccesstoken());
		udc.setAccessToken(udc.getUserByName(testUsername).getId());
		assertEquals(40, udc.getUserByName(testUsername).getAccesstoken().length());
	}
	
	@Test
	public void TestGetUserByAccessToken() {
		udc.setAccessToken(udc.getUserByName(testUsername).getId());
		String accessToken = udc.getUserByName(testUsername).getAccesstoken();
		assertEquals(testUsername, udc.getUserByAccessToken(accessToken).getUsername());
	}
	
	@Test
	public void TestGetUserByWrongAccessToken() {
		udc.setAccessToken(udc.getUserByName(testUsername).getId());
		assertNull(udc.getUserByAccessToken("doesnt exist"));
	}
}
