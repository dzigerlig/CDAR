package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.producer.Node;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class NodeDao extends CUDHelper<NodeDao> implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int ktrid;
	private String title;
	private String wikititle;
	private int dynamictreeflag;
	private int did;

	public NodeDao() {
	}

	public NodeDao(int ktrid, int did) {
		setKtrid(ktrid);
		setDynamicTreeFlag(0);
		setDid(did);
	}

	public NodeDao(int ktrid, String title, int did) {
		setKtrid(ktrid);
		setTitle(title);
		setDynamicTreeFlag(0);
		setDid(did);
	}

	public NodeDao(Node node) {
		setId(node.getId());
		setCreationTime(node.getCreationTime());
		setLastModificationTime(node.getLastModificationTime());
		setKtrid(node.getKtrid());
		setTitle(node.getTitle());
		setWikititle(node.getWikiTitle());
		setDynamicTreeFlag(node.getDynamicTreeFlag());
		setDid(node.getDid());
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
		try {
			return super.update();
		} catch (Exception ex) {
			NodeDao nodeDao = new NodeDao();
			nodeDao.setId(-1);
			return nodeDao;
		}
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGENODE", getId());
	}

	@Override
	public NodeDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			NodeDao nodeDao = new NodeDao();
			nodeDao.setId(-1);
			return nodeDao;
		}
	}

	@Override
	protected NodeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGENODE (CREATION_TIME, TITLE, KTRID, DYNAMICTREEFLAG) VALUES (?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getTitle());
		preparedStatement.setInt(3, getKtrid());
		preparedStatement.setInt(4, getDynamicTreeFlag());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
			setWikititle(String.format("NODE_%d", getId()));
		}
		preparedStatement.close();

		// update wikititle
		preparedStatement = connection.prepareStatement(String.format(
				"UPDATE KNOWLEDGENODE SET WIKITITLE = ? where id = %d;",
				getId()));
		preparedStatement.setString(1, getWikititle());
		preparedStatement.executeUpdate();
		preparedStatement.close();

		if (getDid() != 0) {
			preparedStatement = connection
					.prepareStatement("INSERT INTO KNOWLEDGENODEMAPPING (knid, did) VALUES (?, ?)");
			preparedStatement.setInt(1, getId());
			preparedStatement.setInt(2, getDid());
			preparedStatement.executeUpdate();
		}
		return this;
	}

	@Override
	protected NodeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGENODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG = ?  WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
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
				.prepareStatement("INSERT INTO KNOWLEDGENODEMAPPING (DID, KNID) VALUES (?, ?) ON DUPLICATE KEY UPDATE DID = ?");
		preparedStatement.setInt(1, getDid());
		preparedStatement.setInt(2, getId());
		preparedStatement.setInt(3, getDid());

		preparedStatement.executeUpdate();

		return this;
	}
}
