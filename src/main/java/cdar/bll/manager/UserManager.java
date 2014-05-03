package cdar.bll.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.manager.consumer.ProjectTreeManager;
import cdar.bll.manager.producer.TreeManager;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.WrongCredentialsException;
import cdar.dal.user.UserRepository;

public class UserManager {
	private UserRepository userRepository = new UserRepository();

	public UserManager() {
	}

	public User loginUser(String username, String password) throws Exception {
		User user = getUser(username);

		if (user.getPassword().equals(password)) {
			user.setAccesstoken(DigestUtils.shaHex(String.format("%s%s", user.getPassword(), new Date().toString())));
			updateUser(user);
			return user;
		} else {
			throw new WrongCredentialsException();
		}
	}

	public User createUser(User user) throws Exception {
		return userRepository.createUser(user);
	}

	public void deleteUser(int userId) throws Exception {
		TreeManager tm = new TreeManager();
		ProjectTreeManager ptm = new ProjectTreeManager();
		
		for (Tree tree : tm.getTrees(userId)) {
			tm.deleteTree(tree.getId());
		}
		
		for (Tree projectTree : ptm.getProjectTrees(userId)) {
			ptm.deleteProjectTree(projectTree.getId());
		}
		
		userRepository.deleteUser(userId);
	}

	public User updateUser(User user) throws Exception {
		return userRepository.updateUser(user);
	}

	public User getUser(String username) throws UnknownUserException {
		return userRepository.getUser(username);
	}

	public User getUser(int userId) throws UnknownUserException {
		return userRepository.getUser(userId);
	}

	public List<User> getUsers() throws Exception {
		return userRepository.getUsers();
	}
}
