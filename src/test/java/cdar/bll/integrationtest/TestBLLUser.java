package cdar.bll.integrationtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cdar.bll.user.User;
import cdar.bll.user.UserManager;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.WrongCredentialsException;

public class TestBLLUser {
	private UserManager um = new UserManager();
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";

	@Test
	public void testCreateUser() throws Exception {
		int usercount = um.getUsers().size();
		User user = um.createUser(username, password);
		assertEquals(usercount + 1, um.getUsers().size());
		assertEquals(user.getId(), um.getUser(user.getUsername()).getId());
		um.deleteUser(user.getId());
		assertEquals(usercount, um.getUsers().size());
	}

	@Test
	public void testCreateUserSameUsername() throws Exception {
		User user = um.createUser(username, password);
		try {
			um.createUser(username, password);
		} catch (Exception ex) {
			assert (true);
		}
		um.deleteUser(user.getId());
	}

	@Test(expected = UnknownUserException.class)
	public void testGetUnknownUserByUsername() throws UnknownUserException {
		um.getUser("Bill Gates");
	}

	@Test(expected = UnknownUserException.class)
	public void testGetUnknownUserById() throws UnknownUserException {
		um.getUser(-13);
	}

	@Test(expected = Exception.class)
	public void testDeleteUnknownUser() throws Exception {
		um.deleteUser(-13);
	}

	@Test
	public void updateUser() throws Exception {
		final String newPassword = "newpassword";
		User user = um.createUser(username, password);
		assertEquals(password, um.getUser(user.getUsername()).getPassword());
		user.setPassword(newPassword);
		um.updateUser(user);
		assertEquals(newPassword, um.getUser(user.getUsername()).getPassword());
		um.deleteUser(user.getId());
	}

	@Test
	public void testLogin() throws Exception {
		User user = um.createUser(username, password);
		assertNull(um.getUser(user.getUsername()).getAccesstoken());
		um.loginUser(user.getUsername(), user.getPassword());
		assert (um.getUser(user.getUsername()).getAccesstoken() != null);
		um.deleteUser(user.getId());
	}

	@Test
	public void testLoginWrongPassword() throws Exception {
		User user = um.createUser(username, password);
		try {
			um.loginUser(user.getUsername(), "fakePassword");
		} catch (WrongCredentialsException ex) {
			assert (true);
		}
		um.deleteUser(user.getId());
	}
}
