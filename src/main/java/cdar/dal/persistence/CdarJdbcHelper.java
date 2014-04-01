package cdar.dal.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class CdarJdbcHelper {
	protected void closeConnections(Connection connection,
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
}
