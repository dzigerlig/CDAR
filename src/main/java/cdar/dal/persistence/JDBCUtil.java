package cdar.dal.persistence;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtil {
	private static Connection connection = null;

	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/cdar";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(DB_CONNECTION,
						DB_USER, DB_PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
