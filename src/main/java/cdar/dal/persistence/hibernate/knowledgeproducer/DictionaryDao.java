package cdar.dal.persistence.hibernate.knowledgeproducer;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dictionary")
public class DictionaryDao {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@Column(name = "title")
	private String title;
	
	@OneToOne(mappedBy="dictionary", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = KnowledgeNodeDao.class )
	private KnowledgeNodeDao knowledgeNode;
	
	public DictionaryDao() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creation_time;
	}

	public void setCreationTime(Date creation_time) {
		this.creation_time = creation_time;
	}

	public Date getLastModificationTime() {
		return last_modification_time;
	}

	public void setLastModificationTime(Date last_modification_time) {
		this.last_modification_time = last_modification_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public KnowledgeNodeDao getKnowledgeNode() {
		return knowledgeNode;
	}

	public void setKnowledgeNode(KnowledgeNodeDao knowledgeNode) {
		this.knowledgeNode = knowledgeNode;
	}
}
