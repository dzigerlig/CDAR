package cdar.dal.persistence;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtil {
	private static Connection connection = null;

	private static final String LOCAL_DB_CONNECTION = "jdbc:mysql://localhost:3306/cdar";
	private static final String LOCAL_DB_USER = "root";
	private static final String LOCAL_DB_PASSWORD = "";
	
	private static final String REMOTE_DB_CONNECTION = "jdbc:mysql://ec2-23-21-211-172.compute-1.amazonaws.com:3306/cdar";
	private static final String REMOTE_DB_USER = "cdaruser";
	private static final String REMOTE_DB_PASSWORD = "cdarpassword";
	

	public static Connection getConnection() {
		try {
			String hibernateConfig = System.getProperty("fileName");
			
			if (connection == null || connection.isClosed()) {
				if (hibernateConfig!=null) {
					connection = DriverManager.getConnection(REMOTE_DB_CONNECTION,
    						REMOTE_DB_USER, REMOTE_DB_PASSWORD);
                } else {
                	connection = DriverManager.getConnection(LOCAL_DB_CONNECTION,
    						LOCAL_DB_USER, LOCAL_DB_PASSWORD);
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
