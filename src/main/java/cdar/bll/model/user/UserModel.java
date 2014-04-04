package cdar.bll.model.user;

import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class UserModel {
	private UserDaoController udc = new UserDaoController();

	public UserModel() {
	}

	public User loginUser(String username, String password, boolean isProducer) {
		UserDao userdao = udc.loginUser(username, password);
		if (userdao != null) {
			return new User(userdao);
		}
		return new User(-1);
	}

	public User createUser(User user) {
		try {
			UserDao userdao = new UserDao();
			userdao.setUsername(user.getUsername());
			userdao.setPassword(user.getPassword());
			userdao.create();
			return new User(userdao);
		} catch (Exception ex) {
			return new User(-1);
		}
	}
	
	public User updateUser(User user) {
		try {
			UserDao userdao = udc.getUserById(user.getId());
			System.out.println(userdao.getUsername());
			userdao.setPassword(user.getPassword());
			userdao.update();
			return new User(userdao);
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
