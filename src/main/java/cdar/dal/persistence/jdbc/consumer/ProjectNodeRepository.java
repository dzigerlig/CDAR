package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.consumer.ProjectNode;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.DBConnection;

public class ProjectNodeRepository {
	public List<ProjectNode> getProjectNodes(int projectTreeId) throws UnknownProjectTreeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, NODESTATUS, KPTID FROM KNOWLEDGEPROJECTNODE WHERE KPTID = ?";
		List<ProjectNode> projectNodes = new ArrayList<ProjectNode>();
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(result.getDate(2));
					projectNode.setLastModificationTime(result.getDate(3));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikiTitle(result.getString(5));
					projectNode.setNodeStatus(result.getInt(6));
					projectNode.setRefProjectTreeId(projectTreeId);
					projectNodes.add(projectNode);
				}
			}
		} catch (SQLException e) {
			throw new UnknownProjectTreeException();
		}
		return projectNodes;
	}
	
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, NODESTATUS, KPTID FROM KNOWLEDGEPROJECTNODE WHERE ID = ?";
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectNodeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(result.getDate(2));
					projectNode.setLastModificationTime(result.getDate(3));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikiTitle(result.getString(5));
					projectNode.setNodeStatus(result.getInt(6));
					projectNode.setRefProjectTreeId(result.getInt(7));
					return projectNode;
				}
			}
		} catch (SQLException e) {
			throw new UnknownProjectNodeException();
		}
		throw new UnknownProjectNodeException();
	}
	
	public ProjectNode createProjectNode(ProjectNode projectNode) throws UnknownProjectNodeException, UnknownProjectTreeException {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTNODE (CREATION_TIME, TITLE, WIKITITLE, KPTID, NODESTATUS) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setString(3, projectNode.getWikiTitle());
			preparedStatement.setInt(4, projectNode.getRefProjectTreeId());
			preparedStatement.setInt(5, projectNode.getNodeStatus());
			
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectNode.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
		return projectNode;
	}
	
	public ProjectNode updateProjectNode(ProjectNode projectNode) throws UnknownProjectNodeException {
		final String sql = "UPDATE KNOWLEDGEPROJECTNODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, NODESTATUS = ?  WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setInt(3, projectNode.getNodeStatus());
			preparedStatement.setInt(4, projectNode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}
		return projectNode;
	}
	
	public boolean deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTNODE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectNodeId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownProjectNodeException();
			}
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}
	}
}
