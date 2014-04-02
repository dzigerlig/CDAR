package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ProjectNodeLinkDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int sourceid;
	private int targetid;
	private int kpnsnid;
	private int kptid;
	
	public ProjectNodeLinkDao(int sourceid, int targetid, int kptid) {
		setSourceid(sourceid);
		setTargetid(targetid);
		setKptid(kptid);
	}
	
	public ProjectNodeLinkDao(int sourceid, int targetid, int kptid, int kpnsnid) {
		setSourceid(sourceid);
		setTargetid(targetid);
		setKptid(kptid);
		setKpnsnid(kpnsnid);
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
	
	public int getSourceid() {
		return sourceid;
	}

	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}

	public int getTargetid() {
		return targetid;
	}

	public void setTargetid(int targetid) {
		this.targetid = targetid;
	}

	public int getKpnsnid() {
		return kpnsnid;
	}

	public void setKpnsnid(int kpnsnid) {
		this.kpnsnid = kpnsnid;
	}

	public int getKptid() {
		return kptid;
	}

	public void setKptid(int kptid) {
		this.kptid = kptid;
	}

	@Override
	public ProjectNodeLinkDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO KNOWLEDGEPROJECTNODELINK (SOURCEID, TARGETID, KPNSNID, KPTID) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, getSourceid());
			preparedStatement.setInt(2, getTargetid());
			if (getKpnsnid()!=0) {
				preparedStatement.setInt(3, getKpnsnid());
			} else {
				preparedStatement.setNull(3, Types.INTEGER);
			}
			preparedStatement.setInt(4, getKptid());

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
	public ProjectNodeLinkDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE KNOWLEDGEPROJECTNODELINK SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KPNSNID = ?, KPTID = ? WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setInt(2, getSourceid());
			preparedStatement.setInt(3, getTargetid());
			if (getKpnsnid()!=0) {
				preparedStatement.setInt(4, getKpnsnid());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, getKptid());
			preparedStatement.setInt(6, getId());

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
		return delete("KNOWLEDGEPROJECTNODELINK", getId());
	}
}
