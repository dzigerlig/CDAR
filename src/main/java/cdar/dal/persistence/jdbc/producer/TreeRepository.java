package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.producer.Tree;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.persistence.DBConnection;

public class TreeRepository {

	public List<Tree> getTrees(int uid) throws SQLException {
		String sql = null;
		if (uid == 0) {
			sql = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGETREE";
		} else {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM KNOWLEDGETREE LEFT JOIN knowledgetreemapping ON knowledgetreemapping.ktrid = knowledgetree.id where knowledgetreemapping.uid = %d;",
							uid);
		}

		List<Tree> trees = new ArrayList<Tree>();

		try (Connection connection = DBConnection.getConnection(); Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(sql)) {
				while (result.next()) {
					Tree tree = new Tree();
					tree.setId(result.getInt(1));
					tree.setCreationTime(result.getDate(2));
					tree.setLastModificationTime(result.getDate(3));
					tree.setTitle(result.getString(4));
					tree.setUid(uid);
					trees.add(tree);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return trees;
	}

	public Tree getTree(int treeId) throws Exception {
		final String sql = "SELECT MAPPING.UID,TREE.ID,TREE.CREATION_TIME,TREE.LAST_MODIFICATION_TIME,TREE.TITLE FROM KNOWLEDGETREE AS TREE JOIN KNOWLEDGETREEMAPPING AS MAPPING ON MAPPING.KTRID = TREE.ID WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Tree tree = new Tree();
					tree.setUid(result.getInt(1));
					tree.setId(result.getInt(2));
					tree.setCreationTime(result.getDate(3));
					tree.setLastModificationTime(result.getDate(4));
					tree.setTitle(result.getString(5));
					return tree;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownTreeException();
		}
		throw new UnknownTreeException();
	}
	
	public Tree createTree(Tree tree) throws Exception {
		final String sql = "INSERT INTO KNOWLEDGETREE (CREATION_TIME, TITLE) VALUES (?, ?)";
		final String sql2 = "INSERT INTO KNOWLEDGETREEMAPPING (uid, ktrid) VALUES (?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, tree.getTitle());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					tree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
			preparedStatement.setInt(1, tree.getUid());
			preparedStatement.setInt(2, tree.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return tree;
	}
	
	public Tree updateTree(Tree tree) throws Exception {
		final String sql = "UPDATE KNOWLEDGETREE SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setString(2, tree.getTitle());
			preparedStatement.setInt(3, tree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return tree;
	}
	
	public boolean deleteTree(int treeId) throws Exception {
		final String sql = "DELETE FROM KNOWLEDGETREE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, treeId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownTreeException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
