package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.NodeLink;
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;

public class ProjectNodeLinkRepository {
	public List<NodeLink> getProjectNodeLinks(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE KPTID = ?";

		List<NodeLink> projectNodeLinks = new ArrayList<NodeLink>();
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink projectNodeLink = new NodeLink();
					projectNodeLink.setId(result.getInt(1));
					projectNodeLink.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectNodeLink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectNodeLink.setSourceId(result.getInt(4));
					projectNodeLink.setTargetId(result.getInt(5));
					projectNodeLink.setSubnodeId(result.getInt(6));
					projectNodeLink.setTreeId(projectTreeId);
					projectNodeLinks.add(projectNodeLink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownProjectTreeException();
		}
		return projectNodeLinks;
	}
	
	public NodeLink getProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectNodeLinkId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink projectNodeLink = new NodeLink();
					projectNodeLink.setId(result.getInt(1));
					projectNodeLink.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectNodeLink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectNodeLink.setSourceId(result.getInt(4));
					projectNodeLink.setTargetId(result.getInt(5));
					projectNodeLink.setTreeId(result.getInt(6));
					projectNodeLink.setSubnodeId(result.getInt(7));
					return projectNodeLink;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownProjectNodeLinkException();
		}
		throw new UnknownProjectNodeLinkException();
	}
	
	public NodeLink createProjectNodeLink(NodeLink projectNodeLink) throws UnknownProjectTreeException {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTNODELINK (CREATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, projectNodeLink.getSourceId());
			preparedStatement.setInt(3, projectNodeLink.getTargetId());
			if (projectNodeLink.getSubnodeId() != 0) {
				preparedStatement.setInt(4, projectNodeLink.getSubnodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, projectNodeLink.getTreeId());

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
	
	public NodeLink updateProjectNodeLink(NodeLink projectNodeLink) throws UnknownProjectNodeLinkException {
		final String sql = "UPDATE KNOWLEDGEPROJECTNODELINK SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KPNSNID = ?, KPTID = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, projectNodeLink.getSourceId());
			preparedStatement.setInt(3, projectNodeLink.getTargetId());
			if (projectNodeLink.getSubnodeId() != 0) {
				preparedStatement.setInt(4, projectNodeLink.getSubnodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, projectNodeLink.getTreeId());
			preparedStatement.setInt(6, projectNodeLink.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectNodeLink;
	}
	
	public void deleteProjectNodeLink(int projectNodeLinkId) throws UnknownProjectNodeLinkException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTNODELINK WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectNodeLinkId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownProjectNodeLinkException();
			}
		} catch (Exception ex) {
			throw new UnknownProjectNodeLinkException();
		}
	}
}
