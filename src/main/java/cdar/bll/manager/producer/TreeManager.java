package cdar.bll.manager.producer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.Directory;
import cdar.bll.entity.Tree;
import cdar.bll.entity.User;
import cdar.dal.producer.DirectoryRepository;
import cdar.dal.producer.TreeRepository;
import cdar.dal.user.UserRepository;

public class TreeManager {
	private TreeRepository tr = new TreeRepository();

	public Set<Tree> getTrees(int uid) throws SQLException {
		Set<Tree> trees = new HashSet<Tree>();
		for (Tree tree : tr.getTrees(uid)) {
			trees.add(tree);
		}
		return trees;
	} 

	public Tree addTree(int uid, String treeTitle) throws Exception {
		DirectoryRepository dr = new DirectoryRepository();		
		User user = new UserRepository().getUser(uid);
		Tree tree = new Tree();
		tree.setUserId(user.getId());
		tree.setTitle(treeTitle);
		tree = tr.createTree(tree);
		Directory directory = new Directory();
		directory.setTreeId(tree.getId());
		directory.setTitle(treeTitle);
		dr.createDirectory(directory);
		return tree;
	}

	public boolean deleteTree(int ktreeId) throws Exception {
		return tr.deleteTree(ktreeId);
	}

	public Tree getTree(int treeId) throws Exception {
		return tr.getTree(treeId);
	}

	public Tree updateTree(Tree tree) throws Exception {
		return tr.updateTree(tree);
	}
}
