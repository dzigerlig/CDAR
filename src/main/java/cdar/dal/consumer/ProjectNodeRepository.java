package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeRepository {
	public List<ProjectNode> getProjectNodes(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		final String sql = "SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, MAPPING.PDID FROM KNOWLEDGEPROJECTNODE AS PNODE, KNOWLEDGEPROJECTNODEMAPPING AS MAPPING WHERE PNODE.KPTID = ? AND PNODE.ID = MAPPING.KPNID";
		List<ProjectNode> projectNodes = new ArrayList<ProjectNode>();
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectNode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikititle(result.getString(5));
					projectNode.setDynamicTreeFlag(result.getInt(6));
					projectNode.setStatus(result.getInt(7));
					projectNode.setDirectoryId(result.getInt(8));
					projectNode.setTreeId(projectTreeId);
					projectNodes.add(projectNode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			throw new UnknownProjectTreeException();
		}
		return projectNodes;
	}
	
	public ProjectNode getProjectNode(int projectNodeId) throws UnknownProjectNodeException, EntityException {
		final String sql = "SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, PNODE.KPTID, MAPPING.PDID FROM KNOWLEDGEPROJECTNODE AS PNODE, KNOWLEDGEPROJECTNODEMAPPING AS MAPPING WHERE PNODE.ID = ? AND PNODE.ID = MAPPING.KPNID";
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectNodeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectNode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikititle(result.getString(5));
					projectNode.setDynamicTreeFlag(result.getInt(6));
					projectNode.setStatus(result.getInt(7));
					projectNode.setTreeId(result.getInt(8));
					projectNode.setDirectoryId(result.getInt(9));
					return projectNode;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			throw new UnknownProjectNodeException();
		}
		throw new UnknownProjectNodeException();
	}
	
	public ProjectNode createProjectNode(ProjectNode projectNode) throws UnknownProjectTreeException {
		final String sqlProjectNode = "INSERT INTO KNOWLEDGEPROJECTNODE (CREATION_TIME, TITLE, KPTID, WIKITITLE, DYNAMICTREEFLAG, NODESTATUS) VALUES (?, ?, ?, ?, ?, ?)";
		final String sqlMapping = "INSERT INTO KNOWLEDGEPROJECTNODEMAPPING (kpnid, pdid) VALUES (?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlProjectNode, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setInt(3, projectNode.getTreeId());
			preparedStatement.setString(4, projectNode.getWikititle());
			preparedStatement.setInt(5, projectNode.getDynamicTreeFlag());
			preparedStatement.setInt(6, 0);
			
			preparedStatement.executeUpdate();
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectNode.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
		
		// projectNodeMapping
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, projectNode.getId());
			preparedStatement.setInt(2, projectNode.getDirectoryId());
			preparedStatement.executeUpdate();
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
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setInt(3, projectNode.getStatus());
			preparedStatement.setInt(4, projectNode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}
		return projectNode;
	}
	
	public void deleteProjectNode(int projectNodeId) throws UnknownProjectNodeException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTNODE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectNodeId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownProjectNodeException();
			}
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}
	}
}
