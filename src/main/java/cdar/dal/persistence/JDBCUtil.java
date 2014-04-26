package cdar.dal.persistence;

import java.io.File;
import java.io.FileInputStream;
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
		Properties prop = new Properties();

		try {
			File file = new File("src/main/resources/cdarconfig.properties");
			prop.load(new FileInputStream(file));

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
