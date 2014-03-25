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
@Table(name = "knowledgenode")
@NamedQueries({ @NamedQuery(name = "findKnowledgeNodeById", query = "from KnowledgeNodeDao kn where kn.id = :id" )})
public class KnowledgeNodeDao {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name="ktrid")
	private KnowledgeTreeDao knowledgeTree;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "wikititle")
	private String wikititle;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "knid")
	private Set<KnowledgeSubNodeDao> knowledgeSubNodes;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "sourceid")
	private Set<KnowledgeNodeLinkDao> linksAsSource;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name = "targetid")
	private Set<KnowledgeNodeLinkDao> linksAsTarget;
	
	public KnowledgeNodeDao() {
		
	}
	
	public KnowledgeNodeDao(String title) {
		setTitle(title);
		setWikititle(String.format("NODE_%d", getId()));
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

	public KnowledgeTreeDao getKnowledgeTree() {
		return knowledgeTree;
	}

	public void setKnowledgeTree(KnowledgeTreeDao knowledgeTree) {
		this.knowledgeTree = knowledgeTree;
	}
	
	public Set<KnowledgeSubNodeDao> getKnowledgeSubNodes() {
		return knowledgeSubNodes;
	}

	public void setKnowledgeSubNodes(Set<KnowledgeSubNodeDao> knowledgeSubNodes) {
		this.knowledgeSubNodes = knowledgeSubNodes;
	}
	
	public void deleteKnowledgeSubNode(KnowledgeSubNodeDao subnode) {
		getKnowledgeSubNodes().remove(subnode);
	}
	
	@Override
	public int hashCode() {
	    return id;
	}

	public Set<KnowledgeNodeLinkDao> getLinksAsSource() {
		return linksAsSource;
	}

	public void setLinksAsSource(Set<KnowledgeNodeLinkDao> linksAsSource) {
		this.linksAsSource = linksAsSource;
	}
	
	public void deleteSourceLink(KnowledgeNodeLinkDao link) {
		getLinksAsSource().remove(link);
	}

	public Set<KnowledgeNodeLinkDao> getLinksAsTarget() {
		return linksAsTarget;
	}

	public void setLinksAsTarget(Set<KnowledgeNodeLinkDao> linksAsTarget) {
		this.linksAsTarget = linksAsTarget;
	}
	
	public void deleteTargetLink(KnowledgeNodeLinkDao link) {
		getLinksAsTarget().remove(link);
	}
}
