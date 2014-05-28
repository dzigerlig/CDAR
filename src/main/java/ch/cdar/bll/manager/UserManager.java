package ch.cdar.bll.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import ch.cdar.bll.entity.Tree;
import ch.cdar.bll.entity.User;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.bll.wiki.WikiRegistrationManager;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.exceptions.UsernameInvalidException;
import ch.cdar.dal.exceptions.WikiLoginException;
import ch.cdar.dal.exceptions.WrongCredentialsException;
import ch.cdar.dal.user.UserRepository;
import ch.cdar.dal.wiki.WikiRepository;

/**
 * The Class UserManager.
 */
public class UserManager {
	
	/** The user repository. */
	private UserRepository userRepository = new UserRepository();

	/**
	 * Instantiates a new user manager.
	 */
	public UserManager() {
	}

	/**
	 * Login user.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws WrongCredentialsException the wrong credentials exception
	 */
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

	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @param createWikiUser the create wiki user
	 * @return the user
	 * @throws UsernameInvalidException the username invalid exception
	 */
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

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
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

	/**
	 * Update user.
	 *
	 * @param user the user
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
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

	/**
	 * Gets the user.
	 *
	 * @param username the username
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public User getUser(String username) throws UnknownUserException {
		return userRepository.getUser(username);
	}

	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public User getUser(int userId) throws UnknownUserException,
			EntityException {
		return userRepository.getUser(userId);
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 * @throws EntityException the entity exception
	 */
	public List<User> getUsers() throws EntityException {
		return userRepository.getUsers();
	}

	/**
	 * Gets the users by tree.
	 *
	 * @param isProducer the is producer
	 * @param treeId the tree id
	 * @return the users by tree
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public List<User> getUsersByTree(boolean isProducer, int treeId) throws EntityException, UnknownUserException {
		return userRepository.getUsersByTree(isProducer, treeId);
	}

	/**
	 * Sets the consumer user right.
	 *
	 * @param treeId the tree id
	 * @param user the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public void setConsumerUserRight(int treeId, User user) throws UnknownUserException {
		userRepository.setConsumerUserRight(treeId, user);		
	}

	/**
	 * Sets the producer user right.
	 *
	 * @param treeId the tree id
	 * @param user the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public void setProducerUserRight(int treeId, User user) throws UnknownUserException {
		userRepository.setProducerUserRight(treeId, user);				
	}
}
