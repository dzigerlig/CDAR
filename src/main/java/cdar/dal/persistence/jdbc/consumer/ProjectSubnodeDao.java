package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.consumer.ProjectSubnode;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class ProjectSubnodeDao extends CUDHelper<ProjectSubnodeDao> implements
		CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int kpnid;
	private String title;
	private String wikititle;
	private int position;

	public ProjectSubnodeDao(int kpnid, int position) {
		setKpnid(kpnid);
		setWikititle(String.format("PROJECTSUBNODE_%d", getId()));
		setPosition(position);
	}

	public ProjectSubnodeDao(int kpnid, int position, String title) {
		setKpnid(kpnid);
		setTitle(title);
		setWikititle(String.format("PROJECTSUBNODE_%d", getId()));
		setPosition(position);
	}

	public ProjectSubnodeDao(ProjectSubnode projectsubnode) {
		setId(projectsubnode.getId());
		setCreationTime(projectsubnode.getCreationTime());
		setLastModificationTime(projectsubnode.getLastModificationTime());
		setKpnid(projectsubnode.getRefProjectNodeId());
		setTitle(projectsubnode.getTitle());
		setWikititle(projectsubnode.getWikiTitle());
	}

	public ProjectSubnodeDao() {
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
	public ProjectSubnodeDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			ProjectSubnodeDao projectSubnodeDao = new ProjectSubnodeDao();
			projectSubnodeDao.setId(-1);
			return projectSubnodeDao;
		}
	}

	@Override
	public ProjectSubnodeDao update() {
		try {
			return super.update();
		} catch (Exception ex) {
			ProjectSubnodeDao projectSubnodeDao = new ProjectSubnodeDao();
			projectSubnodeDao.setId(-1);
			return projectSubnodeDao;
		}
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGEPROJECTSUBNODE", getId());
	}

	@Override
	protected ProjectSubnodeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGEPROJECTSUBNODE (CREATION_TIME, KPNID, TITLE, WIKITITLE, POSITION) VALUES (?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getKpnid());
		preparedStatement.setString(3, getTitle());
		preparedStatement.setString(4, getWikititle());
		preparedStatement.setInt(5, getPosition());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected ProjectSubnodeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGEPROJECTSUBNODE SET LAST_MODIFICATION_TIME = ?, KPNID = ?, TITLE = ?, POSITION = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getKpnid());
		preparedStatement.setString(3, getTitle());
		preparedStatement.setInt(4, getPosition());
		preparedStatement.setInt(5, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
