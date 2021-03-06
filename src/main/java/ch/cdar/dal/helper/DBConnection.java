package ch.cdar.dal.helper;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Class DBConnection.
 */
public class DBConnection {
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public static Connection getConnection() {
		Connection connection = null;
		PropertyHelper propertyHelper = new PropertyHelper();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					propertyHelper.getProperty("CDAR_LOCAL_DB_CONNECTION"),
					propertyHelper.getProperty("CDAR_LOCAL_DB_USER"),
					propertyHelper.getProperty("CDAR_LOCAL_DB_PASSWORD"));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
