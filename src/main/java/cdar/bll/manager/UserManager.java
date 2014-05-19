package cdar.bll.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.wikipedia.Wiki;

import cdar.bll.UserRole;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.bll.manager.both.TreeManager;
import cdar.bll.wiki.WikiRegistrationManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UsernameInvalidException;
import cdar.dal.exceptions.WikiLoginException;
import cdar.dal.exceptions.WrongCredentialsException;
import cdar.dal.user.UserRepository;
import cdar.dal.wiki.WikiRepository;

public class UserManager {
	private UserRepository userRepository = new UserRepository();

	public UserManager() {
	}

	public User loginUser(String username, String password)
			throws UnknownUserException, EntityException,
			WrongCredentialsException {
		User user = getUser(username);

		if (user.getPassword().equals(password)) {
			try {
				WikiRepository wikiConnection = new WikiRepository();
				wikiConnection.getConnection(username, password);
			} catch (WikiLoginException ex) {
				System.out.println(ex.getMessage());
				throw new WrongCredentialsException();
			}
			user.setAccesstoken(DigestUtils.shaHex(String.format("%s%s",
					user.getPassword(), new Date().toString())));
			updateUser(user);
			return user;
		} else {
			throw new WrongCredentialsException();
		}
	}

	public User createUser(User user, boolean createWikiUser)
			throws UsernameInvalidException {
		try {
			if (createWikiUser) {
				try {
					WikiRepository wikiConnection = new WikiRepository();
					wikiConnection.getConnection(user.getUsername(), user.getPassword());
				} catch (WikiLoginException ex) {
					WikiRegistrationManager wrm = new WikiRegistrationManager();
					wrm.createUser(user.getUsername(), user.getPassword());
				}
			}
			return userRepository.createUser(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UsernameInvalidException();
		}
	}

	public void deleteUser(int userId) throws UnknownUserException,
			EntityException, UnknownTreeException, UnknownProjectTreeException {
		TreeManager tm = new TreeManager(UserRole.PRODUCER);
		TreeManager ptm = new TreeManager(UserRole.CONSUMER);

		for (Tree tree : tm.getTrees(userId)) {
			tm.deleteTree(tree.getId());
		}

		for (Tree projectTree : ptm.getTrees(userId)) {
			ptm.deleteTree(projectTree.getId());
		}

		userRepository.deleteUser(userId);
	}

	public User updateUser(User user) throws UnknownUserException,
			EntityException {
		User updatedUser = userRepository.getUser(user.getId());

		if (user.getAccesstoken() != null) {
			updatedUser.setAccesstoken(user.getAccesstoken());
		}

		if (user.getPassword() != null) {
			updatedUser.setPassword(user.getPassword());
		}
		if (user.getDrillHierarchy() != 0) {
			updatedUser.setDrillHierarchy(user.getDrillHierarchy());
		}

		return userRepository.updateUser(updatedUser);
	}

	public User getUser(String username) throws UnknownUserException {
		return userRepository.getUser(username);
	}

	public User getUser(int userId) throws UnknownUserException,
			EntityException {
		return userRepository.getUser(userId);
	}

	public List<User> getUsers() throws EntityException {
		return userRepository.getUsers();
	}

	public List<User> getUsersByTree(boolean isProducer, int treeId) throws EntityException, UnknownUserException {
		return userRepository.getUsersByTree(isProducer, treeId);
	}

	public void setConsumerUserRight(int treeId, User user) throws UnknownUserException {
		userRepository.setConsumerUserRight(treeId, user);		
	}

	public void setProducerUserRight(int treeId, User user) throws UnknownUserException {
		userRepository.setProducerUserRight(treeId, user);				
	}
}
