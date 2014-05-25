package cdar.dal.producer;

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
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;
import cdar.dal.interfaces.INodeLinkRepository;

public class NodeLinkRepository implements INodeLinkRepository {
	public List<NodeLink> getNodeLinks(int treeId) throws EntityException, UnknownTreeException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID FROM %s WHERE KTRID = ?", DBTableHelper.NODELINK);

		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setTreeId(treeId);
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(DateHelper.getDate(result.getString(2)));
					nodelink.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setSubnodeId(result.getInt(6));
					nodelinks.add(nodelink);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownTreeException();
		} 
		return nodelinks;
	}

	public List<NodeLink> getParentNodeLinks(int nodeId) throws EntityException, UnknownNodeLinkException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM %s WHERE ? = TARGETID",DBTableHelper.NODELINK);
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

	public List<NodeLink> getSiblingNodeLinks(int nodeId) throws UnknownTreeException, EntityException {
		final String sql = String.format("SELECT LINK.ID, LINK.CREATION_TIME, LINK.LAST_MODIFICATION_TIME, LINK.SOURCEID, LINK.TARGETID, LINK.KSNID, LINK.KTRID FROM %s AS LINK WHERE LINK.SOURCEID IN (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE  ?=LINKTO.TARGETID) AND LINK.TARGETID <> ?", DBTableHelper.NODELINK,DBTableHelper.NODELINK);
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);
			preparedStatement.setInt(2, nodeId);

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
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM %s WHERE ? = SOURCEID",DBTableHelper.NODELINK);
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
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM %s WHERE KSNID = ?",DBTableHelper.NODELINK);

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

	public NodeLink getNodeLink(int nodeLinkId) throws UnknownNodeLinkException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KTRID, KSNID FROM %s WHERE ID = ?",DBTableHelper.NODELINK);

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
	
	public NodeLink createNodeLink(NodeLink nodeLink) throws UnknownTreeException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, SOURCEID, TARGETID, KSNID, KTRID) VALUES (?, ?, ?, ?, ?)",DBTableHelper.NODELINK);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, nodeLink.getSourceId());
			preparedStatement.setInt(3, nodeLink.getTargetId());
			if (nodeLink.getSubnodeId() != 0) {
				preparedStatement.setInt(4, nodeLink.getSubnodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, nodeLink.getTreeId());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					nodeLink.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownTreeException();
		}
		return nodeLink;
	}
	
	public NodeLink updateNodeLink(NodeLink nodeLink) throws UnknownNodeLinkException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KSNID = ?, KTRID = ? WHERE id = ?",DBTableHelper.NODELINK);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, nodeLink.getSourceId());
			preparedStatement.setInt(3, nodeLink.getTargetId());
			if (nodeLink.getSubnodeId() != 0) {
				preparedStatement.setInt(4, nodeLink.getSubnodeId());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, nodeLink.getTreeId());
			preparedStatement.setInt(6, nodeLink.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownNodeLinkException();
		}
		return nodeLink;
	}
	
	public void deleteNodeLink(int nodeLinkId) throws UnknownNodeLinkException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?", DBTableHelper.NODELINK);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, nodeLinkId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownNodeLinkException();
			}
		} catch (Exception ex) {
			throw new UnknownNodeLinkException();
		}
	}
}
