package cdar.dal.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CdarJdbcHelper {
	public boolean delete(String table, int id) {
		if (id != -1) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;

			String deleteSQL = String.format("DELETE FROM %s WHERE ID = %d;",
					table, id);
			try {
				connection = JDBCUtil.getConnection();
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				closeConnections(connection, preparedStatement, null, null);
			}
		} 
		return false;
	}

	protected void closeConnections(Connection connection,
			PreparedStatement preparedStatement, Statement statement,
			ResultSet generatedKeys) {
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
