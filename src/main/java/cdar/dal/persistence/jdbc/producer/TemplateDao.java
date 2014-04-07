package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.bll.producer.Template;
import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class TemplateDao extends CUDHelper<TemplateDao> implements CdarDao {
	private int id;
	private int ktrid;
	private Date creationTime;
	private Date lastModificationTime;
	private String title;
	private String templatetext;

	public TemplateDao(int ktrid) {
		setKtrid(ktrid);
	}

	public TemplateDao(int ktrid, String title, String templatetext) {
		setKtrid(ktrid);
		setTitle(title);
		setTemplatetext(templatetext);
	}
	
	public TemplateDao(Template template) {
		setId(template.getId());
		setKtrid(template.getTreeid());
		setCreationTime(template.getCreationTime());
		setLastModificationTime(template.getLastModified());
		setTitle(template.getTitle());
		setTemplatetext(template.getTemplatetexthtml());
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

	public String getTemplatetext() {
		return templatetext;
	}

	public void setTemplatetext(String templatetext) {
		this.templatetext = templatetext;
	}

	@Override
	public TemplateDao update() {
		return super.update();
	}

	@Override
	public boolean delete() {
		return delete("KNOWLEDGETEMPLATE", getId());
	}

	@Override
	public TemplateDao create() {
		return super.create();
	}

	@Override
	protected TemplateDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGETEMPLATE (TITLE, TEMPLATETEXT, KTRID) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, getTitle());
		preparedStatement.setString(2, getTemplatetext());
		preparedStatement.setInt(3, getKtrid());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected TemplateDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGETEMPLATE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, TEMPLATETEXT = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(0));
		preparedStatement.setString(2, getTitle());
		preparedStatement.setString(3, getTemplatetext());
		preparedStatement.setInt(4, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		return this;
	}
}
