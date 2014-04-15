package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.consumer.ProjectTree;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class ProjectTreeDao extends CUDHelper<ProjectTreeDao> implements
		CdarDao {
	private int id;
	private int uid;
	private Date creationTime;
	private Date lastModificationTime;
	private String name;

	public ProjectTreeDao(int uid) {
		setUid(uid);
	}

	public ProjectTreeDao(int uid, String name) {
		setUid(uid);
		setName(name);
	}

	public ProjectTreeDao(ProjectTree projecttree) {
		setId(projecttree.getId());
		setCreationTime(projecttree.getCreationTime());
		setLastModificationTime(projecttree.getLastModificationTime());
		setName(projecttree.getTitle());
	}

	public ProjectTreeDao() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ProjectTreeDao update() {
		try {
			return super.update();
		} catch (Exception ex) {
			ProjectTreeDao projectTreeDao = new ProjectTreeDao();
			projectTreeDao.setId(-1);
			return projectTreeDao;
		}
	}

	public boolean delete() {
		return delete("KNOWLEDGEPROJECTTREE", getId());
	}

	@Override
	public ProjectTreeDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			ProjectTreeDao projectTreeDao = new ProjectTreeDao();
			projectTreeDao.setId(-1);
			return projectTreeDao;
		}
	}

	@Override
	protected ProjectTreeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGEPROJECTTREE (CREATION_TIME, NAME) VALUES (?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getName());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		preparedStatement = connection
				.prepareStatement("INSERT INTO KNOWLEDGEPROJECTTREEMAPPING (uid, kptid) VALUES (?, ?)");
		preparedStatement.setInt(1, getUid());
		preparedStatement.setInt(2, getId());
		preparedStatement.executeUpdate();
		return this;
	}

	@Override
	protected ProjectTreeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGEPROJECTTREE SET LAST_MODIFICATION_TIME = ?, NAME = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getName());
		preparedStatement.setInt(3, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}
}
