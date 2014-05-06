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

import cdar.bll.entity.XmlTree;
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownEntityException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownXmlTreeException;

public class XmlTreeRepository {
	public List<XmlTree> getXmlTrees(int treeId) throws UnknownTreeException, UnknownEntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE KTRID = ?";

		List<XmlTree> xmlTrees = new ArrayList<XmlTree>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					XmlTree xmlTree = new XmlTree();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUserId(result.getInt(5));
					xmlTree.setTreeId(result.getInt(6));
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

	public XmlTree getXmlTree(int id) throws UnknownXmlTreeException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					XmlTree xmlTree = new XmlTree();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(DateHelper.getDate(result.getString(2)));
					xmlTree.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUserId(result.getInt(5));
					xmlTree.setTreeId(result.getInt(6));
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
	
	public XmlTree createXmlTree(XmlTree xmlTree) throws UnknownXmlTreeException {
		final String sql = "INSERT INTO KNOWLEDGETREEXML (CREATION_TIME, uid, ktrid, xmlstring) VALUES (?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, xmlTree.getUserId());
			preparedStatement.setInt(3, xmlTree.getTreeId());
			preparedStatement.setString(4, xmlTree.getXmlString());

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
	
	public XmlTree updateXmlTree(XmlTree xmlTree) throws UnknownXmlTreeException {
		final String sql = "UPDATE KNOWLEDGETREEXML SET LAST_MODIFICATION_TIME = ?, UID = ?, KTRID = ?, XMLSTRING = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, xmlTree.getUserId());
			preparedStatement.setInt(3, xmlTree.getTreeId());
			preparedStatement.setString(4, xmlTree.getXmlString());
			preparedStatement.setInt(5, xmlTree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
		return xmlTree;
	}
	
	public void deleteXmlTree(XmlTree xmlTree) throws UnknownXmlTreeException   {
		final String sql = "DELETE FROM KNOWLEDGETREEXML WHERE ID = ?";
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
