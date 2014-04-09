package cdar.dal.persistence.hibernate.knowledgeconsumer;

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
@Table(name = "knowledgeprojectnode")
@NamedQueries({ @NamedQuery(name = "findKnowledgeProjectNodeById", query = "from KnowledgeProjectNodeDao kpn where kpn.id = :id" )})
public class KnowledgeProjectNodeDao {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name="kptid")
	private KnowledgeProjectTreeDao knowledgeProjectTree;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "wikititle")
	private String wikititle;
	
	@Column(name="nodestatus")
	private int nodestatus;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "kpnid")
	private Set<KnowledgeProjectSubNodeDao> knowledgeProjectSubNodes;
	
	public KnowledgeProjectNodeDao() {
		
	}
	
	public KnowledgeProjectNodeDao(String title, String wikititle) {
		if (wikititle==null) {
			setWikititle(String.format("PROJEKTNODE_%d", getId()));
		} else {
			setWikititle(wikititle);
		}
		setTitle(title);
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
	
	public KnowledgeProjectTreeDao getKnowledgeProjectTree() {
		return knowledgeProjectTree;
	}

	public void setKnowledgeProjectTree(KnowledgeProjectTreeDao knowledgeProjectTree) {
		this.knowledgeProjectTree = knowledgeProjectTree;
	}
	
	public Set<KnowledgeProjectSubNodeDao> getKnowledgeProjectSubNodes() {
		return knowledgeProjectSubNodes;
	}

	public void setKnowledgeProjectSubNodes(Set<KnowledgeProjectSubNodeDao> knowledgeProjectSubNodes) {
		this.knowledgeProjectSubNodes = knowledgeProjectSubNodes;
	}
	
	public void deleteKnowledgeProjectSubNode(KnowledgeProjectSubNodeDao subnode) {
		getKnowledgeProjectSubNodes().remove(subnode);
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	public int getNodestatus() {
		return nodestatus;
	}

	public void setNodestatus(int nodestatus) {
		this.nodestatus = nodestatus;
	}
}
