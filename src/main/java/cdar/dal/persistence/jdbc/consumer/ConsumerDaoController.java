package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ConsumerDaoController extends CdarJdbcHelper {
	public List<ProjectTreeDao> getProjectTrees() {
		return getProjectTrees(0);
	}
	
	public List<ProjectTreeDao> getProjectTrees(int uid) {
		String getUsers = null;
		if (uid==0) {
			getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE";
		} else {
			getUsers = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE LEFT JOIN knowledgeprojecttreemapping ON knowledgeprojecttreemapping.kptid = knowledgeprojecttree.id where knowledgeprojecttreemapping.uid = %d;", uid);
		}
		

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectTreeDao> projecttrees = new ArrayList<ProjectTreeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUsers);
			while (result.next()) {
				ProjectTreeDao projecttree = new ProjectTreeDao(uid);
				projecttree.setId(result.getInt(1));
				projecttree.setCreationTime(result.getDate(2));
				projecttree.setLastModificationTime(result.getDate(3));
				projecttree.setName(result.getString(4));
				projecttree.setUid(uid);
				projecttrees.add(projecttree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projecttrees;
	}
	
	public ProjectTreeDao getTreeById(int id) {
		final String getTreeByIdStatement = String.format("SELECT UID,ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE JOIN KNOWLEDGEPROJECTTREEMAPPING ON KNOWLEDGEPROJECTTREEMAPPING.kptid = KNOWLEDGEPROJECTTREE.id WHERE ID = %d;" , id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectTreeDao projecttree = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTreeByIdStatement);
			if (result.next()) {
				projecttree = new ProjectTreeDao(result.getInt(1));
				projecttree.setId(result.getInt(2));
				projecttree.setCreationTime(result.getDate(3));
				projecttree.setLastModificationTime(result.getDate(4));
				projecttree.setName(result.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projecttree;
	}
}
