package cdar.dal.persistence.jdbc.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.user.User;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.persistence.JDBCUtil;

public class UserRepository {

	public List<User> getUsers() throws Exception {
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER";
		ResultSet result = null;
		List<User> users = new ArrayList<User>();
		try (Connection connection = JDBCUtil.getConnection();
				Statement statement = connection.createStatement()) {
			result = statement.executeQuery(sql);
			while (result.next()) {
				User user = new User();
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
				users.add(user);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return users;
	}

	public User getUser(int id) throws UnknownUserException {
		User user = new User();
		ResultSet result = null;
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE ID = ?";

		try (Connection connection = JDBCUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();
			while (result.next()) {
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
				return user;
			}
		} catch (SQLException e) {
			throw new UnknownUserException();
		}
		throw new UnknownUserException();
	}

	public User getUser(String username) throws UnknownUserException {
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE USERNAME = ?";
		ResultSet result = null;
		User user = new User();

		try (Connection connection = JDBCUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, username);
			result = preparedStatement.executeQuery();

			while (result.next()) {
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
				return user;
			}
		} catch (SQLException e) {
			throw new UnknownUserException();
		}
		throw new UnknownUserException();
	}

	public User createUser(User user) throws Exception {
		final String sql = "INSERT INTO USER (CREATION_TIME, USERNAME, PASSWORD) VALUES (?, ?, ?)";
		ResultSet generatedKeys = null;

		try (Connection connection = JDBCUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setId(generatedKeys.getInt(1));
			}
		} catch (Exception ex) {
			throw ex;
		}
		return user;
	}

	public User updateUser(User user) throws Exception {
		final String sql = "UPDATE USER SET LAST_MODIFICATION_TIME = ?, USERNAME = ?, PASSWORD = ?, ACCESSTOKEN = ? WHERE id = ?";
		ResultSet generatedKeys = null;
		try (Connection connection = JDBCUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getAccesstoken());
			preparedStatement.setInt(5, user.getId());

			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setId(generatedKeys.getInt(1));
			}

		} catch (Exception ex) {
			throw ex;
		}
		return user;
	}

	public boolean deleteUser(User user) throws Exception {
		// prepared statement cannot be used for tablename,
		// preparedstatement is only for the column names
		// http://stackoverflow.com/questions/11312155/how-to-use-a-tablename-variable-for-a-java-prepared-statement-insert
		final String sql = "DELETE FROM USER WHERE ID = ?";
		try (Connection connection = JDBCUtil.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, user.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
