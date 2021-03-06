package ch.cdar.dal.repository.producer;

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

import ch.cdar.bll.entity.Directory;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownDirectoryException;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.helper.DBConnection;
import ch.cdar.dal.helper.DBTableHelper;
import ch.cdar.dal.helper.DateHelper;
import ch.cdar.dal.repository.interfaces.IDirectoryRepository;

/**
 * The Class DirectoryRepository.
 */
public class DirectoryRepository implements IDirectoryRepository {
	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#getDirectories(int)
	 */
	public List<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, TITLE FROM %s WHERE KTRID = ?",DBTableHelper.DIRECTORY);

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
			throw new UnknownUserException();
		}
		return directories;
	}

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#getDirectory(int)
	 */
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, KTRID, TITLE FROM DIRECTORY WHERE ID = ?",DBTableHelper.DIRECTORY);

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
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownDirectoryException();
		}
		throw new UnknownDirectoryException();
	}

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#createDirectory(cdar.bll.entity.Directory)
	 */
	public Directory createDirectory(Directory directory) throws UnknownDirectoryException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, PARENTID, KTRID, TITLE) VALUES (?, ?, ?, ?)",DBTableHelper.DIRECTORY);

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
			throw new UnknownDirectoryException();
		}

		return directory;
	}

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#updateDirectory(cdar.bll.entity.Directory)
	 */
	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, PARENTID = ?, KTRID = ?, TITLE = ? WHERE id = ?",DBTableHelper.DIRECTORY);
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

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#deleteDirectory(int)
	 */
	public void deleteDirectory(int directoryId) throws UnknownDirectoryException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.DIRECTORY);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, directoryId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownDirectoryException();
			}
		} catch (Exception ex) {
			System.out.println(directoryId);
			System.out.println(ex.getMessage());
			throw new UnknownDirectoryException();
		}
	}

	/* (non-Javadoc)
	 * @see cdar.dal.interfaces.IDirectoryRepository#getRootDirectory(int)
	 */
	public Directory getRootDirectory(int treeid) throws EntityException, UnknownDirectoryException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, PARENTID, KTRID, TITLE FROM %s WHERE ID = (SELECT MIN(ID) FROM %s WHERE  KTRID = ?)",DBTableHelper.DIRECTORY, DBTableHelper.DIRECTORY);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeid);

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
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownDirectoryException();
		}
		throw new UnknownDirectoryException();
	}
}
