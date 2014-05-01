package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Directory;
import cdar.dal.DBConnection;
import cdar.dal.exceptions.UnknownDirectoryException;

public class ProjectDirectoryRepository {
	public List<Directory> getDirectories(int treeid) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, TITLE FROM KNOWLEDGEPROJECTDIRECTORY WHERE PTREEID = ?";

		List<Directory> directories = new ArrayList<Directory>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeid);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Directory directory = new Directory();
					directory.setKtrid(treeid);
					directory.setId(result.getInt(1));
					directory.setCreationTime(result.getDate(2));
					directory.setLastModificationTime(result.getDate(3));
					directory.setParentid(result.getInt(4));
					directory.setTitle(result.getString(5));
					directories.add(directory);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return directories;
	}

	public Directory getDirectory(int id) throws UnknownDirectoryException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, PTREEID, TITLE FROM KNOWLEDGEPROJECTDIRECTORY WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Directory directory = new Directory();
					directory.setId(result.getInt(1));
					directory.setCreationTime(result.getDate(2));
					directory.setLastModificationTime(result.getDate(3));
					directory.setParentid(result.getInt(4));
					directory.setKtrid(result.getInt(5));
					directory.setTitle(result.getString(6));
					return directory;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownDirectoryException();
		}
		throw new UnknownDirectoryException();
	}

	public Directory createDirectory(Directory directory) throws Exception {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTDIRECTORY (CREATION_TIME, PARENTID, PTREEID, TITLE) VALUES (?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			if (directory.getParentid() != 0) {
				preparedStatement.setInt(2, directory.getParentid());
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}
			preparedStatement.setInt(3, directory.getKtrid());
			preparedStatement.setString(4, directory.getTitle());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					directory.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw ex;
		}

		return directory;
	}

	public Directory updateDirectory(Directory directory) throws Exception {
		final String sql = "UPDATE KNOWLEDGEPROJECTDIRECTORY SET LAST_MODIFICATION_TIME = ?, PARENTID = ?, PTREEID = ?, TITLE = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1,
					new java.sql.Date(new Date().getTime()));
			if (directory.getParentid() != 0) {
				preparedStatement.setInt(2, directory.getParentid());
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}
			preparedStatement.setInt(3, directory.getKtrid());
			preparedStatement.setString(4, directory.getTitle());
			preparedStatement.setInt(5, directory.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return directory;
	}

	public boolean deleteDirectory(int directoryId) throws Exception {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTDIRECTORY WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, directoryId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownDirectoryException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
