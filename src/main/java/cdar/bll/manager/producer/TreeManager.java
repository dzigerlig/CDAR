package cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownDirectoryException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.TreeRepository;
import cdar.dal.user.UserRepository;

public class TreeManager {
	private TreeRepository tr = new TreeRepository();

	public Set<Tree> getTrees(int uid) throws UnknownUserException, EntityException {
		Set<Tree> trees = new HashSet<Tree>();
		for (Tree tree : tr.getTrees(uid)) {
			trees.add(tree);
		}
		return trees;
	} 

	public Tree addTree(int uid, Tree tree) throws UnknownUserException, EntityException, CreationException, UnknownDirectoryException {
		DirectoryRepository dr = new DirectoryRepository();		
		User user = new UserRepository().getUser(uid);
		tree.setUserId(user.getId());
		tree = tr.createTree(tree);
		Directory directory = new Directory();
		directory.setTreeId(tree.getId());
		directory.setTitle(tree.getTitle());
		dr.createDirectory(directory);
		return tree;
	}

	public void deleteTree(int ktreeId) throws UnknownTreeException {
		tr.deleteTree(ktreeId);
	}

	public Tree getTree(int treeId) throws UnknownTreeException {
		return tr.getTree(treeId);
	}

	public Tree updateTree(Tree tree) throws UnknownTreeException  {
		Tree updatedTree = tr.getTree(tree.getId());
		
		if (tree.getTitle()!=null) {
			updatedTree.setTitle(tree.getTitle());
		}
		
		return tr.updateTree(updatedTree);
	}
}
