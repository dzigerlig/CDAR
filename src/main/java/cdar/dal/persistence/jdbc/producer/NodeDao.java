package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class NodeDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int ktrid;
	private String title;
	private String wikititle;
	private int dynamictreeflag;
	
	public NodeDao(int ktrid) {
		setKtrid(ktrid);
		setWikititle(String.format("NODE_%d", getId()));
		setDynamicTreeFlag(0);
	}
	
	public NodeDao(int ktrid, String title) {
		setKtrid(ktrid);
		setTitle(title);
		setWikititle(String.format("NODE_%d", getId()));
		setDynamicTreeFlag(0);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
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

	public int getDynamicTreeFlag() {
		return dynamictreeflag;
	}

	public void setDynamicTreeFlag(int dynamictreeflag) {
		this.dynamictreeflag = dynamictreeflag;
	}
	
	@Override
	public NodeDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE KNOWLEDGENODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG = ?  WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setString(2, getTitle());
			preparedStatement.setInt(3, getDynamicTreeFlag());
			preparedStatement.setInt(4, getId());

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
		return delete("KNOWLEDGENODE", getId());
	}

	@Override
	public NodeDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO KNOWLEDGENODE (TITLE, WIKITITLE, KTRID, DYNAMICTREEFLAG) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, getTitle());
			preparedStatement.setString(2, getWikititle());
			preparedStatement.setInt(3, getKtrid());
			preparedStatement.setInt(4, getDynamicTreeFlag());

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
