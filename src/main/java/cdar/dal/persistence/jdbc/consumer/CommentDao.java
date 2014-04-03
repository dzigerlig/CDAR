package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class CommentDao extends CdarJdbcHelper implements CdarDao {
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
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
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
			preparedStatement.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnections(connection, preparedStatement, null, generatedKeys);
		}
		return this;
	}
	
//	CREATE TABLE IF NOT EXISTS cdar.UserComment (
//	  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
//	  creation_time DATETIME NULL,
//	  last_modification_time DATETIME NULL,
//	  uid INT,
//	  kpnid INT,
//	  comment VARCHAR(200) NOT NULL,
//	  FOREIGN KEY (uid) REFERENCES cdar.User (id),
//	  FOREIGN KEY (kpnid) REFERENCES cdar.KnowledgeProjectNode (id)
//	);

	@Override
	public CommentDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
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
		return delete("USERCOMMENT", getId());
	}
}
