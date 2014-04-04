package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ProducerDaoController extends CdarJdbcHelper {

	public List<TreeDao> getTrees() {
		return getTrees(0);
	}
	
	public List<TreeDao> getTrees(int uid) {
		String getUsers = null;
		if (uid==0) {
			getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE";
		} else {
			getUsers = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE LEFT JOIN knowledgetreemapping ON knowledgetreemapping.ktrid = knowledgetree.id where knowledgetreemapping.uid = %d;", uid);
		}
		

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<TreeDao> trees = new ArrayList<TreeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUsers);
			while (result.next()) {
				TreeDao tree = new TreeDao(uid);
				tree.setId(result.getInt(1));
				tree.setCreationTime(result.getDate(2));
				tree.setLastModificationTime(result.getDate(3));
				tree.setName(result.getString(4));
				tree.setUid(uid);
				trees.add(tree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return trees;
	}
	
	public TreeDao getTreeById(int id) {
		final String getTreeByIdStatement = String.format("SELECT UID,ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE JOIN KNOWLEDGETREEMAPPING ON KNOWLEDGETREEMAPPING.ktrid = KNOWLEDGETREE.id WHERE ID = %d;" , id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		TreeDao tree = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTreeByIdStatement);
			if (result.next()) {
				tree = new TreeDao(result.getInt(1));
				tree.setId(result.getInt(2));
				tree.setCreationTime(result.getDate(3));
				tree.setLastModificationTime(result.getDate(4));
				tree.setName(result.getString(5));
			}
		} catch (SQLException e) {
			System.out.println("error getTreeById");
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return tree;
	}
	
	public List<TemplateDao> getTemplates(int treeid) {
		String getTemplates = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE FROM KNOWLEDGETEMPLATE WHERE KTRID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<TemplateDao> templates = new ArrayList<TemplateDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTemplates);
			while (result.next()) {
				TemplateDao template = new TemplateDao(treeid);
				template.setId(result.getInt(1));
				template.setCreationTime(result.getDate(2));
				template.setLastModificationTime(result.getDate(3));
				template.setTitle(result.getString(4));
				template.setWikititle(result.getString(5));
				templates.add(template);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return templates;
	}
	
	public TemplateDao getTemplate(int id) {
		String getTemplate = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, KTRID FROM KNOWLEDGETEMPLATE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		TemplateDao template = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTemplate);
			while (result.next()) {
				template = new TemplateDao(result.getInt(6));
				template.setId(result.getInt(1));
				template.setCreationTime(result.getDate(2));
				template.setLastModificationTime(result.getDate(3));
				template.setTitle(result.getString(4));
				template.setWikititle(result.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return template;
	}
	
	public List<NodeDao> getNodes(int treeid) {
		String getNodes = String.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.KTRID = %d AND NODE.ID = MAPPING.KNID;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeDao> nodes = new ArrayList<NodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				NodeDao node = new NodeDao(treeid, result.getInt(7));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
				node.setDid(result.getInt(7));
				nodes.add(node);
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}
	
	public NodeDao getNode(int id) {
		String getNode = String.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.ID = %d AND NODE.ID = MAPPING.KNID;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		NodeDao node = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNode);
			while (result.next()) {
				node = new NodeDao(result.getInt(7),result.getInt(6));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return node;
	}
	
	public List<SubNodeDao> getSubNodes(int nodeid) {
		String getSubNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE FROM KNOWLEDGESUBNODE WHERE KNID = %d;", nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubNodeDao> subnodes = new ArrayList<SubNodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubNodes);
			while (result.next()) {
				SubNodeDao subnode = new SubNodeDao(nodeid);
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(4));
				subnode.setWikititle(result.getString(5));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}
	
	public SubNodeDao getSubNode(int id) {
		String getSubNode = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KNID, TITLE, WIKITITLE FROM KNOWLEDGESUBNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		SubNodeDao subnode = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubNode);
			while (result.next()) {
				subnode = new SubNodeDao(result.getInt(4));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnode;
	}
	
	public List<NodeLinkDao> getNodeLinks(int treeid) {
		String getNodeLinks = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID FROM NODELINK WHERE KTRID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4), result.getInt(5), treeid);
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		} 
		return nodelinks;
	}
	
	public NodeLinkDao getNodeLink(int id) {
		String getNodeLink = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KTRID, KSNID FROM KNOWLEDGESUBNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		NodeLinkDao nodelink = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLink);
			while (result.next()) {
				nodelink = new NodeLinkDao(result.getInt(4), result.getInt(5), result.getInt(6));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelink;
	}
	
	public List<DirectoryDao> getDirectories(int treeid) {
		String getDirectories = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, TITLE FROM DIRECTORY WHERE KTRID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<DirectoryDao> directories = new ArrayList<DirectoryDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getDirectories);
			while (result.next()) {
				DirectoryDao directory = new DirectoryDao(treeid);
				directory.setId(result.getInt(1));
				directory.setCreationTime(result.getDate(2));
				directory.setLastModificationTime(result.getDate(3));
				directory.setParentid(result.getInt(4));
				directory.setTitle(result.getString(5));
				directories.add(directory);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return directories;
	}
	
	public DirectoryDao getDirectory(int id) {
		String getDirectory = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, KTRID, TITLE FROM DIRECTORY WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		DirectoryDao directory = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getDirectory);
			while (result.next()) {
				directory = new DirectoryDao(result.getInt(5));
				directory.setId(result.getInt(1));
				directory.setCreationTime(result.getDate(2));
				directory.setLastModificationTime(result.getDate(3));
				directory.setParentid(result.getInt(4));
				directory.setTitle(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return directory;
	}
}
