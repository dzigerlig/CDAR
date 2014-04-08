package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.producer.Subnode;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class SubnodeDao extends CUDHelper<SubnodeDao> implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int knid;
	private String title;
	private String wikititle;
	
	public SubnodeDao(int knid) {
		setKnid(knid);
		setWikititle(String.format("SUBNODE_%d", getId()));
	}
	
	public SubnodeDao(int knid, String title) {
		setKnid(knid);
		setTitle(title);
		setWikititle(String.format("SUBNODE_%d", getId()));
	}
	
	public SubnodeDao(Subnode subnode) {
		setId(subnode.getId());
		setCreationTime(subnode.getCreationTime());
		setLastModificationTime(subnode.getLastModificationTime());
		setKnid(subnode.getKnid());
		setTitle(subnode.getTitle());
		setWikititle(subnode.getWikiTitle());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getKnid() {
		return knid;
	}

	public void setKnid(int knid) {
		this.knid = knid;
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
	public SubnodeDao create() {
		return super.create();
	}

	@Override
	public SubnodeDao update() {
		return  super.update();
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGESUBNODE", getId());
	}

	@Override
	protected SubnodeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"INSERT INTO KNOWLEDGESUBNODE (CREATION_TIME, KNID, TITLE, WIKITITLE) VALUES (?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getKnid());
		preparedStatement.setString(3, getTitle());
		preparedStatement.setString(4, getWikititle());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected SubnodeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"UPDATE KNOWLEDGESUBNODE SET LAST_MODIFICATION_TIME = ?, KNID = ?, TITLE = ? WHERE id = ?",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getKnid());
		preparedStatement.setString(3, getTitle());
		preparedStatement.setInt(4, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}
}