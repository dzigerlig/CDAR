package cdar.dal.helpers;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;

public class DBConnection {
	public static Connection getConnection() {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		Connection connection = null;

		try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {

			prop.load(resourceStream);

			String hibernateConfig = System.getProperty("fileName");
			Class.forName("com.mysql.jdbc.Driver");
			if (hibernateConfig != null) {
				connection = DriverManager.getConnection(
						prop.getProperty("REMOTE_DB_CONNECTION"),
						prop.getProperty("REMOTE_DB_USER"),
						prop.getProperty("REMOTE_DB_PASSWORD"));
			} else {
				connection = DriverManager.getConnection(
						prop.getProperty("LOCAL_DB_CONNECTION"),
						prop.getProperty("LOCAL_DB_USER"),
						prop.getProperty("LOCAL_DB_PASSWORD"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return connection;
	}
}
