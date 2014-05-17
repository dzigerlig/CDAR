package cdar.dal.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Node;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;

public class NodeRepository {
	public List<Node> getNodes(int treeId) throws EntityException,
			UnknownTreeException {
		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, MAPPING.DID FROM %s AS NODE, %s AS MAPPING WHERE NODE.KTRID = ? AND NODE.ID = MAPPING.KNID",
						DBTableHelper.NODE, DBTableHelper.NODEMAPPING);

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setTreeId(treeId);
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setDirectoryId(result.getInt(7));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownTreeException();
		}
		return nodes;
	}

	public Node getNode(int nodeId) throws UnknownNodeException,
			EntityException {
		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM %s AS NODE, %s AS MAPPING WHERE NODE.ID = ? AND NODE.ID = MAPPING.KNID",
						DBTableHelper.NODE, DBTableHelper.NODEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					return node;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}
		throw new UnknownNodeException();
	}

	public List<Node> getSiblingNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT DISTINCT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM (SELECT * FROM %s AS LINK WHERE (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>?",
						DBTableHelper.NODELINK, DBTableHelper.NODELINK,
						DBTableHelper.NODE, DBTableHelper.NODEMAPPING);

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);
			preparedStatement.setInt(2, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return nodes;
	}

	public List<Node> getParentNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.SOURCEID=NODE.ID AND NODE.ID=MAPPING.KNID",
						DBTableHelper.NODELINK, DBTableHelper.NODE,
						DBTableHelper.NODEMAPPING);

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {

			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return nodes;
	}

	public List<Node> getFollowerNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM (SELECT LINKTO.TARGETID FROM %s AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID",
						DBTableHelper.NODELINK, DBTableHelper.NODE,
						DBTableHelper.NODEMAPPING);

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return nodes;
	}

	public Node createNode(Node node) throws UnknownTreeException,
			CreationException {
		final String sqlNode = String
				.format("INSERT INTO %s (CREATION_TIME, TITLE, KTRID, DYNAMICTREEFLAG) VALUES (?, ?, ?, ?)",
						DBTableHelper.NODE);
		final String sqlWiki = String.format(
				"UPDATE %s SET WIKITITLE = ? where id = ?", DBTableHelper.NODE);
		final String sqlMapping = String.format(
				"INSERT INTO %s (knid, did) VALUES (?, ?)",
				DBTableHelper.NODEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlNode,
								Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, node.getTitle());
			preparedStatement.setInt(3, node.getTreeId());
			preparedStatement.setInt(4, node.getDynamicTreeFlag());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					node.setId(generatedKeys.getInt(1));
					node.setWikititle(String.format("NODE_%d", node.getId()));
				}
			}
		} catch (Exception ex) {
			throw new UnknownTreeException();
		}

		// sqlWiki
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlWiki)) {
			preparedStatement.setString(1, node.getWikititle());
			preparedStatement.setInt(2, node.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new CreationException();
		}

		// nodeMapping
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, node.getId());
			preparedStatement.setInt(2, node.getDirectoryId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new CreationException();
		}

		return node;
	}

	public Node updateNode(Node node) throws UnknownNodeException {
		final String sqlNode = String
				.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG = ?, WIKITITLE = ?  WHERE id = ?",
						DBTableHelper.NODE);
		final String sqlMapping = String
				.format("INSERT INTO %s (DID, KNID) VALUES (?, ?) ON DUPLICATE KEY UPDATE DID = ?",
						DBTableHelper.NODEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlNode)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, node.getTitle());
			preparedStatement.setInt(3, node.getDynamicTreeFlag());
			preparedStatement.setString(4, node.getWikititle());
			preparedStatement.setInt(5, node.getId());

			System.out.println("wikititle: " + node.getWikititle());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownNodeException();
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, node.getDirectoryId());
			preparedStatement.setInt(2, node.getId());
			preparedStatement.setInt(3, node.getDirectoryId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownNodeException();
		}

		return node;
	}

	public void deleteNode(int nodeId) throws UnknownNodeException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",
				DBTableHelper.NODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, nodeId);
			if (preparedStatement.executeUpdate() != 1) {
				throw new UnknownNodeException();
			}
		} catch (Exception ex) {
			throw new UnknownNodeException();
		}
	}

	public Node getRoot(int ktreeid) throws EntityException,
			UnknownNodeException {

		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM %s AS NODE, %s AS MAPPING WHERE NODE.ID NOT IN (SELECT TARGETID FROM %s) AND NODE.DYNAMICTREEFLAG = 1 AND NODE.ID = MAPPING.KNID AND NODE.KTRID=? LIMIT 1",
						DBTableHelper.NODE, DBTableHelper.NODEMAPPING,
						DBTableHelper.NODELINK);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, ktreeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					return node;
				}
				return getMinNode(ktreeid);
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}
	}

	public Node getMinNode(int ktreeid) throws EntityException,
			UnknownNodeException {

		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM %S AS NODE, %s AS MAPPING WHERE NODE.ID = (SELECT MIN(ID) FROM %s AS INNERNODE WHERE INNERNODE.DYNAMICTREEFLAG = 1 AND INNERNODE.KTRID = ?) AND NODE.ID = MAPPING.KNID",
						DBTableHelper.NODE, DBTableHelper.NODEMAPPING,
						DBTableHelper.NODE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, ktreeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				if (result.wasNull()) {
				}
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setDirectoryId(result.getInt(8));
					return node;
				}
				return null;
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}
	}
}
