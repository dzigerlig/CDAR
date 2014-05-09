package cdar.dal.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.exceptions.UsernameInvalidException;

public class UserRepository {

	public List<User> getUsers() throws EntityException {
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER";
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
				users.add(user);
			}
		} catch (Exception ex) {
			throw new EntityException();
		}
		return users;
	}

	public User getUser(int id) throws UnknownUserException, EntityException {
		User user = new User();
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					user.setId(result.getInt(1));
					user.setCreationTime(DateHelper.getDate(result.getString(2)));
					user.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
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

	public User getUser(String username) throws UnknownUserException {
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE USERNAME = ?";
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

	public User createUser(User user) throws UsernameInvalidException {
		final String sql = "INSERT INTO USER (CREATION_TIME, USERNAME, PASSWORD) VALUES (?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();

			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setId(generatedKeys.getInt(1));
			}
		} catch (Exception ex) {
			System.out.println("create user exception repo");
			ex.printStackTrace();
			throw new UsernameInvalidException();
		}
		return user;
	}

	public User updateUser(User user) throws UnknownUserException {
		final String sql = "UPDATE USER SET LAST_MODIFICATION_TIME = ?, USERNAME = ?, PASSWORD = ?, ACCESSTOKEN = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getAccesstoken());
			preparedStatement.setInt(5, user.getId());

			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw new UnknownUserException();
		}
		return user;
	}

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

	public List<User> getUsersByTree(int treeId) throws EntityException,
			UnknownUserException {
		final String sql = "SELECT * FROM cdar.user AS user, cdar.knowledgeprojecttreemapping AS mapping WHERE user.id=mapping.uid AND   mapping.kptid=?";
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
					user.setTreeaccess(true);
					users.add(user);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownUserException();
		}
		return users;
	}

	public void setProducerUserRight(int treeId, User user)
			throws UnknownUserException {
		final String sql;
		if (user.isTreeaccess()) {
			sql = "INSERT INTO KNOWLEDGEPROJECTTREEMAPPING (KPTID, UID) VALUES (?, ?)";
		} else {
			sql = "DELETE FROM KNOWLEDGEPROJECTTREEMAPPING WHERE KPTID = ? AND UID = ?";
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
