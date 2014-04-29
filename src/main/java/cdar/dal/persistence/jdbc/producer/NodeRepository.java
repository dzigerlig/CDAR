package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.producer.Node;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.persistence.DBConnection;

public class NodeRepository {
	public List<Node> getNodes(int treeid) throws SQLException {
		final String sql = "SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.KTRID = ? AND NODE.ID = MAPPING.KNID";

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setKtrid(treeid);
					node.setId(result.getInt(1));
					node.setCreationTime(result.getDate(2));
					node.setLastModificationTime(result.getDate(3));
					node.setTitle(result.getString(4));
					node.setWikiTitle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setDid(result.getInt(7));
					nodes.add(node);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
		return nodes;
	}

	public Node getNode(int id) throws UnknownNodeException {
		final String sql = "SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.ID = ? AND NODE.ID = MAPPING.KNID";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(result.getDate(2));
					node.setLastModificationTime(result.getDate(3));
					node.setTitle(result.getString(4));
					node.setWikiTitle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setKtrid(result.getInt(7));
					node.setDid(result.getInt(8));
					return node;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}
		throw new UnknownNodeException();
	}

	public List<Node> getSiblingNode(int nodeid) {
		final String sql = "SELECT DISTINCT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM (SELECT * FROM NODELINK AS LINK WHERE (SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO WHERE ?=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>?";

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeid);
			preparedStatement.setInt(2, nodeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(result.getDate(2));
					node.setLastModificationTime(result.getDate(3));
					node.setTitle(result.getString(4));
					node.setWikiTitle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setKtrid(result.getInt(7));
					node.setDid(result.getInt(8));
					nodes.add(node);
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			// e.printStackTrace();
		}
		return nodes;
	}

	public List<Node> getParentNode(int nodeid) {
		final String sql = "SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM (SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO WHERE ?=LINKTO.TARGETID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.SOURCEID=NODE.ID AND NODE.ID=MAPPING.KNID";

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {

			preparedStatement.setInt(1, nodeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(result.getDate(2));
					node.setLastModificationTime(result.getDate(3));
					node.setTitle(result.getString(4));
					node.setWikiTitle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setKtrid(result.getInt(7));
					node.setDid(result.getInt(8));
					nodes.add(node);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
		return nodes;
	}

	public List<Node> getFollowerNode(int nodeid) {
		final String sql = "SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM(SELECT LINKTO.TARGETID FROM NODELINK AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID";

		List<Node> nodes = new ArrayList<Node>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Node node = new Node();
					node.setId(result.getInt(1));
					node.setCreationTime(result.getDate(2));
					node.setLastModificationTime(result.getDate(3));
					node.setTitle(result.getString(4));
					node.setWikiTitle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setKtrid(result.getInt(7));
					node.setDid(result.getInt(8));
					nodes.add(node);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
		return nodes;
	}

	public Node createNode(Node node) throws Exception {
		final String sqlNode = "INSERT INTO KNOWLEDGENODE (CREATION_TIME, TITLE, KTRID, DYNAMICTREEFLAG) VALUES (?, ?, ?, ?)";
		final String sqlWiki = "UPDATE KNOWLEDGENODE SET WIKITITLE = ? where id = ?";
		final String sqlMapping = "INSERT INTO KNOWLEDGENODEMAPPING (knid, did) VALUES (?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlNode,
								Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, node.getTitle());
			preparedStatement.setInt(3, node.getKtrid());
			preparedStatement.setInt(4, node.getDynamicTreeFlag());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					node.setId(generatedKeys.getInt(1));
					node.setWikiTitle(String.format("NODE_%d", node.getId()));
				}
			}
		} catch (Exception ex) {
			throw new UnknownTreeException();
		}

		// sqlWiki
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlWiki)) {
			preparedStatement.setString(1, node.getWikiTitle());
			preparedStatement.setInt(2, node.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println("SQL WIKI EXCEPTION: ");
			ex.printStackTrace();
			throw ex;
		}

		// nodeMapping
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, node.getId());
			preparedStatement.setInt(2, node.getDid());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println("SQL MAPPING EXCEPTION: ");
			ex.printStackTrace();
			throw ex;
		}

		return node;
	}

	public Node updateNode(Node node) throws Exception {
		final String sqlNode = "UPDATE KNOWLEDGENODE SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG = ?  WHERE id = ?";
		final String sqlMapping = "INSERT INTO KNOWLEDGENODEMAPPING (DID, KNID) VALUES (?, ?) ON DUPLICATE KEY UPDATE DID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlNode)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, node.getTitle());
			preparedStatement.setInt(3, node.getDynamicTreeFlag());
			preparedStatement.setInt(4, node.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, node.getDid());
			preparedStatement.setInt(2, node.getId());
			preparedStatement.setInt(3, node.getDid());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}

		return node;
	}

	public boolean deleteNode(Node node) throws Exception {
		final String sql = "DELETE FROM KNOWLEDGENODE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, node.getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
