package cdar.bll.producer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;

public class Directory extends BasicEntity {
	private int parentId;
	private int refTreeId;
	private String title;
	private Set<Node> nodes;


	public Directory() {
		super();
	}

	public Directory(int id, Date creationTime, Date lastModificationTime,
			int parentId,int refTreeId, String title, Set<Node> nodes) {
		super(id, creationTime, lastModificationTime);
		this.parentId = parentId;
		this.refTreeId = refTreeId;
		this.title = title;
		this.nodes = nodes;
	}

	public Directory(DictionaryDao dd) {
		super(dd.getId(), dd.getCreationTime(), dd.getLastModificationTime());		
		this.title = dd.getTitle();	
		if(dd.getParentDictionaryDao()!=null){
			this.parentId = dd.getParentDictionaryDao().getId();
		}	
		if(dd.getKnowledgeNodes()!=null){
			this.nodes = new HashSet<Node>();
			for(KnowledgeNodeDao knd: dd.getKnowledgeNodes()){
				this.nodes.add(new Node(knd));
			}
		}
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getRefTreeId() {
		return refTreeId;
	}

	public void setRefTreeId(int refTreeId) {
		this.refTreeId = refTreeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}
}
