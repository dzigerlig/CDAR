package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class NodeLinkDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int sourceid;
	private int targetid;
	private int ksnid;
	private int ktrid;
	
	public NodeLinkDao(int sourceid, int targetid, int ktrid) {
		setSourceid(sourceid);
		setTargetid(targetid);
		setKtrid(ktrid);
	}
	
	public NodeLinkDao(int sourceid, int targetid, int ktrid, int ksnid) {
		setSourceid(sourceid);
		setTargetid(targetid);
		setKtrid(ktrid);
		setKsnid(ksnid);
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

	public int getKsnid() {
		return ksnid;
	}

	public void setKsnid(int ksnid) {
		this.ksnid = ksnid;
	}
	
	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	@Override
	public NodeLinkDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO NODELINK (SOURCEID, TARGETID, KSNID, KTRID) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, getSourceid());
			preparedStatement.setInt(2, getTargetid());
			if (getKsnid()!=0) {
				preparedStatement.setInt(3, getKsnid());
			} else {
				preparedStatement.setNull(3, Types.INTEGER);
			}
			preparedStatement.setInt(4, getKtrid());

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
	public NodeLinkDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE NODELINK SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KSNID = ?, KTRID = ? WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setInt(2, getSourceid());
			preparedStatement.setInt(3, getTargetid());
			if (getKsnid()!=0) {
				preparedStatement.setInt(4, getKsnid());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, getKtrid());
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
		return delete("NODELINK", getId());
	}
}