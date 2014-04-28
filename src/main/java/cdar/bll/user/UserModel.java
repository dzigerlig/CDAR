package cdar.bll.user;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.WrongCredentialsException;
import cdar.dal.persistence.jdbc.user.UserRepository;

public class UserModel {
	private UserRepository userRepository = new UserRepository();

	public UserModel() {
	}

	public User loginUser(String username, String password) throws Exception {
		User user = getUser(username);

		if (user.getPassword().equals(password)) {
			user.setAccesstoken(DigestUtils.shaHex(user.getPassword()));
			updateUser(user);
			return user;
		} else {
			throw new WrongCredentialsException();
		}
	}

	public User createUser(String username, String password) throws Exception {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);

		return userRepository.createUser(user);
	}

	public boolean deleteUser(int userId) throws Exception {
		User user;
		user = userRepository.getUser(userId);
		return userRepository.deleteUser(user);
	}

	public User updateUser(User user) throws Exception {
		return userRepository.updateUser(user);
	}

	public User getUser(String username) throws UnknownUserException {
		return userRepository.getUser(username);
	}

	public User getUser(int userid) throws UnknownUserException {
		return userRepository.getUser(userid);
	}

	public List<User> getUsers() throws Exception {
		return userRepository.getUsers();
	}
}
