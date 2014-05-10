package cdar.dal.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.TreeXml;
import cdar.dal.DBConnection;
import cdar.dal.DBTableHelper;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownEntityException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownXmlTreeException;

public class XmlTreeRepository {
	public List<TreeXml> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID, TITLE, FULLFLAG FROM %s WHERE KTRID = ?",DBTableHelper.TREEXML);

		List<TreeXml> xmlTrees = new ArrayList<TreeXml>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					TreeXml xmlTree = new TreeXml();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUserId(result.getInt(5));
					xmlTree.setTreeId(result.getInt(6));
					xmlTree.setTitle(result.getString(7));
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

	public TreeXml getXmlTree(int id) throws UnknownXmlTreeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID, TITLE, FULLFLAG FROM %s WHERE ID = ?",DBTableHelper.TREEXML);

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					TreeXml xmlTree = new TreeXml();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUserId(result.getInt(5));
					xmlTree.setTreeId(result.getInt(6));
					xmlTree.setTitle(result.getString(7));
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
	
	public TreeXml createXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, uid, ktrid, xmlstring, title, fullflag) VALUES (?, ?, ?, ?)",DBTableHelper.TREEXML);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, xmlTree.getUserId());
			preparedStatement.setInt(3, xmlTree.getTreeId());
			preparedStatement.setString(4, xmlTree.getXmlString());
			preparedStatement.setString(5, xmlTree.getTitle());
			if (xmlTree.getIsFull()) {
				preparedStatement.setInt(6, 1);
			} else {
				preparedStatement.setInt(6, 0);
			}

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					xmlTree.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
		return xmlTree;
	}
	
	public TreeXml updateXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ? WHERE id = ?",DBTableHelper.TREEXML);
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, xmlTree.getTitle());
			preparedStatement.setInt(3, xmlTree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
		return xmlTree;
	}
	
	public void deleteXmlTree(TreeXml xmlTree) throws UnknownXmlTreeException   {
		final String sql = String.format("DELETE FROM KNOWLEDGETREEXML WHERE ID = ?",DBTableHelper.TREEXML);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, xmlTree.getId());
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownXmlTreeException();
			}
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
	}
}
