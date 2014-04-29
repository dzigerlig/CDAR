package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.producer.XmlTree;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.persistence.DBConnection;

public class XmlTreeRepository {
	public List<XmlTree> getXmlTrees(int treeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE KTRID = ?";

		List<XmlTree> xmlTrees = new ArrayList<XmlTree>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					XmlTree xmlTree = new XmlTree();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(result.getDate(2));
					xmlTree.setLastModificationTime(result.getDate(3));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUid(result.getInt(5));
					xmlTree.setKtrid(result.getInt(6));
					xmlTrees.add(xmlTree);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return xmlTrees;
	}

	public XmlTree getXmlTree(int id) throws UnknownXmlTreeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, XMLSTRING, UID, KTRID FROM KNOWLEDGETREEXML WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					XmlTree xmlTree = new XmlTree();
					xmlTree.setId(result.getInt(1));
					xmlTree.setCreationTime(result.getDate(2));
					xmlTree.setLastModificationTime(result.getDate(3));
					xmlTree.setXmlString(result.getString(4));
					xmlTree.setUid(result.getInt(5));
					xmlTree.setKtrid(result.getInt(6));
					return xmlTree;
				}
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
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, xmlTree.getUid());
			preparedStatement.setInt(3, xmlTree.getKtrid());
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
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, xmlTree.getUid());
			preparedStatement.setInt(3, xmlTree.getKtrid());
			preparedStatement.setString(4, xmlTree.getXmlString());
			preparedStatement.setInt(5, xmlTree.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownXmlTreeException();
		}
		return xmlTree;
	}
	
	public boolean deleteXmlTree(XmlTree xmlTree) throws Exception {
		final String sql = "DELETE FROM KNOWLEDGETREEXML WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, xmlTree.getId());
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownXmlTreeException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
