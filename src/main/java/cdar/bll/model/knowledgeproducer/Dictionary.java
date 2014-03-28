package cdar.bll.model.knowledgeproducer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import cdar.bll.model.BasicEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;

public class Dictionary extends BasicEntity {
	private int parentId;
	private int refTreeId;
	private String title;
	private Set<Node> nodes;


	public Dictionary() {
		super();
	}

	public Dictionary(int id, Date creationTime, Date lastModificationTime,
			int parentId,int refTreeId, String title) {
		super(id, creationTime, lastModificationTime);
		this.parentId = parentId;
		this.refTreeId = refTreeId;
		this.title = title;
	}

	public Dictionary(DictionaryDao dd) {
		super(dd.getId(), dd.getCreationTime(), dd.getLastModificationTime());		
		this.title = dd.getTitle();	
		if(dd.getParentDictionaryDao()!=null){
			this.parentId = dd.getParentDictionaryDao().getId();
		}	
		if(dd.getKnowledgeNodes()!=null){
			for(KnowledgeNodeDao knd: dd.getKnowledgeNodes()){
				//this.nodes.add(new Node(knd));
				int i =3;
				i++;
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
