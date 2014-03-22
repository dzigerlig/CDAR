package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "knowledgesubnode")
@NamedQueries({ @NamedQuery(name = "findKnowledgeSubNodeById", query = "from KnowledgeSubNodeDao ksn where ksn.id = :id" )})
public class KnowledgeSubNodeDao {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@ManyToOne
	@JoinColumn(name="knid")
	private KnowledgeNodeDao knowledgeNode;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "wikititle")
	private String wikititle;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "ksnid")
	private Set<KnowledgeNodeLinkDao> links;
		
	public KnowledgeSubNodeDao() {
		
	}
	
	public KnowledgeSubNodeDao(String title) {
		setTitle(title);
		setWikititle(String.format("SUBNODE_%d", getId()));
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

	public KnowledgeNodeDao getKnowledgeTree() {
		return knowledgeNode;
	}

	public void setKnowledgeNode(KnowledgeNodeDao knowledgeNode) {
		this.knowledgeNode = knowledgeNode;
	}
	
	@Override
	public int hashCode() {
	    return id;
	}

	public Set<KnowledgeNodeLinkDao> getLinks() {
		return links;
	}

	public void setLinks(Set<KnowledgeNodeLinkDao> links) {
		this.links = links;
	}
}
