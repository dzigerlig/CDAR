package cdar.bll.manager;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.UserRole;
import cdar.dal.consumer.ProjectDirectoryRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.helpers.PropertyHelper;
import cdar.dal.interfaces.IDirectoryRepository;
import cdar.dal.producer.DirectoryRepository;

public class DirectoryManager {
	private IDirectoryRepository directoryRepository;
	
	public DirectoryManager(UserRole userRole) {
		if (userRole == UserRole.CONSUMER) {
			directoryRepository = new ProjectDirectoryRepository();
		} else {
			directoryRepository = new DirectoryRepository();
		}
	}
	
	public Set<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException {
		Set<Directory> directories = new HashSet<Directory>();
		for (Directory directory : directoryRepository.getDirectories(treeId)) {
			directories.add(directory);		
		}
		return directories;
	}
	
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException {
		return directoryRepository.getDirectory(directoryId);
	}

	public void deleteDirectory(int directoryId) throws UnknownDirectoryException {
		directoryRepository.deleteDirectory(directoryId);
	}

	public Directory addDirectory(Directory directory) throws CreationException, UnknownDirectoryException
	{ 
		if (directory.getTitle() == null) {
			PropertyHelper propertyHelper = new PropertyHelper();
			directory.setTitle(propertyHelper.getProperty("DIRECTORY_DESCRIPTION"));
		}
		return directoryRepository.createDirectory(directory);
	}

	public Directory renameDirectory(Directory directory) throws UnknownDirectoryException, EntityException {
		Directory updatedDirectory = getDirectory(directory.getId());
		updatedDirectory.setTitle(directory.getTitle());
		return directoryRepository.updateDirectory(updatedDirectory);
	}
	
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

	public Directory moveDirectory(Directory directory) throws UnknownDirectoryException, EntityException {
		Directory movedDirectory = directoryRepository.getDirectory(directory.getId());
		movedDirectory.setParentId(directory.getParentId());
		return directoryRepository.updateDirectory(movedDirectory);
	}
}
