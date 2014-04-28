package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.consumer.ProjectSubnode;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;
import cdar.dal.persistence.DBConnection;

public class ProjectSubnodeRepository {
	public List<ProjectSubnode> getProjectSubnodes(int kpnid) throws UnknownProjectNodeLinkException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION FROM KNOWLEDGEPROJECTSUBNODE WHERE KPNID = ?";

		List<ProjectSubnode> projectsubnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)){
			preparedStatement.setInt(1, kpnid);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode projectSubnode = new ProjectSubnode();
					projectSubnode.setId(result.getInt(1));
					projectSubnode.setCreationTime(result.getDate(2));
					projectSubnode.setLastModificationTime(result.getDate(3));
					projectSubnode.setTitle(result.getString(4));
					projectSubnode.setWikiTitle(result.getString(5));
					projectSubnode.setPosition(result.getInt(6));
					projectSubnode.setRefProjectNodeId(kpnid);
					projectsubnodes.add(projectSubnode);
				}
			}
		} catch (SQLException ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectsubnodes;
	}
	
	public ProjectSubnode getProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, TITLE, WIKITITLE, POSITION FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = ?";


		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectSubnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode projectSubnode = new ProjectSubnode();
					projectSubnode.setId(result.getInt(1));
					projectSubnode.setCreationTime(result.getDate(2));
					projectSubnode.setLastModificationTime(result.getDate(3));
					projectSubnode.setTitle(result.getString(5));
					projectSubnode.setWikiTitle(result.getString(6));
					projectSubnode.setRefProjectNodeId(result.getInt(4));
					projectSubnode.setPosition(result.getInt(7));
					return projectSubnode;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownProjectSubnodeException();
		}
		throw new UnknownProjectSubnodeException();
	}
	public int getNextProjectSubnodePosition(int projectNodeId) throws UnknownProjectNodeLinkException {
		int position = 0;
		
		for (ProjectSubnode projectSubnode : getProjectSubnodes(projectNodeId)) {
			if (projectSubnode.getPosition() > position) {
				position = projectSubnode.getPosition();
			}
		}
		
		return ++position;
	}
	
	public ProjectSubnode createProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeException {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTSUBNODE (CREATION_TIME, KPNID, TITLE, WIKITITLE, POSITION) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, projectSubnode.getRefProjectNodeId());
			preparedStatement.setString(3, projectSubnode.getTitle());
			preparedStatement.setString(4, projectSubnode.getTitle());
			preparedStatement.setInt(5, projectSubnode.getPosition());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectSubnode.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}
		return projectSubnode;
	}
	
	public ProjectSubnode updateProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException {
		final String sql = "UPDATE KNOWLEDGEPROJECTSUBNODE SET LAST_MODIFICATION_TIME = ?, KPNID = ?, TITLE = ?, POSITION = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, projectSubnode.getRefProjectNodeId());
			preparedStatement.setString(3, projectSubnode.getTitle());
			preparedStatement.setInt(4, projectSubnode.getPosition());
			preparedStatement.setInt(5, projectSubnode.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectSubnode;
	}
	
	public boolean deleteProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectSubnodeId);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception ex) {
			throw new UnknownProjectSubnodeException();
		}
	}
}
