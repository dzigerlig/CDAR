package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ProjectSubNodeDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int kpnid;
	private String title;
	private String wikititle;

	public ProjectSubNodeDao(int kpnid) {
		setKpnid(kpnid);
		setWikititle(String.format("PROJECTSUBNODE_%d", getId()));
	}

	public ProjectSubNodeDao(int kpnid, String title) {
		setKpnid(kpnid);
		setTitle(title);
		setWikititle(String.format("PROJECTSUBNODE_%d", getId()));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKpnid() {
		return kpnid;
	}

	public void setKpnid(int kpnid) {
		this.kpnid = kpnid;
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
	public ProjectSubNodeDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection
					.prepareStatement(
							"INSERT INTO KNOWLEDGEPROJECTSUBNODE (KPNID, TITLE, WIKITITLE) VALUES (?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, getKpnid());
			preparedStatement.setString(2, getTitle());
			preparedStatement.setString(3, getWikititle());

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
	public ProjectSubNodeDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection
					.prepareStatement(
							"UPDATE KNOWLEDGEPROJECTSUBNODE SET LAST_MODIFICATION_TIME = ?, KPNID = ?, TITLE = ? WHERE id = ?",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setInt(2, getKpnid());
			preparedStatement.setString(3, getTitle());
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
		return delete("KNOWLEDGEPROJECTSUBNODE", getId());
	}
}
