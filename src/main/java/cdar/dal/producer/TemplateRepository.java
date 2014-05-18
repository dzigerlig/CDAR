package cdar.dal.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.producer.Template;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;

public class TemplateRepository {
	public List<Template> getTemplates(int treeId) throws EntityException, UnknownTreeException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, ISDEFAULT, DECISIONMADE, ISSUBNODE FROM %s WHERE KTRID = ?",DBTableHelper.TEMPLATE);

		List<Template> templates = new ArrayList<Template>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Template template = new Template();
					template.setTreeId(treeId);
					template.setId(result.getInt(1));
					template.setCreationTime(DateHelper.getDate(result.getString(2)));
					template.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					template.setTitle(result.getString(4));
					template.setTemplatetext(result.getString(5));
					template.setIsDefault(result.getInt(6) == 1);
					template.setDecisionMade(result.getInt(7) == 1);
					template.setIsSubnode(result.getInt(8) == 1);
					templates.add(template);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownTreeException();
		}
		return templates;
	}

	public Template getTemplate(int templateId) throws UnknownTemplateException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, KTRID, ISDEFAULT, DECISIONMADE, ISSUBNODE FROM %s WHERE ID = ?",DBTableHelper.TEMPLATE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, templateId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Template template = new Template();
					template.setTreeId(result.getInt(6));
					template.setId(result.getInt(1));
					template.setCreationTime(DateHelper.getDate(result.getString(2)));
					template.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					template.setTitle(result.getString(4));
					template.setTemplatetext(result.getString(5));
					template.setIsDefault(result.getInt(7) == 1);
					template.setDecisionMade(result.getInt(8) == 1);
					template.setIsSubnode(result.getInt(9) == 1);
					return template;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownTemplateException();
		}
		throw new UnknownTemplateException();
	}
	
	public Template createTemplate(Template template) throws UnknownTemplateException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, TITLE, TEMPLATETEXT, KTRID, ISDEFAULT, DECISIONMADE, ISSUBNODE) VALUES (?, ?, ?, ?, ?, ?, ?)",DBTableHelper.TEMPLATE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, template.getTitle());
			preparedStatement.setString(3, template.getTemplatetext());
			preparedStatement.setInt(4, template.getTreeId());
			if (template.getIsDefault()) {
				preparedStatement.setInt(5, 1);
			} else {
				preparedStatement.setInt(5, 0);
			}
			
			if (template.getDecisionMade()) {
				preparedStatement.setInt(6, 1);
			} else {
				preparedStatement.setInt(6, 0);
			}
			
			if (template.getIsSubnode()) {
				preparedStatement.setInt(7, 1);
			} else {
				preparedStatement.setInt(7, 0);
			}

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){ 
				if (generatedKeys.next()) {
					template.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownTemplateException();
		}
		return template;
	}
	
	public Template updateTemplate(Template template) throws UnknownTemplateException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ?, TEMPLATETEXT = ?, ISDEFAULT = ?, DECISIONMADE = ?, ISSUBNODE = ? WHERE id = ?",DBTableHelper.TEMPLATE);
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, template.getTitle());
			preparedStatement.setString(3, template.getTemplatetext());
			if (template.getIsDefault()) {
				preparedStatement.setInt(4, 1);
			} else {
				preparedStatement.setInt(4, 0);
			}
			if (template.getDecisionMade()) {
				preparedStatement.setInt(5, 1);
			} else {
				preparedStatement.setInt(5, 0);
			}
			if (template.getIsSubnode()) {
				preparedStatement.setInt(6, 1);
			} else {
				preparedStatement.setInt(6, 0);
			}
			preparedStatement.setInt(7, template.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownTemplateException();
		}
		return template;
	}
	
	public void deleteTemplate(int templateId) throws UnknownTemplateException  {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.TEMPLATE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, templateId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownTemplateException();
			}
		} catch (Exception ex) {
			throw new UnknownTemplateException();
		}
	}
}
