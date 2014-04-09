package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
@Table(name = "directory")
@NamedQueries({ @NamedQuery(name = "findDictionaryById", query = "from DictionaryDao dd where dd.id = :id" )})
public class DictionaryDao {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "creation_time")
	private Date creation_time;

	@Column(name = "last_modification_time")
	private Date last_modification_time;

	@ManyToOne
	@JoinColumn(name = "parentid")
	private DictionaryDao parent;

	@ManyToOne
	@JoinColumn(name = "ktrid")
	private KnowledgeTreeDao knowledgeTree;

	@Column(name = "title")
	private String title;

	@OneToMany(mappedBy = "dictionary", fetch = FetchType.EAGER, targetEntity = KnowledgeNodeDao.class)
	private Set<KnowledgeNodeDao> knowledgeNodes = new HashSet<KnowledgeNodeDao>();

	public DictionaryDao() {

	}

	public DictionaryDao(int parentid, String dictionaryTitle) {
		setParentDictionaryDao(parent);
		setTitle(dictionaryTitle);
	}

	public DictionaryDao(String string) {
		setTitle(string);
	}

	public DictionaryDao(KnowledgeTreeDao tree, KnowledgeNodeDao myNode,
			String dictionaryTitle) {
		getKnowledgeNodes().add(myNode);
		setKnowledgeTree(tree);
		setTitle(dictionaryTitle);
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

	public DictionaryDao getParentDictionaryDao() {
		return parent;
	}

	public void setParentDictionaryDao(DictionaryDao parent) {
		this.parent = parent;
	}

	public KnowledgeTreeDao getKnowledgeTree() {
		return knowledgeTree;
	}

	public void setKnowledgeTree(KnowledgeTreeDao knowledgeTree) {
		this.knowledgeTree = knowledgeTree;
	}

	public Set<KnowledgeNodeDao> getKnowledgeNodes() {
		return knowledgeNodes;
	}

	public void setKnowledgeNodes(Set<KnowledgeNodeDao> knowledgeNodes) {
		this.knowledgeNodes = knowledgeNodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DictionaryDao other = (DictionaryDao) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
