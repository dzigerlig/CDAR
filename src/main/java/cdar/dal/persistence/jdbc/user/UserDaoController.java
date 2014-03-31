package cdar.dal.persistence.jdbc.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cdar.dal.persistence.JDBCUtil;

public class UserDaoController {

	public UserDao getUserById(int id) {
		final String getUserByIdStatement = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE ID = "
				+ id;

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		UserDao user = new UserDao();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserByIdStatement);
			if (result != null) {
				result.next();
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return user;
	}

	public UserDao getUserByName(String username) {
		final String getUserByIdStatement = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE USERNAME = '"
				+ username +"'";

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		UserDao user = new UserDao();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserByIdStatement);
			if (result != null) {
				result.next();
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return user;
	}

	public UserDao createUser(UserDao user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO USER (USERNAME, PASSWORD) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());

			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setId(generatedKeys.getInt(1));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return user;
	}

	private void closeConnections(Connection connection,
			PreparedStatement preparedStatement, Statement statement, ResultSet generatedKeys) {
		try {
			if (generatedKeys != null)
				generatedKeys.close();
			if (preparedStatement != null)
				preparedStatement.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void deleteUser(int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String deleteSQL = "DELETE FROM USER WHERE ID = ?";

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, preparedStatement, null, null);
		}
	}
}
