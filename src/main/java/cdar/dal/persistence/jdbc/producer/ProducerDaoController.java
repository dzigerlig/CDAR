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
		if (uid == 0) {
			getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGETREE";
		} else {
			getUsers = String
					.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGETREE LEFT JOIN knowledgetreemapping ON knowledgetreemapping.ktrid = knowledgetree.id where knowledgetreemapping.uid = %d;",
							uid);
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
				tree.setTitle(result.getString(4));
				tree.setUid(uid);
				trees.add(tree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return trees;
	}

	public TreeDao getTree(int id) {
		final String getTreeByIdStatement = String
				.format("SELECT MAPPING.UID,TREE.ID,TREE.CREATION_TIME,TREE.LAST_MODIFICATION_TIME,TREE.TITLE FROM KNOWLEDGETREE AS TREE JOIN KNOWLEDGETREEMAPPING AS MAPPING ON MAPPING.KTRID = TREE.ID WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		TreeDao tree = new TreeDao();
		tree.setId(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTreeByIdStatement);
			while (result.next()) {
				tree = new TreeDao(result.getInt(1));
				tree.setId(result.getInt(2));
				tree.setCreationTime(result.getDate(3));
				tree.setLastModificationTime(result.getDate(4));
				tree.setTitle(result.getString(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get tree exception");
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}

		return tree;
	}

	public List<XmlTreeDao> getXmlTrees(int treeid) {
		String getXmlTrees = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE KTRID = %d;",
						treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<XmlTreeDao> xmlTrees = new ArrayList<XmlTreeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getXmlTrees);
			while (result.next()) {
				XmlTreeDao xmlTree = new XmlTreeDao(result.getInt(1));
				xmlTree.setCreationTime(result.getDate(2));
				xmlTree.setLastModificationTime(result.getDate(3));
				xmlTree.setXmlString(result.getString(4));
				xmlTree.setUid(result.getInt(5));
				xmlTree.setKtrid(result.getInt(6));
				xmlTrees.add(xmlTree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return xmlTrees;
	}

	public XmlTreeDao getXmlTree(int id) {
		String getXmlTree = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		XmlTreeDao xmlTree = new XmlTreeDao(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getXmlTree);
			while (result.next()) {
				xmlTree = new XmlTreeDao(result.getInt(1));
				xmlTree.setCreationTime(result.getDate(2));
				xmlTree.setLastModificationTime(result.getDate(3));
				xmlTree.setXmlString(result.getString(4));
				xmlTree.setUid(result.getInt(5));
				xmlTree.setKtrid(result.getInt(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return xmlTree;
	}

	public List<TemplateDao> getTemplates(int treeid) {
		String getTemplates = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, ISDEFAULT, DECISIONMADE FROM KNOWLEDGETEMPLATE WHERE KTRID = %d;",
						treeid);

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
				template.setTemplatetext(result.getString(5));
				template.setIsDefault(result.getInt(6) == 1);
				template.setDecisionMade(result.getInt(7) == 1);
				templates.add(template);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return templates;
	}

	public TemplateDao getTemplate(int id) {
		String getTemplate = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, TEMPLATETEXT, KTRID, ISDEFAULT, DECISIONMADE FROM KNOWLEDGETEMPLATE WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		TemplateDao template = new TemplateDao();
		template.setId(-1);

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
				template.setTemplatetext(result.getString(5));
				template.setIsDefault(result.getInt(7) == 1);
				template.setDecisionMade(result.getInt(8) == 1);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return template;
	}

	public List<NodeDao> getNodes(int treeid) {
		String getNodes = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.KTRID = %d AND NODE.ID = MAPPING.KNID;",
						treeid);

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
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}

	public NodeDao getNode(int id) {
		String getNode = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID FROM KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE NODE.ID = %d AND NODE.ID = MAPPING.KNID;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		NodeDao node = new NodeDao();
		node.setId(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNode);
			while (result.next()) {
				node = new NodeDao(result.getInt(7), result.getInt(8));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return node;
	}

	public List<NodeDao> getSiblingNode(int nodeid) {
		String getNodes = String
				.format("SELECT DISTINCT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID "
						+ "FROM ("
						+ "SELECT* FROM NODELINK AS LINK "
						+ "WHERE ("
						+ "SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO "
						+ "WHERE  %d=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>%d;",
						nodeid, nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeDao> nodes = new ArrayList<NodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				NodeDao node = new NodeDao(result.getInt(7), result.getInt(8));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
				nodes.add(node);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}

	public List<NodeDao> getParentNode(int nodeid) {
		String getNodes = String
				.format("SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID "
						+ "FROM ("
						+ "SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO "
						+ "WHERE %d=LINKTO.TARGETID"
						+ ") AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.SOURCEID=NODE.ID AND NODE.ID=MAPPING.KNID;",
						nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeDao> nodes = new ArrayList<NodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				NodeDao node = new NodeDao(result.getInt(7), result.getInt(8));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
				nodes.add(node);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}

	public List<NodeDao> getFollowerNode(int nodeid) {
		String getNodes = String
				.format("SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KTRID, MAPPING.DID "
						+ "FROM("
						+ "SELECT LINKTO.TARGETID FROM NODELINK AS LINKTO "
						+ "WHERE %d=LINKTO.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID;",
						nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeDao> nodes = new ArrayList<NodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				NodeDao node = new NodeDao(result.getInt(7), result.getInt(8));
				node.setId(result.getInt(1));
				node.setCreationTime(result.getDate(2));
				node.setLastModificationTime(result.getDate(3));
				node.setTitle(result.getString(4));
				node.setWikititle(result.getString(5));
				node.setDynamicTreeFlag(result.getInt(6));
				nodes.add(node);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}

	public List<SubnodeDao> getSubnodes(int nodeid) {
		String getSubnodes = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION FROM KNOWLEDGESUBNODE WHERE KNID = %d;",
						nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubnodeDao> subnodes = new ArrayList<SubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnodes);
			while (result.next()) {
				SubnodeDao subnode = new SubnodeDao(nodeid, result.getInt(6));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(4));
				subnode.setWikititle(result.getString(5));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}

	public SubnodeDao getSubnode(int id) {
		String getSubnode = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KNID, TITLE, WIKITITLE, POSITION FROM KNOWLEDGESUBNODE WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		SubnodeDao subnode = new SubnodeDao();
		subnode.setId(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnode);
			while (result.next()) {
				subnode = new SubnodeDao(result.getInt(4), result.getInt(7));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnode;
	}
	
	public List<SubnodeDao> getParentSubnode(int nodeid) {
		String getSubnode = String
				.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION "
						+ "FROM ( "
						+ "SELECT NODE.ID "
						+ "FROM( "
						+ "SELECT LINKTO.SOURCEID "
						+ "FROM NODELINK AS LINKTO "
						+ "WHERE %d = LINKTO.TARGETID) AS SUB,  KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.SOURCEID = NODE.ID AND NODE.ID = MAPPING.KNID) AS NODES, KNOWLEDGESUBNODE AS SUBN "
						+ "WHERE SUBN.KNID=NODES.ID;",
						nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubnodeDao> subnodes = new ArrayList<SubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnode);
			while (result.next()) {
				SubnodeDao subnode = new SubnodeDao(result.getInt(4),
						result.getInt(7));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}

	public List<SubnodeDao> getSiblingSubnode(int nodeid) {
		String getSubnode = String
				.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION "
						+ "FROM( "
						+ "SELECT DISTINCT  NODE.ID "
						+ "FROM ( "
						+ "SELECT* FROM NODELINK AS LINK "
						+ "WHERE ( "
						+ "SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO "
						+ "WHERE  %d=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>%d) AS NODES, KNOWLEDGESUBNODE AS SUBN "
						+ "WHERE SUBN.KNID=NODES.ID;", nodeid, nodeid);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubnodeDao> subnodes = new ArrayList<SubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnode);
			while (result.next()) {
				SubnodeDao subnode = new SubnodeDao(result.getInt(4),
						result.getInt(7));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}
	
	public List<SubnodeDao> getFollowerSubnode(int nodeid) {
		String getSubnode = String
				.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION "
						+ "FROM ( "
						+ "SELECT  NODE.ID "
						+ "FROM( "
						+ "SELECT LINKTO.TARGETID FROM NODELINK AS LINKTO "
						+ "WHERE %d=LINKTO.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING "
						+ "WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID) AS NODES, KNOWLEDGESUBNODE AS SUBN "
						+ "WHERE SUBN.KNID=NODES.ID;",
						nodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubnodeDao> subnodes = new ArrayList<SubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnode);
			while (result.next()) {
				SubnodeDao subnode = new SubnodeDao(result.getInt(4),
						result.getInt(7));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		//	e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}

	public int getNextSubnodePosition(int nodeid) {
		int position = 0;

		for (SubnodeDao subnode : getSubnodes(nodeid)) {
			if (subnode.getPosition() > position) {
				position = subnode.getPosition();
			}
		}

		return ++position;
	}

	public List<SubnodeDao> getSubnodesByTree(int treeid) {
		String getSubnodesByTree = String
				.format("SELECT SUBNODE.ID, SUBNODE.CREATION_TIME, SUBNODE.LAST_MODIFICATION_TIME, SUBNODE.KNID, SUBNODE.TITLE, SUBNODE.WIKITITLE, POSITION FROM KNOWLEDGESUBNODE AS SUBNODE JOIN KNOWLEDGENODE ON KNOWLEDGENODE.ID = SUBNODE.KNID WHERE KNOWLEDGENODE.KTRID = %d;",
						treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<SubnodeDao> subnodes = new ArrayList<SubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getSubnodesByTree);
			while (result.next()) {
				SubnodeDao subnode = new SubnodeDao(result.getInt(4),
						result.getInt(7));
				subnode.setId(result.getInt(1));
				subnode.setCreationTime(result.getDate(2));
				subnode.setLastModificationTime(result.getDate(3));
				subnode.setTitle(result.getString(5));
				subnode.setWikititle(result.getString(6));
				subnodes.add(subnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return subnodes;
	}

	public List<NodeLinkDao> getNodeLinks(int treeid) {
		String getNodeLinks = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID FROM NODELINK WHERE KTRID = %d;",
						treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4),
						result.getInt(5), treeid);
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelinks;
	}

	public List<NodeLinkDao> getParentNodeLinks(int nodeid) {
		String getNodeLinks = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID "
						+ "FROM NODELINK " + "WHERE %d = TARGETID;", nodeid);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4),
						result.getInt(5), result.getInt(7));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelinks;
	}

	public List<NodeLinkDao> getSiblingNodeLinks(int nodeid) {
		String getNodeLinks = String
				.format("SELECT LINK.ID, LINK.CREATION_TIME, LINK.LAST_MODIFICATION_TIME, LINK.SOURCEID, LINK.TARGETID, LINK.KSNID, LINK.KTRID "
						+ "FROM NODELINK AS LINK "
						+ "WHERE ( "
						+ "SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO "
						+ "WHERE  %d=LINKTO.TARGETID)=LINK.SOURCEID AND LINK.TARGETID <> %d;",
						nodeid, nodeid);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4),
						result.getInt(5), result.getInt(7));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelinks;
	}

	public List<NodeLinkDao> getFollowerNodeLinks(int nodeid) {
		String getNodeLinks = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID "
						+ "FROM    NODELINK " + "WHERE %d = SOURCEID;", nodeid);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4),
						result.getInt(5), result.getInt(7));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelinks;
	}

	public List<NodeLinkDao> getNodeLinksBySubnode(int subnodeid) {
		String getNodeLinks = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KSNID, KTRID FROM NODELINK WHERE KSNID = %d;",
						subnodeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<NodeLinkDao> nodelinks = new ArrayList<NodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLinks);
			while (result.next()) {
				NodeLinkDao nodelink = new NodeLinkDao(result.getInt(4),
						result.getInt(5), result.getInt(7));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(6));
				nodelinks.add(nodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelinks;
	}

	public NodeLinkDao getNodeLink(int id) {
		String getNodeLink = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KTRID, KSNID FROM NODELINK WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		NodeLinkDao nodelink = new NodeLinkDao();
		nodelink.setId(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodeLink);
			while (result.next()) {
				nodelink = new NodeLinkDao(result.getInt(4), result.getInt(5),
						result.getInt(6));
				nodelink.setId(result.getInt(1));
				nodelink.setCreationTime(result.getDate(2));
				nodelink.setLastModificationTime(result.getDate(3));
				nodelink.setKsnid(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodelink;
	}

	public List<DirectoryDao> getDirectories(int treeid) {
		String getDirectories = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, TITLE FROM DIRECTORY WHERE KTRID = %d;",
						treeid);

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
			//e.printStackTrace();
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return directories;
	}

	public DirectoryDao getDirectory(int id) {
		String getDirectory = String
				.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, KTRID, TITLE FROM DIRECTORY WHERE ID = %d;",
						id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		DirectoryDao directory = new DirectoryDao();
		directory.setId(-1);

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
