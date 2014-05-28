package ch.cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.cdar.bll.entity.TreeXml;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownEntityException;
import ch.cdar.dal.exceptions.UnknownTreeException;
import ch.cdar.dal.exceptions.UnknownXmlTreeException;
import ch.cdar.dal.helpers.DBConnection;
import ch.cdar.dal.helpers.DBTableHelper;
import ch.cdar.dal.helpers.DateHelper;

/**
 * The Class ProjectTreeXmlRepository.
 */
public class ProjectTreeXmlRepository {
	
	/**
	 * Gets the xml trees.
	 *
	 * @param treeId the tree id
	 * @return the xml trees
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownEntityException the unknown entity exception
	 */
	public List<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, XMLSTRING, UID, TREEID, FULLFLAG FROM %s WHERE TREEID = ?",DBTableHelper.PROJECTTREEXML);

		List<TreeXml> xmlTrees = new ArrayList<TreeXml>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					TreeXml xmlTree = new TreeXml();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setTitle(result.getString(4));
					xmlTree.setXmlString(result.getString(5));
					xmlTree.setUserId(result.getInt(6));
					xmlTree.setTreeId(result.getInt(7));
					xmlTree.setIsFull(result.getInt(8) == 1);
					xmlTrees.add(xmlTree);
				}
			} catch (ParseException e) {
				throw new UnknownEntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownTreeException();
		}
		return xmlTrees;
	}

	/**
	 * Gets the xml tree.
	 *
	 * @param xmlTreeId the xml tree id
	 * @return the xml tree
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 */
	public TreeXml getXmlTree(int xmlTreeId) throws UnknownXmlTreeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, XMLSTRING, UID, TREEID, FULLFLAG FROM %s WHERE ID = ?",DBTableHelper.PROJECTTREEXML);

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, xmlTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					TreeXml xmlTree = new TreeXml();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setTitle(result.getString(4));
					xmlTree.setXmlString(result.getString(5));
					xmlTree.setUserId(result.getInt(6));
					xmlTree.setTreeId(result.getInt(7));
					xmlTree.setIsFull(result.getInt(8) == 1);
					return xmlTree;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownXmlTreeException();
		}

		throw new UnknownXmlTreeException();
	}
	
	/**
	 * Creates the xml tree.
	 *
	 * @param xmlTree the xml tree
	 * @return the tree xml
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 */
	public TreeXml createXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, uid, treeid, xmlstring, fullflag, title) VALUES (?, ?, ?, ?, ?, ?)",DBTableHelper.PROJECTTREEXML);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, xmlTree.getUserId());
			preparedStatement.setInt(3, xmlTree.getTreeId());
			preparedStatement.setString(4, xmlTree.getXmlString());
			if (xmlTree.getIsFull()) {
				preparedStatement.setInt(5, 1);
			} else {
				preparedStatement.setInt(5, 0);
			}
			preparedStatement.setString(6,  xmlTree.getTitle());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					xmlTree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnknownXmlTreeException();
		}
		return xmlTree;
	}
	
	/**
	 * Delete xml tree.
	 *
	 * @param xmlTree the xml tree
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 */
	public void deleteXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException   {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.PROJECTTREEXML);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, xmlTree.getId());
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownXmlTreeException();
			}
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
	}

	/**
	 * Update xml tree.
	 *
	 * @param updatedTreeXml the updated tree xml
	 * @return the tree xml
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 */
	public TreeXml updateXmlTree(TreeXml updatedTreeXml) throws UnknownXmlTreeException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",DBTableHelper.PROJECTTREEXML);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, updatedTreeXml.getTitle());
			preparedStatement.setInt(3, updatedTreeXml.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
		return updatedTreeXml;
	}
}
