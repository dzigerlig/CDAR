package cdar.bll.manager.consumer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.dal.consumer.ProjectDirectoryRepository;
import cdar.dal.exceptions.UnknownDirectoryException;

public class ProjectDirectoryManager {
	private ProjectDirectoryRepository pdr = new ProjectDirectoryRepository();

	public Set<Directory> getDirectories(int treeId) throws SQLException {
		Set<Directory> directories = new HashSet<Directory>();
		for (Directory directory : pdr.getDirectories(treeId)) {
			directories.add(directory);		
		}
		return directories;
	}
	
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException {
		return pdr.getDirectory(directoryId);
	}

	public void deleteDirectory(int directoryId) throws Exception {
		pdr.deleteDirectory(directoryId);
	}

	public Directory addDirectory(Directory directory) throws Exception	
	{ 
		return pdr.createDirectory(directory);
	}

	public Directory renameDirectory(Directory directory) throws Exception {
		Directory updatedDirectory = getDirectory(directory.getId());
		updatedDirectory.setTitle(directory.getTitle());
		return pdr.updateDirectory(updatedDirectory);
	}
	
	public Directory updateDirectory(Directory directory) throws Exception {
		Directory updatedDirectory = pdr.getDirectory(directory.getId());
		if (directory.getParentId()!=0) {
			updatedDirectory.setParentId(directory.getParentId());
		}
		if (directory.getTitle()!=null) {
			updatedDirectory.setTitle(directory.getTitle());
		}
		return pdr.updateDirectory(updatedDirectory);
	}

	public Directory moveDirectory(Directory directory) throws Exception {
		Directory movedDirectory = pdr.getDirectory(directory.getId());
		movedDirectory.setParentId(directory.getParentId());
		return pdr.updateDirectory(movedDirectory);
	}
}
