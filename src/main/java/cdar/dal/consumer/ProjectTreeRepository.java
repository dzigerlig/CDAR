package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Tree;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;
import cdar.dal.interfaces.ITreeRepository;

public class ProjectTreeRepository implements ITreeRepository<Tree> {
	public List<Tree> getTrees(int uid) throws UnknownUserException, EntityException {
		String sql = null;
		if (uid==0) {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM %s",DBTableHelper.PROJECTTREE);
		} else {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM %s AS TREE LEFT JOIN %s AS MAPPING ON MAPPING.kptid = TREE.id where MAPPING.uid = %d;",DBTableHelper.PROJECTTREE,DBTableHelper.PROJECTTREEMAPPING, uid);
		}
		

		List<Tree> projectTrees = new ArrayList<Tree>();

		try (Connection connection = DBConnection.getConnection(); Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(sql)) {
				while (result.next()) {
					Tree projectTree = new Tree();
					projectTree.setId(result.getInt(1));
					projectTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectTree.setTitle(result.getString(4));
					projectTree.setUserId(uid);
					projectTrees.add(projectTree);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownUserException();
		}
		return projectTrees;
	}
	
	public Tree getTree(int projectTreeId) throws UnknownProjectTreeException, EntityException {
		final String sql = String.format("SELECT UID,ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM %s AS TREE JOIN %s AS MAPPING ON MAPPING.kptid = TREE.id WHERE ID = ?",DBTableHelper.PROJECTTREE,DBTableHelper.PROJECTTREEMAPPING);

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				if (result.next()) {
					Tree projectTree = new Tree();
					projectTree.setUserId(result.getInt(1));
					projectTree.setId(result.getInt(2));
					projectTree.setCreationTime(DateHelper.getDate(result.getString(3)));
					projectTree.setLastModificationTime(DateHelper.getDate(result.getString(4)));
					projectTree.setTitle(result.getString(5));
					return projectTree;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownProjectTreeException();
		}
		throw new UnknownProjectTreeException();
	}
	
	public Tree createTree(Tree projectTree) throws CreationException, EntityException  {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, TITLE) VALUES (?, ?)",DBTableHelper.PROJECTTREE);
		final String sql2 = String.format("INSERT INTO %s (uid, kptid) VALUES (?, ?)",DBTableHelper.PROJECTTREEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectTree.getTitle());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectTree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new CreationException();
		}
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
			preparedStatement.setInt(1, projectTree.getUserId());
			preparedStatement.setInt(2, projectTree.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new EntityException();
		}
		return projectTree;
	}
	
	public Tree updateTree(Tree projectTree) throws UnknownProjectTreeException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",DBTableHelper.PROJECTTREE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectTree.getTitle());
			preparedStatement.setInt(3, projectTree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
		return projectTree;
	}
	
	public void deleteTree(int projectTreeId) throws UnknownProjectTreeException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.PROJECTTREE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectTreeId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownProjectTreeException();
			}
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}
	}
}
