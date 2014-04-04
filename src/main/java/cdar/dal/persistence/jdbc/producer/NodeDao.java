package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;


public class NodeDao extends CUDHelper<Object> implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int ktrid;
	private String title;
	private String wikititle;
	private int dynamictreeflag;
	private int did;

	public NodeDao(int ktrid, int did) {
		setKtrid(ktrid);
		setWikititle(String.format("NODE_%d", getId()));
		setDynamicTreeFlag(0);
		setDid(did);
	}

	public NodeDao(int ktrid, String title, int did) {
		setKtrid(ktrid);
		setTitle(title);
		setWikititle(String.format("NODE_%d", getId()));
		setDynamicTreeFlag(0);
		setDid(did);
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

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	@Override
	public NodeDao update() {
		return (NodeDao) super.update();
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGENODE", getId());
	}

	@Override
	public NodeDao create() {
		return (NodeDao) super.create();
	}

	@Override
	protected NodeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
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

		preparedStatement = connection
				.prepareStatement("INSERT INTO KNOWLEDGENODEMAPPING (knid, did) VALUES (?, ?)");
		preparedStatement.setInt(1, getId());
		preparedStatement.setInt(2, getDid());	
		preparedStatement.executeUpdate();
		preparedStatement.close();
		return this;
	}

	@Override
	protected NodeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys) throws SQLException {
		preparedStatement = connection
				.prepareStatement(
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

		preparedStatement.close();
		preparedStatement = connection
				.prepareStatement("INSERT INTO KNOWLEDGENODEMAPPING (DID, KNID) VALUES (?, ?)ON DUPLICATE KEY UPDATE DID = ?");
		preparedStatement.setInt(1, getDid());
		preparedStatement.setInt(2, getId());
		preparedStatement.setInt(3, getDid());

		preparedStatement.executeUpdate();
		return this;
	}
}
