package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.producer.Template;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.persistence.DBConnection;

public class TemplateRepository {
	public List<Template> getTemplates(int treeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, ISDEFAULT, DECISIONMADE FROM KNOWLEDGETEMPLATE WHERE KTRID = ?";

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
					template.setCreationTime(result.getDate(2));
					template.setLastModificationTime(result.getDate(3));
					template.setTitle(result.getString(4));
					template.setTemplatetext(result.getString(5));
					template.setIsDefault(result.getInt(6) == 1);
					template.setDecisionMade(result.getInt(7) == 1);
					templates.add(template);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return templates;
	}

	public Template getTemplate(int id) throws UnknownTemplateException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, KTRID, ISDEFAULT, DECISIONMADE FROM KNOWLEDGETEMPLATE WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Template template = new Template();
					template.setTreeId(result.getInt(6));
					template.setId(result.getInt(1));
					template.setCreationTime(result.getDate(2));
					template.setLastModificationTime(result.getDate(3));
					template.setTitle(result.getString(4));
					template.setTemplatetext(result.getString(5));
					template.setIsDefault(result.getInt(7) == 1);
					template.setDecisionMade(result.getInt(8) == 1);
					return template;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownTemplateException();
		}
		throw new UnknownTemplateException();
	}
	
	public Template createTemplate(Template template) throws UnknownTemplateException {
		final String sql = "INSERT INTO KNOWLEDGETEMPLATE (CREATION_TIME, TITLE, TEMPLATETEXT, KTRID, ISDEFAULT, DECISIONMADE) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
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
		final String sql = "UPDATE KNOWLEDGETEMPLATE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, TEMPLATETEXT = ?, ISDEFAULT = ?, DECISIONMADE = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
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
			preparedStatement.setInt(6, template.getId());
			preparedStatement.executeUpdate();
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					template.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownTemplateException();
		}
		return template;
	}
	
	public boolean deleteTemplate(Template template) throws UnknownTemplateException  {
		final String sql = "DELETE FROM KNOWLEDGETEMPLATE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, template.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception ex) {
			throw new UnknownTemplateException();
		}
	}
}
