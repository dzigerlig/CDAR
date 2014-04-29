package cdar.bll.producer.managers;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Directory;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.persistence.jdbc.producer.DirectoryRepository;

public class DirectoryManager {
	private DirectoryRepository dr = new DirectoryRepository();

	public Set<Directory> getDirectories(int treeId) throws SQLException {
		Set<Directory> directories = new HashSet<Directory>();
		for (Directory directory : dr.getDirectories(treeId)) {
			directories.add(directory);		
		}
		return directories;
	}
	
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException {
		return dr.getDirectory(directoryId);
	}

	public boolean deleteDirectory(int directoryId) throws Exception {
		return dr.deleteDirectory(directoryId);
	}

	public Directory addDirectory(int treeId, int parentId, String title) throws Exception	
	{ 
		Directory directory = new Directory();
		directory.setKtrid(treeId);
		directory.setParentid(parentId);
		directory.setTitle(title);
		return dr.createDirectory(directory);
	}

	public Directory renameDirectory(Directory directory) throws Exception {
		Directory updatedDirectory = getDirectory(directory.getId());
		updatedDirectory.setTitle(directory.getTitle());
		return dr.updateDirectory(updatedDirectory);
	}
	
	public Directory updateDirectory(Directory directory) throws Exception {
		return dr.updateDirectory(directory);
	}

	public Directory moveDirectory(Directory directory) throws Exception {
		Directory movedDirectory = dr.getDirectory(directory.getId());
		movedDirectory.setParentid(directory.getParentid());
		return dr.updateDirectory(movedDirectory);
	}
}
