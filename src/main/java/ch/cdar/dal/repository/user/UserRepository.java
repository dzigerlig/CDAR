package ch.cdar.dal.repository.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.cdar.bll.entity.User;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.exception.UsernameInvalidException;
import ch.cdar.dal.helper.DBConnection;
import ch.cdar.dal.helper.DBTableHelper;
import ch.cdar.dal.helper.DateHelper;

/**
 * The Class UserRepository.
 */
public class UserRepository {
	/**
	 * Gets the users.
	 *
	 * @return the users
	 * @throws EntityException the entity exception
	 */
	public List<User> getUsers() throws EntityException {
		final String sql = String
				.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN, DRILL_HIERARCHY FROM %s",
						DBTableHelper.USER);
		ResultSet result = null;
		List<User> users = new ArrayList<User>();
		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement()) {
			result = statement.executeQuery(sql);
			while (result.next()) {
				User user = new User();
				user.setId(result.getInt(1));
				user.setCreationTime(DateHelper.getDate(result.getString(2)));
				user.setLastModificationTime(DateHelper.getDate(result
						.getString(3)));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
				user.setDrillHierarchy(result.getInt(7));
				users.add(user);
			}
		} catch (Exception ex) {
			throw new EntityException();
		}
		return users;
	}

	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public User getUser(int userId) throws UnknownUserException, EntityException {
		User user = new User();
		final String sql = String
				.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN, DRILL_HIERARCHY FROM %s WHERE ID = ?",
						DBTableHelper.USER);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, userId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					user.setId(result.getInt(1));
					user.setCreationTime(DateHelper.getDate(result.getString(2)));
					user.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
					user.setDrillHierarchy(result.getInt(7));
					return user;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			throw new UnknownUserException();
		}
		throw new UnknownUserException();
	}

	/**
	 * Gets the user.
	 *
	 * @param username the username
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public User getUser(String username) throws UnknownUserException {
		final String sql = String
				.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN, DRILL_HIERARCHY FROM %s WHERE USERNAME = ?",
						DBTableHelper.USER);
		User user = new User();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, username);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					user.setId(result.getInt(1));
					user.setCreationTime(DateHelper.getDate(result.getString(2)));
					user.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
					user.setDrillHierarchy(result.getInt(7));
					return user;
				}
			} catch (ParseException e) {
				throw new UnknownUserException();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new UnknownUserException();
		}
		throw new UnknownUserException();
	}

	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @return the user
	 * @throws UsernameInvalidException the username invalid exception
	 */
	public User createUser(User user) throws UsernameInvalidException {
		final String sql = String
				.format("INSERT INTO %s (CREATION_TIME, USERNAME, PASSWORD,DRILL_HIERARCHY) VALUES (?, ?, ?,?)",
						DBTableHelper.USER);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setInt(4, 4);

			preparedStatement.executeUpdate();

			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setId(generatedKeys.getInt(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UsernameInvalidException();
		}
		return user;
	}

	/**
	 * Update user.
	 *
	 * @param user the user
	 * @return the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public User updateUser(User user) throws UnknownUserException {
		final String sql = String
				.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, USERNAME = ?, PASSWORD = ?, ACCESSTOKEN = ?, DRILL_HIERARCHY=? WHERE id = ?",
						DBTableHelper.USER);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getAccesstoken());
			preparedStatement.setInt(5, user.getDrillHierarchy());
			preparedStatement.setInt(6, user.getId());

			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw new UnknownUserException();
		}
		return user;
	}

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @throws UnknownUserException the unknown user exception
	 */
	public void deleteUser(int userId) throws UnknownUserException {
		final String sql = "DELETE FROM USER WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, userId);
			if (preparedStatement.executeUpdate() != 1) {
				throw new UnknownUserException();
			}
		} catch (Exception ex) {
			throw new UnknownUserException();
		}
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
	public List<User> getUsersByTree(boolean isProducer, int treeId) throws EntityException,
			UnknownUserException {
		String sql = null;
		if (isProducer) {
			sql = String.format("SELECT * FROM %s AS user, %s AS mapping WHERE user.id=mapping.uid AND   mapping.ktrid=?",
					DBTableHelper.USER, DBTableHelper.TREEMAPPING);
		} else {
			sql = String.format("SELECT * FROM %s AS user, %s AS mapping WHERE user.id=mapping.uid AND   mapping.kptid=?",
							DBTableHelper.USER, DBTableHelper.PROJECTTREEMAPPING);
		}
		List<User> users = new ArrayList<User>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					User user = new User();
					user.setId(result.getInt(1));
					user.setCreationTime(DateHelper.getDate(result.getString(2)));
					user.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
					user.setDrillHierarchy(result.getInt(7));
					user.setTreeaccess(true);
					users.add(user);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new UnknownUserException();
		}
		return users;
	}

	/**
	 * Sets the producer user right.
	 *
	 * @param treeId the tree id
	 * @param user the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public void setProducerUserRight(int treeId, User user)
			throws UnknownUserException {
		final String sql;
		if (user.isTreeaccess()) {
			sql = String.format("INSERT INTO %s (KTRID, UID) VALUES (?, ?)",
					DBTableHelper.TREEMAPPING);
		} else {
			sql = String.format("DELETE FROM %s WHERE KTRID = ? AND UID = ?",
					DBTableHelper.TREEMAPPING);
		}
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, treeId);
			preparedStatement.setInt(2, user.getId());
			if (preparedStatement.executeUpdate() != 1) {
				throw new UnknownUserException();
			}
		} catch (Exception ex) {
			throw new UnknownUserException();
		}
	}

	/**
	 * Sets the consumer user right.
	 *
	 * @param treeId the tree id
	 * @param user the user
	 * @throws UnknownUserException the unknown user exception
	 */
	public void setConsumerUserRight(int treeId, User user)
			throws UnknownUserException {
		final String sql;
		if (user.isTreeaccess()) {
			sql = String.format("INSERT INTO %s (KPTID, UID) VALUES (?, ?)",
					DBTableHelper.PROJECTTREEMAPPING);
		} else {
			sql = String.format("DELETE FROM %s WHERE KPTID = ? AND UID = ?",
					DBTableHelper.PROJECTTREEMAPPING);
		}
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, treeId);
			preparedStatement.setInt(2, user.getId());
			if (preparedStatement.executeUpdate() != 1) {
				throw new UnknownUserException();
			}
		} catch (Exception ex) {
			throw new UnknownUserException();
		}
	}
}
