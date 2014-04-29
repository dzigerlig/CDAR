package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.consumer.ProjectNodeLink;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.persistence.DBConnection;

public class ProjectNodeLinkRepository {
	public List<ProjectNodeLink> getProjectNodeLinks(int projectTreeId) throws UnknownProjectTreeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE KPTID = ?";

		List<ProjectNodeLink> projectNodeLinks = new ArrayList<ProjectNodeLink>();
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNodeLink projectNodeLink = new ProjectNodeLink();
					projectNodeLink.setId(result.getInt(1));
					projectNodeLink.setCreationTime(result.getDate(2));
					projectNodeLink.setLastModificationTime(result.getDate(3));
					projectNodeLink.setSourceId(result.getInt(4));
					projectNodeLink.setTargetId(result.getInt(5));
					projectNodeLink.setRefProjectSubNodeId(result.getInt(6));
					projectNodeLink.setRefProjectTreeId(projectTreeId);
					projectNodeLinks.add(projectNodeLink);
				}
			}
		} catch (SQLException ex) {
			throw new UnknownProjectTreeException();
		}
		return projectNodeLinks;
	}
	
	public ProjectNodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectNodeLinkId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNodeLink projectNodeLink = new ProjectNodeLink();
					projectNodeLink.setId(result.getInt(1));
					projectNodeLink.setCreationTime(result.getDate(2));
					projectNodeLink.setLastModificationTime(result.getDate(3));
					projectNodeLink.setSourceId(result.getInt(4));
					projectNodeLink.setTargetId(result.getInt(5));
					projectNodeLink.setRefProjectTreeId(result.getInt(6));
					projectNodeLink.setRefProjectSubNodeId(result.getInt(7));
					return projectNodeLink;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownProjectNodeLinkException();
		}
		throw new UnknownProjectNodeLinkException();
	}
	
	public ProjectNodeLink createProjectNodeLink(ProjectNodeLink projectNodeLink) throws UnknownProjectTreeException {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTNODELINK (CREATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, projectNodeLink.getSourceId());
			preparedStatement.setInt(3, projectNodeLink.getTargetId());
			if (projectNodeLink.getRefProjectSubNodeId() != 0) {
				preparedStatement.setInt(4, projectNodeLink.getRefProjectSubNodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, projectNodeLink.getRefProjectTreeId());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectNodeLink.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
		return projectNodeLink;
	}
	
	public ProjectNodeLink updateProjectNodeLink(ProjectNodeLink projectNodeLink) throws UnknownProjectNodeLinkException {
		final String sql = "UPDATE KNOWLEDGEPROJECTNODELINK SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KPNSNID = ?, KPTID = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, projectNodeLink.getSourceId());
			preparedStatement.setInt(3, projectNodeLink.getTargetId());
			if (projectNodeLink.getRefProjectSubNodeId() != 0) {
				preparedStatement.setInt(4, projectNodeLink.getRefProjectSubNodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, projectNodeLink.getRefProjectTreeId());
			preparedStatement.setInt(6, projectNodeLink.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectNodeLink;
	}
	
	public boolean deleteProjectNodeLink(int projectNodeLinkId) throws Exception {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTNODELINK WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectNodeLinkId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownProjectNodeLinkException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
