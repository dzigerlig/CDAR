package cdar.dal.helpers;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
	public static Connection getConnection() {
		Connection connection = null;
		PropertyHelper propertyHelper = new PropertyHelper();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					propertyHelper.getProperty("LOCAL_DB_CONNECTION"),
					propertyHelper.getProperty("LOCAL_DB_USER"),
					propertyHelper.getProperty("LOCAL_DB_PASSWORD"));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
