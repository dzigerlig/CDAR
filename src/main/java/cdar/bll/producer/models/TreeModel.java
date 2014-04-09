package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Tree;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.TemplateDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class TreeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public Set<Tree> getKnowledgeTreesByUid(int uid) {
		Set<Tree> trees = new HashSet<Tree>();
		for (TreeDao tree : pdc.getTrees(uid)) {
			trees.add(new Tree(tree));
		}
		return trees;
	} 

	public Tree addKnowledgeTreeByUid(int uid, String treeName) {
		TreeDao tree = new TreeDao(uid, treeName);
		tree.create();
		DirectoryDao directoryDao = new DirectoryDao(tree.getId());
		directoryDao.setTitle(treeName);
		directoryDao.create();
		return new Tree(tree);
	}

	public boolean deleteKnowledgeTree(int ktreeid) {
		return pdc.getTreeById(ktreeid).delete();
	}

	public Tree getKnowledgeTree(int treeId) {
		return new Tree(pdc.getTreeById(treeId));
	}
}
