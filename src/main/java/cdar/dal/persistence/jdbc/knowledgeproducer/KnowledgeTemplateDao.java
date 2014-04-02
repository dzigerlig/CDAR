package cdar.dal.persistence.jdbc.knowledgeproducer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class KnowledgeTemplateDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private int ktrid;
	private Date creationTime;
	private Date lastModificationTime;
	private String title;
	private String wikititle;
	
	
	public KnowledgeTemplateDao(int ktrid) {
		setKtrid(ktrid);
	}
	
	public KnowledgeTemplateDao(int ktrid, String title) {
		setKtrid(ktrid);
		setTitle(title);
		setWikititle(String.format("TEMPLATE_%d", getId()));
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWikititle() {
		return wikititle;
	}

	public void setWikititle(String wikititle) {
		this.wikititle = wikititle;
	}

	@Override
	public KnowledgeTemplateDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE KNOWLEDGETEMPLATE SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setString(2, getTitle());
			preparedStatement.setInt(3, getId());

			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				setId(generatedKeys.getInt(1));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return this;
	}

	@Override
	public boolean delete() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String deleteSQL = "DELETE FROM KNOWLEDGETEMPLATE WHERE ID = ?";

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, preparedStatement, null, null);
		}
		return false;
	}

	@Override
	public CdarDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO KNOWLEDGETEMPLATE (TITLE, WIKITITLE, KTRID) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, getTitle());
			preparedStatement.setString(2, getWikititle());
			preparedStatement.setInt(3, getKtrid());

			preparedStatement.executeUpdate();

			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				setId(generatedKeys.getInt(1));
			}
			preparedStatement.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return this;
	}
}
