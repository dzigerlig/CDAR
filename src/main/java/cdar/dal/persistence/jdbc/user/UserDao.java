package cdar.dal.persistence.jdbc.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cdar.bll.user.User;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoRepository;
import cdar.dal.persistence.jdbc.consumer.ProjectTreeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoRepository;
import cdar.dal.persistence.jdbc.producer.TreeDao;

@XmlRootElement
public class UserDao extends CUDHelper<UserDao> implements CdarDao {

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

	public UserDao(User user) {
		setId(user.getId());
		setCreationTime(user.getCreationTime());
		setLastModificationTime(user.getLastModificationTime());
		setUsername(user.getUsername());
		setPassword(user.getPassword());
		setAccesstoken(user.getAccesstoken());
	}

	public UserDao(int id) {
		setId(id);
	}

	public int getId() {
		return id;
	}

	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	@XmlElement
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	@XmlElement
	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getUsername() {
		return username;
	}

	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	@XmlElement
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	@Override
	public UserDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			return new UserDao(-1);
		}
	}

	@Override
	public UserDao update() {
		try {
			return super.update();
		} catch (Exception ex) {
			return new UserDao(-1);
		}
	}

	@Override
	public boolean delete() {
		ProducerDaoRepository kpdc = new ProducerDaoRepository();
		ConsumerDaoRepository cdc = new ConsumerDaoRepository();

		for (TreeDao tree : kpdc.getTrees(getId())) {
			tree.delete();
		}

		for (ProjectTreeDao projecttree : cdc.getProjectTrees(getId())) {
			projecttree.delete();
		}

		return delete("USER", getId());
	}

	@Override
	protected UserDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws Exception {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO USER (CREATION_TIME, USERNAME, PASSWORD) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getUsername());
		preparedStatement.setString(3, getPassword());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}

	@Override
	protected UserDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws Exception {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE USER SET LAST_MODIFICATION_TIME = ?, USERNAME = ?, PASSWORD = ?, ACCESSTOKEN = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setString(2, getUsername());
		preparedStatement.setString(3, getPassword());
		preparedStatement.setString(4, getAccesstoken());
		preparedStatement.setInt(5, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}

}
