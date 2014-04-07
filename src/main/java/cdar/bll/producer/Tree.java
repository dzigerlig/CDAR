package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.TreeDao;

public class Tree extends BasicEntity {

	private String name;

	public Tree(TreeDao tree) {
		this(tree.getId(), tree.getCreationTime(), tree.getLastModificationTime(), tree.getName());
	}

	public Tree(int id, Date creationDate, Date lastModification,
			String name) {
		super(id, creationDate, lastModification);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/*
	private List<Template> knowledgeTemplates;
	private List<KnowledgeNode> knowledgeNodes;
	private String name;
	
	public Tree(KnowledgeTreeDao ktd) {
		setId(ktd.getId());
		setCreationDate(ktd.getCreationTime());
		setLastModified(ktd.getModificationTime());
		setName(ktd.getName());
	}
	
	public List<Template> getKnowledgeTemplates() {
		return knowledgeTemplates;
	}
	
	public void setKnowledgeTemplates(List<Template> knowledgeTemplates) {
		this.knowledgeTemplates = knowledgeTemplates;
	}
	
	public List<KnowledgeNode> getKnowledgeNodes() {
		return knowledgeNodes;
	}
	
	public void setKnowledgeNodes(List<KnowledgeNode> knowledgeNodes) {
		this.knowledgeNodes = knowledgeNodes;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}*/
}
