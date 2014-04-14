package cdar.bll.user;

import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;
import cdar.pl.wiki.WikiRegister;

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
		try {
			User user = new User(userdao.create());
			//System.out.println(new WikiRegister().userRequest(userdao.getUsername(), userdao.getPassword()));
			return user;
		} catch (Exception e) {
			return new User(-1);
		}
	}

	public Boolean deleteUser(int userid) {
		UserDao userdao = udc.getUserById(userid);
		return new Boolean(userdao.delete());
	}

	public User updateUser(User user) {
		UserDao userdao = udc.getUserById(user.getId());
		userdao.setUsername(user.getUsername());
		userdao.setPassword(user.getPassword());
		userdao.setAccesstoken(user.getAccesstoken());
		try {
			return new User(userdao.update());
		} catch (Exception e) {
			return new User(-1);
		}
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
