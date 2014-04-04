package cdar.dal.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CUDHelper extends CdarJdbcHelper {
	protected abstract CUDHelper createVisit(Connection connection, PreparedStatement preparedStatement, ResultSet generatedKeys) throws SQLException;
	protected abstract CUDHelper updateVisit();
	protected abstract CUDHelper deleteVisit();
	
	public CUDHelper create(CUDHelper helper){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			
			createVisit( connection,  preparedStatement, generatedKeys);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return this;
		}
	
	
	
	public CUDHelper update() {
	
		return this;
	}
	public boolean delete() {
		return false;
	}

}
