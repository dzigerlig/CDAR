package cdar.bll.model.user;

import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.hibernate.user.UserDao;
import cdar.dal.persistence.hibernate.user.UserDaoController;

public class UserModel {
	private UserDaoController udc = new UserDaoController();

	public UserModel() {
	}

	public User loginUser(String username, String password, boolean isProducer) {
		UserDao user = udc.loginUser(username, password);
		if (user != null) {
			return new User(user.getUsername(), user.getAccesstoken(), isProducer);
		}
		return new User(-1);
	}

	public User createUser(User user) {
		try {
			return udc.createUser(user);
		} catch (Exception ex) {
			return new User(-1);
		}
	}

	public User getUser(String username) {
		return new User(udc.getUserByName(username));
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		for (UserDao userDao : udc.getUsers()) {
			users.add(new User(userDao));
		}
		return users;
	}
}
