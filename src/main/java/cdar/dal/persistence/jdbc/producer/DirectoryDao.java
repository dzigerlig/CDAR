package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import cdar.bll.producer.Directory;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class DirectoryDao extends CUDHelper<DirectoryDao> implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int parentid;
	private int ktrid;
	private String title;

	public DirectoryDao(int treeid) {
		setKtrid(treeid);
	}
	
	public DirectoryDao(Directory directory) {
		setId(directory.getId());
		setCreationTime(directory.getCreationTime());
		setLastModificationTime(directory.getLastModificationTime());
		setParentid(directory.getParentid());
		setKtrid(directory.getKtrid());
		setTitle(directory.getTitle());
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

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
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

	@Override
	public DirectoryDao create() {
		return super.create();
	}

	@Override
	public DirectoryDao update() {
		return super.update();
	}

	@Override
	public boolean delete() {
		return delete("DIRECTORY", getId());
	}

	@Override
	protected DirectoryDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO DIRECTORY (CREATION_TIME, PARENTID, KTRID, TITLE) VALUES (?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		if (getParentid() != 0) {
			preparedStatement.setInt(2, getParentid());
		} else {
			preparedStatement.setNull(2, Types.INTEGER);
		}
		preparedStatement.setInt(3, getKtrid());
		preparedStatement.setString(4, getTitle());
		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected DirectoryDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE DIRECTORY SET LAST_MODIFICATION_TIME = ?, PARENTID = ?, KTRID = ?, TITLE = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		if (getParentid() != 0) {
			preparedStatement.setInt(2, getParentid());
		} else {
			preparedStatement.setNull(2, Types.INTEGER);
		}
		preparedStatement.setInt(3, getKtrid());
		preparedStatement.setString(4, getTitle());
		preparedStatement.setInt(5, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}
}
