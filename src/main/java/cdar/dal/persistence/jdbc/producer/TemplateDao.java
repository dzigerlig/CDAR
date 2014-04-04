package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class TemplateDao extends CUDHelper<TemplateDao> implements CdarDao {
	private int id;
	private int ktrid;
	private Date creationTime;
	private Date lastModificationTime;
	private String title;
	private String wikititle;
	
	
	public TemplateDao(int ktrid) {
		setKtrid(ktrid);
		setWikititle(String.format("TEMPLATE_%d", getId()));
	}
	
	public TemplateDao(int ktrid, String title) {
		setKtrid(ktrid);
		setTitle(title);
		setWikititle(String.format("TEMPLATE_%d", getId()));
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
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
	public TemplateDao update() {
		return  super.update();
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGETEMPLATE", getId());
	}

	@Override
	public TemplateDao create() {
		return  super.create();
	}

	@Override
	protected TemplateDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"INSERT INTO KNOWLEDGETEMPLATE (TITLE, WIKITITLE, KTRID) VALUES (?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, getTitle());
		preparedStatement.setString(2, getWikititle());
		preparedStatement.setInt(3, getKtrid());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();		return this;
	}

	@Override
	protected TemplateDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection.prepareStatement(
				"UPDATE KNOWLEDGETEMPLATE SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",
				Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		preparedStatement.setString(2, getTitle());
		preparedStatement.setInt(3, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}		return this;
	}
}
