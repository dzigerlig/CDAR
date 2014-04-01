package cdar.dal.persistence.jdbc.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.JDBCUtil;
import cdar.dal.persistence.CdarJdbcHelper;

public class UserDaoController extends CdarJdbcHelper {
	
	public List<UserDao> getUsers() {
		final String getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER";

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<UserDao> users = new ArrayList<UserDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUsers);
			while (result.next()) {
				UserDao user = new UserDao();
				user.setId(result.getInt(1));
				user.setCreationTime(result.getDate(2));
				user.setLastModificationTime(result.getDate(3));
				user.setUsername(result.getString(4));
				user.setPassword(result.getString(5));
				user.setAccesstoken(result.getString(6));
				users.add(user);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return users;
	}

	public UserDao getUserById(int id) {
		final String getUserByIdStatement = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE ID = "
				+ id;

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		UserDao user = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserByIdStatement);
			if (result.next()) {
				user = new UserDao();
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
			if (result.next()) {
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
}
