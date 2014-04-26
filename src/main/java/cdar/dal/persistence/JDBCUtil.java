package cdar.dal.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {
	private static Connection connection = null;

	public static Connection getConnection() {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();

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
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
