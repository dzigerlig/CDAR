package cdar.bll.manager.producer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.DirectoryRepository;

public class DirectoryManager {
	private DirectoryRepository dr = new DirectoryRepository();

	public Set<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException  {
		Set<Directory> directories = new HashSet<Directory>();
		for (Directory directory : dr.getDirectories(treeId)) {
			directories.add(directory);		
		}
		return directories;
	}
	
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException {
		return dr.getDirectory(directoryId);
	}

	public void deleteDirectory(int directoryId) throws UnknownDirectoryException  {
		dr.deleteDirectory(directoryId);
	}

	public Directory addDirectory(Directory directory) throws UnknownDirectoryException
	{ 
		return dr.createDirectory(directory);
	}

	public Directory renameDirectory(Directory directory) throws UnknownDirectoryException, EntityException  {
		Directory updatedDirectory = getDirectory(directory.getId());
		updatedDirectory.setTitle(directory.getTitle());
		return dr.updateDirectory(updatedDirectory);
	}
	
	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException, EntityException  {
		Directory updatedDirectory = dr.getDirectory(directory.getId());
		
		if (directory.getParentId()!=0) {
			updatedDirectory.setParentId(directory.getParentId());
		}
		if (directory.getTitle()!=null) {
			updatedDirectory.setTitle(directory.getTitle());
		}
		if (directory.getTreeId()!=0) {
			updatedDirectory.setTreeId(directory.getTreeId());
		}
		
		return dr.updateDirectory(updatedDirectory);
	}

	public Directory moveDirectory(Directory directory) throws UnknownDirectoryException, EntityException   {
		Directory movedDirectory = dr.getDirectory(directory.getId());
		movedDirectory.setParentId(directory.getParentId());
		return dr.updateDirectory(movedDirectory);
	}
}
