package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.Template;
import cdar.bll.producer.Tree;
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
		return new Tree(tree.create());
	}

	public boolean removeKnowledgeTreeById(int ktreeid) {
		return pdc.getTreeById(ktreeid).delete();
	}

	public Tree getKnowledgeTreeById(int treeId) {
		return new Tree(pdc.getTreeById(treeId));
	}

	public Node getNodeById(int nodeid) {
		return new Node(pdc.getNode(nodeid));
	}

	public Set<Node> getKnowledgeNodes(int ktreeid) {
		Set<Node> set = new HashSet<Node>();

		for (NodeDao pnd : pdc.getNodes(ktreeid)) {
			set.add(new Node(pnd));
		}
		return set;
	}

	public Directory getDictionariesById(int dictionaryid) {
		return new Directory(pdc.getDirectory(dictionaryid));
	}

	public Set<Template> getKnowledgeTemplates(int ktreeid) {
		Set<Template> templates = new HashSet<Template>();
		for (TemplateDao template : pdc.getTemplates(ktreeid)) {
			templates.add(new Template(template));
		}
		return templates;
	}

	public Template addKnowledgeTemplate(Template template) {
		try {
		TemplateDao templatedao = new TemplateDao(template.getTreeid(), template.getTitle());
		return new Template(templatedao.create());
		} catch(Exception ex) {
			return new Template(-1);
		}
	}
}
