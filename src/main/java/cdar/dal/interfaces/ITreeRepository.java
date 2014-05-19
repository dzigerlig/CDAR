package cdar.dal.interfaces;

import java.util.List;

import cdar.bll.entity.Tree;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public interface ITreeRepository {
	public List<Tree> getTrees(int uid) throws UnknownUserException, EntityException;
	public Tree getTree(int treeId) throws UnknownTreeException, UnknownProjectTreeException, EntityException;
	public Tree createTree(Tree tree) throws CreationException, EntityException;
	public Tree updateTree(Tree tree) throws UnknownTreeException, UnknownProjectTreeException;
	public void deleteTree(int treeId) throws UnknownTreeException, UnknownProjectTreeException;
}
