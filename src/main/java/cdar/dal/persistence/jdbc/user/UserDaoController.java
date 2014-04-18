package cdar.dal.persistence.jdbc.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
		UserDao user = new UserDao(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserByIdStatement);
			while (result.next()) {
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
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return user;
	}

	public UserDao getUserByName(String username) {
		final String getUserByNameStatement = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,USERNAME,PASSWORD,ACCESSTOKEN FROM USER WHERE USERNAME = '"
				+ username + "'";

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		UserDao user = new UserDao(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserByNameStatement);
			while (result.next()) {
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
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return user;
	}

	public UserDao loginUser(String username, String password) {
		UserDao user = getUserByName(username);
		if (user != null && user.getPassword().equals(password)) {
			try {
				user.setAccesstoken(sha1(String.format("%s%s",
						user.getPassword(), new Date().toString())));
				user.update();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return getUserByName(username);
		}

		return null;
	}

	// SOURCE: http://www.sha1-online.com/sha1-java/
	static String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}
}
