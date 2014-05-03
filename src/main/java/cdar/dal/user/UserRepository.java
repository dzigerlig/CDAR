package cdar.dal.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.User;
import cdar.dal.DBConnection;
import cdar.dal.exceptions.UnknownUserException;

public class UserRepository {

	public List<User> getUsers() throws Exception {
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER";
		ResultSet result = null;
		List<User> users = new ArrayList<User>();
		try (Connection connection = DBConnection.getConnection();
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
		final String sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					user.setId(result.getInt(1));
					user.setCreationTime(result.getDate(2));
					user.setLastModificationTime(result.getDate(3));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
					return user;
				}
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
					user.setCreationTime(result.getDate(2));
					user.setLastModificationTime(result.getDate(3));
					user.setUsername(result.getString(4));
					user.setPassword(result.getString(5));
					user.setAccesstoken(result.getString(6));
					return user;
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new UnknownUserException();
		}
		throw new UnknownUserException();
	}

	public User createUser(User user) throws Exception {
		final String sql = "INSERT INTO USER (CREATION_TIME, USERNAME, PASSWORD) VALUES (?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();

			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
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
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getAccesstoken());
			preparedStatement.setInt(5, user.getId());

			preparedStatement.executeUpdate();

		} catch (Exception ex) {
			throw ex;
		}
		return user;
	}

	public void deleteUser(int userId) throws Exception {
		final String sql = "DELETE FROM USER WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, userId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownUserException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
