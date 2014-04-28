package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ProducerDaoRepository extends CdarJdbcHelper {

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
}
