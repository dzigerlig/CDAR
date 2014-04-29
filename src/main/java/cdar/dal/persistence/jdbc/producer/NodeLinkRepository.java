package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.producer.NodeLink;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.exceptions.UnknownNodeLinkException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.persistence.DBConnection;

public class NodeLinkRepository {
	public List<NodeLink> getNodeLinks(int treeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID FROM NODELINK WHERE KTRID = ?";

		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setKtrid(treeId);
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKsnid(result.getInt(6));
					nodelinks.add(nodelink);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		} 
		return nodelinks;
	}

	public List<NodeLink> getParentNodeLinks(int nodeid) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM NODELINK WHERE ? = TARGETID";
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKsnid(result.getInt(6));
					nodelink.setKtrid(result.getInt(7));
					nodelinks.add(nodelink);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return nodelinks;
	}

	public List<NodeLink> getSiblingNodeLinks(int nodeid) throws SQLException {
		final String sql = "SELECT LINK.ID, LINK.CREATION_TIME, LINK.LAST_MODIFICATION_TIME, LINK.SOURCEID, LINK.TARGETID, LINK.KSNID, LINK.KTRID FROM NODELINK AS LINK WHERE (SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO WHERE  ?=LINKTO.TARGETID)=LINK.SOURCEID AND LINK.TARGETID <> ?";
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
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKsnid(result.getInt(6));
					nodelink.setKtrid(result.getInt(7));
					nodelinks.add(nodelink);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return nodelinks;
	}

	public List<NodeLink> getFollowerNodeLinks(int nodeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM NODELINK WHERE ? = SOURCEID";
		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKsnid(result.getInt(6));
					nodelink.setKtrid(result.getInt(7));
					nodelinks.add(nodelink);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return nodelinks;
	}

	public List<NodeLink> getNodeLinksBySubnode(int subnodeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM NODELINK WHERE KSNID = ?";

		List<NodeLink> nodelinks = new ArrayList<NodeLink>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, subnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKsnid(result.getInt(6));
					nodelink.setKtrid(result.getInt(7));
					nodelinks.add(nodelink);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return nodelinks;
	}

	public NodeLink getNodeLink(int id) throws UnknownNodeLinkException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KTRID, KSNID FROM NODELINK WHERE ID = ?";


		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					NodeLink nodelink = new NodeLink();
					nodelink.setId(result.getInt(1));
					nodelink.setCreationTime(result.getDate(2));
					nodelink.setLastModificationTime(result.getDate(3));
					nodelink.setSourceId(result.getInt(4));
					nodelink.setTargetId(result.getInt(5));
					nodelink.setKtrid(result.getInt(6));
					nodelink.setKsnid(result.getInt(7));
					return nodelink;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownNodeLinkException();
		}
		throw new UnknownNodeLinkException();
	}
	
	public NodeLink createNodeLink(NodeLink nodeLink) throws UnknownTreeException {
		final String sql = "INSERT INTO NODELINK (CREATION_TIME, SOURCEID, TARGETID, KSNID, KTRID) VALUES (?, ?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, nodeLink.getSourceId());
			preparedStatement.setInt(3, nodeLink.getTargetId());
			if (nodeLink.getKsnid() != 0) {
				preparedStatement.setInt(4, nodeLink.getKsnid());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, nodeLink.getKtrid());

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
	
	public NodeLink updateNodeLink(NodeLink nodeLink) throws Exception {
		final String sql = "UPDATE NODELINK SET LAST_MODIFICATION_TIME = ?, SOURCEID = ?, TARGETID = ?, KSNID = ?, KTRID = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, nodeLink.getSourceId());
			preparedStatement.setInt(3, nodeLink.getTargetId());
			if (nodeLink.getKsnid() != 0) {
				preparedStatement.setInt(4, nodeLink.getKsnid());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			preparedStatement.setInt(5, nodeLink.getKtrid());
			preparedStatement.setInt(6, nodeLink.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return nodeLink;
	}
	
	public boolean deleteNodeLink(int nodeLinkId) throws Exception {
		final String sql = "DELETE FROM NODELINK WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, nodeLinkId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownNodeLinkException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
