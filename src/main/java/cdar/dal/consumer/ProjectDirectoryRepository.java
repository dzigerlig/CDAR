package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Directory;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;
import cdar.dal.interfaces.IDirectoryRepository;

public class ProjectDirectoryRepository implements IDirectoryRepository {
	public List<Directory> getDirectories(int treeId) throws EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, TITLE FROM %s WHERE PTREEID = ?",DBTableHelper.PROJECTDIRECTORY);

		List<Directory> directories = new ArrayList<Directory>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Directory directory = new Directory();
					directory.setTreeId(treeId);
					directory.setId(result.getInt(1));
					directory.setCreationTime(DateHelper.getDate(result.getString(2)));
					directory.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					directory.setParentId(result.getInt(4));
					directory.setTitle(result.getString(5));
					directories.add(directory);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new EntityException();
		}
		return directories;
	}

	public Directory getDirectory(int directoryId) throws UnknownDirectoryException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, PTREEID, TITLE FROM %s WHERE ID = ?",DBTableHelper.PROJECTDIRECTORY);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, directoryId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Directory directory = new Directory();
					directory.setId(result.getInt(1));
					directory.setCreationTime(DateHelper.getDate(result.getString(2)));
					directory.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					directory.setParentId(result.getInt(4));
					directory.setTreeId(result.getInt(5));
					directory.setTitle(result.getString(6));
					return directory;
				}
			} catch (ParseException e) {
				throw new UnknownDirectoryException();
			}
		} catch (SQLException ex) {
			throw new UnknownDirectoryException();
		}
		throw new UnknownDirectoryException();
	}

	public Directory createDirectory(Directory directory) throws CreationException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, PARENTID, PTREEID, TITLE) VALUES (?, ?, ?, ?)",DBTableHelper.PROJECTDIRECTORY);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			if (directory.getParentId() != 0) {
				preparedStatement.setInt(2, directory.getParentId());
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}
			preparedStatement.setInt(3, directory.getTreeId());
			preparedStatement.setString(4, directory.getTitle());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					directory.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw new CreationException();
		}

		return directory;
	}

	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException  {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, PARENTID = ?, PTREEID = ?, TITLE = ? WHERE id = ?",DBTableHelper.PROJECTDIRECTORY);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			if (directory.getParentId() != 0) {
				preparedStatement.setInt(2, directory.getParentId());
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}
			preparedStatement.setInt(3, directory.getTreeId());
			preparedStatement.setString(4, directory.getTitle());
			preparedStatement.setInt(5, directory.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownDirectoryException();
		}
		return directory;
	}

	public void deleteDirectory(int directoryId) throws UnknownDirectoryException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.PROJECTDIRECTORY);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, directoryId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownDirectoryException();
			}
		} catch (Exception ex) {
			throw new UnknownDirectoryException();
		}
	}
}
