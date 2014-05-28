package cdar.dal.interfaces;

import java.util.List;

import cdar.bll.entity.Directory;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownUserException;

/**
 * The Interface IDirectoryRepository.
 */
public interface IDirectoryRepository {
	
	/**
	 * Gets the directories.
	 *
	 * @param treeId the tree id
	 * @return the directories
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public List<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException;
	
	/**
	 * Gets the directory.
	 *
	 * @param directoryId the directory id
	 * @return the directory
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws EntityException the entity exception
	 */
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException;
	
	/**
	 * Creates the directory.
	 *
	 * @param directory the directory
	 * @return the directory
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws CreationException the creation exception
	 */
	public Directory createDirectory(Directory directory) throws UnknownDirectoryException, CreationException;
	
	/**
	 * Update directory.
	 *
	 * @param directory the directory
	 * @return the directory
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException;
	
	/**
	 * Delete directory.
	 *
	 * @param directoryId the directory id
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	public void deleteDirectory(int directoryId) throws UnknownDirectoryException;
	
	/**
	 * Gets the root directory.
	 *
	 * @param treeid the treeid
	 * @return the root directory
	 * @throws EntityException the entity exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	public Directory getRootDirectory(int treeid) throws EntityException, UnknownDirectoryException;
}
