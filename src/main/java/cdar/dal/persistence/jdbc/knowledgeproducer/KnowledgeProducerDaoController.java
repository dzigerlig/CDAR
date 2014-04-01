package cdar.dal.persistence.jdbc.knowledgeproducer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
}
