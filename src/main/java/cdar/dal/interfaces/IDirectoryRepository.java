package cdar.dal.interfaces;

import java.util.List;

import cdar.bll.entity.Directory;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownUserException;

public interface IDirectoryRepository {
	public List<Directory> getDirectories(int treeId) throws EntityException, UnknownUserException;
	public Directory getDirectory(int directoryId) throws UnknownDirectoryException, EntityException;
	public Directory createDirectory(Directory directory) throws UnknownDirectoryException, CreationException;
	public Directory updateDirectory(Directory directory) throws UnknownDirectoryException;
	public void deleteDirectory(int directoryId) throws UnknownDirectoryException;
}
