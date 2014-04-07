package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.consumer.ProjectNode;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class ProjectNodeDao extends CUDHelper<ProjectNodeDao> implements CdarDao {
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
	
	public ProjectNodeDao(ProjectNode projectnode) {
		setId(projectnode.getId());
		setCreationTime(projectnode.getCreationTime());
		setLastModificationTime(projectnode.getLastModificationTime());
		setKptid(projectnode.getRefProjectTreeId());
		setTitle(projectnode.getTitle());
		setWikititle(projectnode.getWikiTitle());
		setNodestatus(projectnode.getNodeStatus());
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
		return super.create();
	}

	@Override
	public ProjectNodeDao update() {
		return super.update();
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGEPROJECTNODE", getId());
	}

	@Override
	protected ProjectNodeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"INSERT INTO KNOWLEDGEPROJECTNODE (CREATION_TIME, TITLE, WIKITITLE, KPTID, NODESTATUS) VALUES (?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		preparedStatement.setString(2, getTitle());
		preparedStatement.setString(3, getWikititle());
		preparedStatement.setInt(4, getKptid());
		preparedStatement.setInt(5, getNodestatus());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected ProjectNodeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
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
		return this;
	}
}
