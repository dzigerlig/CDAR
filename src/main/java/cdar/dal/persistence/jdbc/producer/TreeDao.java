package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.producer.Tree;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class TreeDao extends CUDHelper<TreeDao> implements CdarDao {
	private int id;
	private int uid;
	private Date creationTime;
	private Date lastModificationTime;
	private String title;

	public TreeDao() {
	}

	public TreeDao(int uid) {
		setUid(uid);
	}

	public TreeDao(int uid, String title) {
		setUid(uid);
		setTitle(title);
	}

	public TreeDao(Tree tree) {
		setId(tree.getId());
		setCreationTime(tree.getCreationTime());
		setLastModificationTime(tree.getLastModificationTime());
		setTitle(tree.getTitle());
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public TreeDao update() {
		try {
			return super.update();
		} catch (Exception ex) {
			TreeDao treeDao = new TreeDao();
			treeDao.setId(-1);
			return treeDao;
		}
	}

	public boolean delete() {
		return delete("KNOWLEDGETREE", getId());
	}

	@Override
	public TreeDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			TreeDao treeDao = new TreeDao();
			treeDao.setId(-1);
			return treeDao;
		}
	}

	@Override
	protected TreeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGETREE (CREATION_TIME, TITLE) VALUES (?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getTitle());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		preparedStatement = connection
				.prepareStatement("INSERT INTO KNOWLEDGETREEMAPPING (uid, ktrid) VALUES (?, ?)");
		preparedStatement.setInt(1, getUid());
		preparedStatement.setInt(2, getId());
		preparedStatement.executeUpdate();
		return this;
	}

	@Override
	protected TreeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGETREE SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getTitle());
		preparedStatement.setInt(3, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}
}
