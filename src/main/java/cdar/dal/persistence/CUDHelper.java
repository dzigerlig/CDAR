package cdar.dal.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract  class CUDHelper<T> extends CdarJdbcHelper {
	
	protected abstract T createVisit(Connection connection, PreparedStatement preparedStatement, ResultSet generatedKeys) throws Exception;

	protected abstract T updateVisit(Connection connection, PreparedStatement preparedStatement, ResultSet generatedKeys) throws Exception;
 
	public T create() throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		T object = null;
 
		try {
			connection = JDBCUtil.getConnection();
			object = createVisit(connection, preparedStatement, generatedKeys);			

		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return object;
	}

	public T update() throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		T object = null;

		try {
			connection = JDBCUtil.getConnection();
			object = updateVisit(connection, preparedStatement, generatedKeys);			

		} catch (Exception ex) {
			throw ex;
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return object;
	}
	
	public boolean delete(String table, int id) {
		boolean returnBool = false;
		if (id != -1) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;

			String deleteSQL = String.format("DELETE FROM %s WHERE ID = %d;",
					table, id);
			try {
				connection = JDBCUtil.getConnection();
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.executeUpdate();
				returnBool = true;
			} catch (Exception ex) {
				returnBool = false;
			} finally {
				closeConnections(connection, preparedStatement, null, null);
			}
		} 
		return returnBool;
	}
}
