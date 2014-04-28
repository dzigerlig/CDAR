package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.consumer.ProjectTree;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.DBConnection;

public class ProjectTreeRepository {
	public List<ProjectTree> getProjectTrees(int uid) throws SQLException {
		String sql = null;
		if (uid==0) {
			sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGEPROJECTTREE";
		} else {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGEPROJECTTREE LEFT JOIN knowledgeprojecttreemapping ON knowledgeprojecttreemapping.kptid = knowledgeprojecttree.id where knowledgeprojecttreemapping.uid = %d;", uid);
		}
		

		List<ProjectTree> projectTrees = new ArrayList<ProjectTree>();

		try (Connection connection = DBConnection.getConnection(); Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(sql)) {
				while (result.next()) {
					ProjectTree projectTree = new ProjectTree();
					projectTree.setId(result.getInt(1));
					projectTree.setCreationTime(result.getDate(2));
					projectTree.setLastModificationTime(result.getDate(3));
					projectTree.setTitle(result.getString(4));
					projectTree.setUid(uid);
					projectTrees.add(projectTree);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return projectTrees;
	}
	
	public ProjectTree getProjectTree(int id) throws UnknownProjectTreeException {
		final String sql = "SELECT UID,ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGEPROJECTTREE JOIN KNOWLEDGEPROJECTTREEMAPPING ON KNOWLEDGEPROJECTTREEMAPPING.kptid = KNOWLEDGEPROJECTTREE.id WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {

			try (ResultSet result = preparedStatement.executeQuery()) {
				if (result.next()) {
					ProjectTree projectTree = new ProjectTree();
					projectTree.setUid(result.getInt(1));
					projectTree.setId(result.getInt(2));
					projectTree.setCreationTime(result.getDate(3));
					projectTree.setLastModificationTime(result.getDate(4));
					projectTree.setTitle(result.getString(5));
					return projectTree;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownProjectTreeException();
		}
		throw new UnknownProjectTreeException();
	}
	
	public ProjectTree createProjectTree(ProjectTree projectTree) throws Exception {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTTREE (CREATION_TIME, TITLE) VALUES (?, ?)";
		final String sql2 = "INSERT INTO KNOWLEDGEPROJECTTREEMAPPING (uid, kptid) VALUES (?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, projectTree.getTitle());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectTree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
			preparedStatement.setInt(1, projectTree.getUid());
			preparedStatement.setInt(2, projectTree.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return projectTree;
	}
	
	public ProjectTree updateProjectTree(ProjectTree projectTree) throws Exception {
		final String sql = "UPDATE KNOWLEDGEPROJECTTREE SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, projectTree.getTitle());
			preparedStatement.setInt(3, projectTree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return projectTree;
	}
	
	public boolean deleteProjectTree(int projectTreeId) throws UnknownProjectTreeException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTTREE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectTreeId);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
	}
}
