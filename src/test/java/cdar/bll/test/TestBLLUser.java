package cdar.bll.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cdar.bll.model.user.User;
import cdar.bll.model.user.UserModel;

public class TestBLLUser {
	private UserModel um = new UserModel();
	private final String username = "BLLUsername";
	private final String password = "BLLPassword";

	@Test
	public void testCreateUser() {
		int usercount = um.getUsers().size();
		User user = um.createUser(username, password);
		assertEquals(usercount+1, um.getUsers().size());
		assertEquals(user.getId(), um.getUser(user.getUsername()).getId());
		um.deleteUser(user);
		assertEquals(usercount, um.getUsers().size());
	}
	
	@Test
	public void testCreateUserSameUsername() {
		User user = um.createUser(username, password);
		User user2 = um.createUser(username, password);
		assertEquals(-1, user2.getId());
		assertNull(user2.getUsername());
		assertNull(user2.getPassword());
		assertNull(user2.getAccesstoken());
		um.deleteUser(user);
	}
	
	@Test
	public void updateUser() {
		final String newPassword = "newpassword";
		User user = um.createUser(username, password);
		assertEquals(password, um.getUser(user.getUsername()).getPassword());
		user.setPassword(newPassword);
		um.updateUser(user);
		assertEquals(newPassword, um.getUser(user.getUsername()).getPassword());
		um.deleteUser(user);
	}
	
	@Test
	public void testLogin() {
		User user = um.createUser(username, password);
		assertNull(um.getUser(user.getUsername()).getAccesstoken());
		um.loginUser(user.getUsername(), user.getPassword());
		assert(um.getUser(user.getUsername()).getAccesstoken()!=null);
		um.deleteUser(user);
	}
	
	@Test
	public void testLoginWrongPassword() {
		User user = um.createUser(username, password);
		User loggedInUser = um.loginUser(user.getUsername(), "fakePassword");
		assertEquals(-1, loggedInUser.getId());
		assertNull(loggedInUser.getUsername());
		assertNull(loggedInUser.getPassword());
		assertNull(loggedInUser.getAccesstoken());
		um.deleteUser(user);
	}
}