package ch.cdar.bll.manager;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.Directory;
import ch.cdar.bll.entity.UserRole;
import ch.cdar.dal.exception.CreationException;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownDirectoryException;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.helper.PropertyHelper;
import ch.cdar.dal.repository.consumer.ProjectDirectoryRepository;
import ch.cdar.dal.repository.interfaces.IDirectoryRepository;
import ch.cdar.dal.repository.producer.DirectoryRepository;

/**
 * The Class DirectoryManager manages the Directory (Consumer and Producer).
 */
public class DirectoryManager {
	
	/** The directory repository. */
	private IDirectoryRepository directoryRepository;
	
	/**
	 * Instantiates a new directory manager. Using a different Repository depending on the User Role.
	 *
	 * @param userRole the user role
	 */
	public DirectoryManager(UserRole userRole) {
		if (userRole == UserRole.CONSUMER) {
			directoryRepository = new ProjectDirectoryRepository();
		} else {
			directoryRepository = new DirectoryRepository();
		}
	}
	
	/**
	 * Gets the directories.
	 *
	 * @param treeId the tree id
	 * @return the directories
	 * @throws EntityException the entity exception
	 * @throws UnknownUserException the unknown user exception
	 */
	public Set<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException {
		Set<Directory> directories = new HashSet<Directory>();
		for (Directory directory : directoryRepository.getDirectories(treeId)) {
			directories.add(directory);		
		}
		return directories;
	}
	
	/**
	 * Gets the directory.
	 *
	 * @param directoryId the directory id
	 * @return the directory
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws EntityException the entity exception
	 */
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException {
		return directoryRepository.getDirectory(directoryId);
	}

	/**
	 * Delete directory.
	 *
	 * @param directoryId the directory id
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	public void deleteDirectory(int directoryId) throws UnknownDirectoryException {
		directoryRepository.deleteDirectory(directoryId);
	}

	/**
	 * Adds the directory.
	 *
	 * @param directory the directory
	 * @return the directory
	 * @throws CreationException the creation exception
	 * @throws UnknownDirectoryException the unknown directory exception
	 */
	public Directory addDirectory(Directory directory) throws CreationException, UnknownDirectoryException
	{ 
		if (directory.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			directory.setTitle(propertyHelper.getProperty("CDAR_DIRECTORY_DESCRIPTION"));
		}
		return directoryRepository.createDirectory(directory);
	}

	/**
	 * Update directory.
	 *
	 * @param directory the directory
	 * @return the directory
	 * @throws UnknownDirectoryException the unknown directory exception
	 * @throws EntityException the entity exception
	 */
	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException, EntityException {
		Directory updatedDirectory = directoryRepository.getDirectory(directory.getId());
		if (directory.getParentId()!=0) {
			updatedDirectory.setParentId(directory.getParentId());
		}
		if (directory.getTitle()!=null) {
			updatedDirectory.setTitle(directory.getTitle());
		}
		return directoryRepository.updateDirectory(updatedDirectory);
	}
}
