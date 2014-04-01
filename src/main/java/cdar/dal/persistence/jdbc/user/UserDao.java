package cdar.dal.persistence.jdbc.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeProducerDaoController;
import cdar.dal.persistence.jdbc.knowledgeproducer.KnowledgeTreeDao;

public class UserDao extends CdarJdbcHelper implements CdarDao {

	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private String username;
	private String password;
	private String accesstoken;
	
	public UserDao() {
		
	}
	
	public UserDao(String username, String password) {
		setUsername(username);
		setPassword(password);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	@Override
	public UserDao create() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"INSERT INTO USER (USERNAME, PASSWORD) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, getUsername());
			preparedStatement.setString(2, getPassword());

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
	public UserDao update() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE USER SET LAST_MODIFICATION_TIME = ?, USERNAME = ?, PASSWORD = ?, ACCESSTOKEN = ? WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, new java.sql.Date(0));
			preparedStatement.setString(2, getUsername());
			preparedStatement.setString(3, getPassword());
			preparedStatement.setString(4, getAccesstoken());
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
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String deleteSQL = "DELETE FROM USER WHERE ID = ?";
		
		//delete knowledgetrees
		KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();
		for (KnowledgeTreeDao tree : kpdc.getTrees(id)) {
			kpdc.deleteTree(tree.getId());
		}

		try {
			connection = JDBCUtil.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			closeConnections(connection, preparedStatement, null, null);
		}
		return true;
	}	
}
