package ch.cdar.dal.repository.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.cdar.bll.entity.Tree;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownUserException;
import ch.cdar.dal.helpers.DBConnection;
import ch.cdar.dal.helpers.DBTableHelper;
import ch.cdar.dal.helpers.DateHelper;
import ch.cdar.dal.repository.interfaces.ITreeRepository;

/**
 * The Class TreeRepository.
 */
public class TreeRepository implements ITreeRepository {
	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.ITreeRepository#getTrees(int)
	 */
	public List<Tree> getTrees(int uid) throws UnknownUserException, EntityException {
		String sql = null;
		if (uid == 0) {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM %s",DBTableHelper.TREE);
		} else {
			sql = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,TITLE FROM %s AS TREE LEFT JOIN %s AS MAPPING ON MAPPING.ktrid = TREE.id where MAPPING.uid = %d;",
							DBTableHelper.TREE,DBTableHelper.TREEMAPPING,uid);
		}

		List<Tree> trees = new ArrayList<Tree>();

		try (Connection connection = DBConnection.getConnection(); Statement statement = connection.createStatement()) {
			try (ResultSet result = statement.executeQuery(sql)) {
				while (result.next()) {
					Tree tree = new Tree();
					tree.setId(result.getInt(1));
					tree.setCreationTime(DateHelper.getDate(result.getString(2)));
					tree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					tree.setTitle(result.getString(4));
					tree.setUserId(uid);
					trees.add(tree);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownUserException();
		}
		return trees;
	}

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.ITreeRepository#getTree(int)
	 */
	public Tree getTree(int treeId) throws UnknownTreeException   {
		final String sql = String.format("SELECT MAPPING.UID,TREE.ID,TREE.CREATION_TIME,TREE.LAST_MODIFICATION_TIME,TREE.TITLE FROM %s AS TREE JOIN %s AS MAPPING ON MAPPING.KTRID = TREE.ID WHERE ID = ?",DBTableHelper.TREE,DBTableHelper.TREEMAPPING);

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Tree tree = new Tree();
					tree.setUserId(result.getInt(1));
					tree.setId(result.getInt(2));
					tree.setCreationTime(DateHelper.getDate(result.getString(3)));
					tree.setLastModificationTime(DateHelper.getDate(result.getString(4)));
					tree.setTitle(result.getString(5));
					return tree;
				}
			}
		} catch (Exception e) {
			throw new UnknownTreeException();
		}
		throw new UnknownTreeException();
	}
	
	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.ITreeRepository#createTree(cdar.bll.entity.Tree)
	 */
	public Tree createTree(Tree tree) throws CreationException   {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, TITLE) VALUES (?, ?)",DBTableHelper.TREE);
		final String sql2 = String.format("INSERT INTO %s (uid, ktrid) VALUES (?, ?)",DBTableHelper.TREEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, tree.getTitle());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					tree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new CreationException();
		}
		
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql2)) {
			preparedStatement.setInt(1, tree.getUserId());
			preparedStatement.setInt(2, tree.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new CreationException();
		}
		return tree;
	}
	
	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.ITreeRepository#updateTree(cdar.bll.entity.Tree)
	 */
	public Tree updateTree(Tree tree) throws UnknownTreeException   {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?", DBTableHelper.TREE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, tree.getTitle());
			preparedStatement.setInt(3, tree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownTreeException();
		}
		return tree;
	}
	
	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.ITreeRepository#deleteTree(int)
	 */
	public void deleteTree(int treeId) throws UnknownTreeException   {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.TREE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, treeId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownTreeException();
			}
		} catch (Exception ex) {
			throw new UnknownTreeException();
		}
	}
}
