package cdar.dal.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract  class CUDHelper<T> extends CdarJdbcHelper {
	protected abstract T createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException;

	protected abstract T updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException;

	public  T create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		T object = null;

		try {
			connection = JDBCUtil.getConnection();
			object = createVisit(connection, preparedStatement, generatedKeys);			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return object;
	}

	public T update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		T object = null;

		try {
			connection = JDBCUtil.getConnection();
			object =updateVisit(connection, preparedStatement, generatedKeys);			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return object;
	}
}
