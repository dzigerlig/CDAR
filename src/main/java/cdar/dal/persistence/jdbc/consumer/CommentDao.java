package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.consumer.UserComment;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class CommentDao extends CUDHelper<CommentDao> implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private int uid;
	private int kpnid;
	private String comment;
	
	public CommentDao(int kpnid, int uid, String comment) {
		setKpnid(kpnid);
		setUid(uid);
		setComment(comment);
	}
	
	public CommentDao(UserComment comment) {
		setId(comment.getId());
		setCreationTime(comment.getCreationTime());
		setLastModificationTime(comment.getLastModified());
		setUid(comment.getRefUserId());
		setKpnid(comment.getRefProjectNode());
		setComment(comment.getComment());
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

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getKpnid() {
		return kpnid;
	}

	public void setKpnid(int kpnid) {
		this.kpnid = kpnid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public CommentDao create() {
		return super.create();
	}

	@Override
	public CommentDao update() {
		return super.update();
	}

	@Override
	public boolean delete() {
		return delete("USERCOMMENT", getId());
	}

	@Override
	protected CommentDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"INSERT INTO USERCOMMENT (UID, KPNID, COMMENT) VALUES (?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setInt(1, getUid());
		preparedStatement.setInt(2, getKpnid());
		preparedStatement.setString(3, getComment());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();		return this;
	}

	@Override
	protected CommentDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"UPDATE USERCOMMENT SET LAST_MODIFICATION_TIME = ?, UID = ?, KPNID = ?, COMMENT = ?  WHERE id = ?",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		preparedStatement.setInt(2, getUid());
		preparedStatement.setInt(3, getKpnid());
		preparedStatement.setString(4, getComment());
		preparedStatement.setInt(5, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}		return this;
	}
}
