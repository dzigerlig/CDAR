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
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;

public class NodeLinkRepository {
	public List<NodeLink> getProjectNodeLinks(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID FROM %s WHERE KPTID = ?",DBTableHelper.PROJECTNODELINK);

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
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KPNSNID FROM %s WHERE ID = ?",DBTableHelper.PROJECTNODELINK);

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
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID) VALUES (?, ?, ?, ?, ?)",DBTableHelper.PROJECTNODELINK);
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
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KPNSNID = ?, KPTID = ? WHERE id = ?",DBTableHelper.PROJECTNODELINK);
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
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.PROJECTNODELINK);
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

	public List<NodeLink> getParentNodeLinks(int nodeId) throws EntityException, UnknownNodeLinkException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID FROM %s WHERE ? = TARGETID",DBTableHelper.PROJECTNODELINK);
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setSubnodeId(result.getInt(6));
					nodelink.setTreeId(result.getInt(7));
					nodelinks.add(nodelink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

			throw new UnknownNodeLinkException();
		}
		return nodelinks;
	}
	
		public List<NodeLink> getSiblingNodeLinks(int nodeid) throws UnknownTreeException, EntityException {
		final String sql = String.format("SELECT LINK.ID, LINK.CREATION_TIME, LINK.LAST_MODIFICATION_TIME, LINK.SOURCEID, LINK.TARGETID, LINK.KPNSNID, LINK.KPTID FROM %s AS LINK WHERE (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE  ?=LINKTO.TARGETID)=LINK.SOURCEID AND LINK.TARGETID <> ?",DBTableHelper.PROJECTNODELINK,DBTableHelper.PROJECTNODELINK);
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeid);
			preparedStatement.setInt(2, nodeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setSubnodeId(result.getInt(6));
					nodelink.setTreeId(result.getInt(7));
					nodelinks.add(nodelink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

			throw new UnknownTreeException();
		}
		return nodelinks;
	}

	public List<NodeLink> getFollowerNodeLinks(int nodeId) throws UnknownNodeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID FROM %s WHERE ? = SOURCEID", DBTableHelper.PROJECTNODELINK);
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setSubnodeId(result.getInt(6));
					nodelink.setTreeId(result.getInt(7));
					nodelinks.add(nodelink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());

			throw new UnknownNodeException();
		}
		return nodelinks;
	}

	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws EntityException, UnknownSubnodeException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID, KPTID FROM %s WHERE KPNSNID = ?",DBTableHelper.PROJECTNODELINK);

		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, subnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setSubnodeId(result.getInt(6));
					nodelink.setTreeId(result.getInt(7));
					nodelinks.add(nodelink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownSubnodeException();
		}
		return nodelinks;
	}

	public NodeLink getNodeLink(int nodeLinkId) throws EntityException, UnknownNodeLinkException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KPNSNID FROM %s WHERE ID = ?",DBTableHelper.PROJECTNODELINK);


		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeLinkId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setTreeId(result.getInt(6));
					nodelink.setSubnodeId(result.getInt(7));
					return nodelink;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeLinkException();
		}
		throw new UnknownNodeLinkException();
	}

	public NodeLink updateNodeLink(NodeLink updatedNodeLink) throws UnknownNodeLinkException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KPNSNID = ?, KPTID = ? WHERE id = ?",DBTableHelper.PROJECTNODELINK);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, updatedNodeLink.getSourceId());
			preparedStatement.setInt(3, updatedNodeLink.getTargetId());
			if (updatedNodeLink.getSubnodeId() != 0) {
				preparedStatement.setInt(4, updatedNodeLink.getSubnodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, updatedNodeLink.getTreeId());
			preparedStatement.setInt(6, updatedNodeLink.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownNodeLinkException();
		}
		return updatedNodeLink;
	}
}
