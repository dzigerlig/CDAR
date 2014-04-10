package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Tree;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class TreeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public Set<Tree> getTrees(int uid) {
		Set<Tree> trees = new HashSet<Tree>();
		for (TreeDao tree : pdc.getTrees(uid)) {
			trees.add(new Tree(tree));
		}
		return trees;
	} 

	public Tree addTree(int uid, String treeName) {
		TreeDao tree = new TreeDao(uid, treeName);
		tree.create();
		DirectoryDao directoryDao = new DirectoryDao(tree.getId());
		directoryDao.setTitle(treeName);
		directoryDao.create();
		return new Tree(tree);
	}

	public boolean deleteTree(int ktreeid) {
		return pdc.getTree(ktreeid).delete();
	}

	public Tree getTree(int treeId) {
		return new Tree(pdc.getTree(treeId));
	}

	public Tree updateTree(Tree tree) {
		TreeDao treedao = pdc.getTree(tree.getId());
		treedao.setName(tree.getName());
		return new Tree(treedao.update());
	}
}
