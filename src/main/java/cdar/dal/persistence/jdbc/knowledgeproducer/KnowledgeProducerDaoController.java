package cdar.dal.persistence.jdbc.knowledgeproducer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class KnowledgeProducerDaoController extends CdarJdbcHelper {

	public List<KnowledgeTreeDao> getTrees() {
		return getTrees(0);
	}
	
	public List<KnowledgeTreeDao> getTrees(int uid) {
		String getUsers = null;
		if (uid==0) {
			getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE";
		} else {
			getUsers = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE LEFT JOIN knowledgetreemapping ON knowledgetreemapping.ktrid = knowledgetree.id where knowledgetreemapping.uid = %d;", uid);
		}
		

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<KnowledgeTreeDao> trees = new ArrayList<KnowledgeTreeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUsers);
			while (result.next()) {
				KnowledgeTreeDao tree = new KnowledgeTreeDao();
				tree.setId(result.getInt(1));
				tree.setCreationTime(result.getDate(2));
				tree.setLastModificationTime(result.getDate(3));
				tree.setName(result.getString(4));
				trees.add(tree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return trees;
	}
	
	public KnowledgeTreeDao getTreeById(int id) {
		final String getTreeByIdStatement = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGETREE WHERE ID = "
				+ id;

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		KnowledgeTreeDao tree = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTreeByIdStatement);
			if (result.next()) {
				tree = new KnowledgeTreeDao();
				tree.setId(result.getInt(1));
				tree.setCreationTime(result.getDate(2));
				tree.setLastModificationTime(result.getDate(3));
				tree.setName(result.getString(4));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return tree;
	}
	
	public List<KnowledgeTemplateDao> getTemplates(int treeid) {
		String getTemplates = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE FROM KNOWLEDGETEMPLATE WHERE KTRID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<KnowledgeTemplateDao> templates = new ArrayList<KnowledgeTemplateDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTemplates);
			while (result.next()) {
				KnowledgeTemplateDao template = new KnowledgeTemplateDao();
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
	
	public KnowledgeTemplateDao getTemplate(int id) {
		String getTemplate = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE FROM KNOWLEDGETEMPLATE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		KnowledgeTemplateDao template = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTemplate);
			while (result.next()) {
				template = new KnowledgeTemplateDao();
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
	
	public List<KnowledgeNodeDao> getNodes(int treeid) {
		String getNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, DYNAMICTREEFLAG FROM KNOWLEDGENODE WHERE KTRID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<KnowledgeNodeDao> nodes = new ArrayList<KnowledgeNodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				KnowledgeNodeDao node = new KnowledgeNodeDao();
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
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return nodes;
	}
	
	public KnowledgeNodeDao getNode(int id) {
		String getNode = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, DYNAMICTREEFLAG FROM KNOWLEDGENODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		KnowledgeNodeDao node = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNode);
			while (result.next()) {
				node = new KnowledgeNodeDao();
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
}
