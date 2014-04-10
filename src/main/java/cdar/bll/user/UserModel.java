package cdar.bll.user;

import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class UserModel {
	private UserDaoController udc = new UserDaoController();

	public UserModel() {
	}

	public User loginUser(String username, String password) {
		UserDao userdao = udc.loginUser(username, password);
		if (userdao != null) {
			return new User(userdao);
		}
		return new User(-1);
	}

	public User createUser(String username, String password) {
		UserDao userdao = new UserDao();
		userdao.setUsername(username);
		userdao.setPassword(password);
		return new User(userdao.create());
	}

	public Boolean deleteUser(int userid) {
		UserDao userdao = udc.getUserById(userid);
		//UserDao userdao = new UserDao(getUser(user.getUsername()));
		return new Boolean(userdao.delete());
	}

	public User updateUser(User user) {
		UserDao userdao = udc.getUserById(user.getId());
		userdao.setPassword(user.getPassword());
		userdao.setAccesstoken(user.getAccesstoken());
		userdao.update();
		return new User(userdao);
	}

	public User getUser(String username) {
		return new User(udc.getUserByName(username));
	}
	
	public User getUser(int userid) {
		return new User(udc.getUserById(userid));
	}

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		for (UserDao userDao : udc.getUsers()) {
			users.add(new User(userDao));
		}
		return users;
	}
}
