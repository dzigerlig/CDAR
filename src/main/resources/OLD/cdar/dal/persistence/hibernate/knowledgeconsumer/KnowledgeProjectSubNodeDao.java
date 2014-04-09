package cdar.dal.persistence.hibernate.knowledgeconsumer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "knowledgeprojectsubnode")
@NamedQueries({ @NamedQuery(name = "findKnowledgeProjectSubNodeById", query = "from KnowledgeProjectSubNodeDao kpsn where kpsn.id = :id" )})
public class KnowledgeProjectSubNodeDao {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@ManyToOne
	@JoinColumn(name="kpnid")
	private KnowledgeProjectNodeDao knowledgeProjectNode;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "wikititle")
	private String wikititle;
		
	public KnowledgeProjectSubNodeDao() {
		
	}
	
	public KnowledgeProjectSubNodeDao(String title) {
		setTitle(title);
		setWikititle(String.format("PROJECTSUBNODE_%d", getId()));
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

	public String getWikititle() {
		return wikititle;
	}

	public void setWikititle(String wikititle) {
		this.wikititle = wikititle;
	}

	public KnowledgeProjectNodeDao getKnowledgeProjectTree() {
		return knowledgeProjectNode;
	}

	public void setKnowledgeProjectNode(KnowledgeProjectNodeDao knowledgeProjectNode) {
		this.knowledgeProjectNode = knowledgeProjectNode;
	}
	
	@Override
	public int hashCode() {
	    return id;
	}
}
