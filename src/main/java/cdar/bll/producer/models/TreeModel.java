package cdar.bll.producer.models;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Tree;
import cdar.bll.user.User;
import cdar.dal.persistence.jdbc.producer.DirectoryRepository;
import cdar.dal.persistence.jdbc.producer.TreeRepository;
import cdar.dal.persistence.jdbc.user.UserRepository;

public class TreeModel {
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
		tree.setUid(user.getId());
		tree.setTitle(treeTitle);
		tree = tr.createTree(tree);
		Directory directory = new Directory();
		directory.setKtrid(tree.getId());
		directory.setTitle(treeTitle);
		dr.createDirectory(directory);
		return tree;
	}

	public boolean deleteTree(int ktreeid) throws Exception {
		return tr.deleteTree(tr.getTree(ktreeid));
	}

	public Tree getTree(int treeId) throws Exception {
		return tr.getTree(treeId);
	}

	public Tree updateTree(Tree tree) throws Exception {
		return tr.updateTree(tree);
	}
}
