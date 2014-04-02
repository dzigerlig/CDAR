package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ProjectNodeDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int kptid;
	private String title;
	private String wikititle;
	private int nodestatus;
	
	public ProjectNodeDao(int kptid) {
		setKptid(kptid);
		setWikititle(String.format("NODE_%d", getId()));
		setNodestatus(0);
	}
	
	public ProjectNodeDao(int kptid, String title) {
		setKptid(kptid);
		setTitle(title);
		setWikititle(String.format("NODE_%d", getId()));
		setNodestatus(0);
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
	
	public int getKptid() {
		return kptid;
	}

	public void setKptid(int kptid) {
		this.kptid = kptid;
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

	public int getNodestatus() {
		return nodestatus;
	}

	public void setNodestatus(int nodestatus) {
		this.nodestatus = nodestatus;
	}

	@Override
	public ProjectNodeDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO KNOWLEDGEPROJECTNODE (TITLE, WIKITITLE, KPTID, NODESTATUS) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, getTitle());
			preparedStatement.setString(2, getWikititle());
			preparedStatement.setInt(3, getKptid());
			preparedStatement.setInt(4, getNodestatus());

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

	@Override
	public ProjectNodeDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE KNOWLEDGEPROJECTNODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, NODESTATUS = ?  WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setString(2, getTitle());
			preparedStatement.setInt(3, getNodestatus());
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
		return delete("KNOWLEDGEPROJECTNODE", getId());
	}
}
