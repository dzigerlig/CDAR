package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cdar.dal.persistence.hibernate.user.UserDao;

@Entity
@Table(name = "knowledgetree")
@NamedQueries({ @NamedQuery(name = "findTreeById", query = "from KnowledgeTreeDao ktd where ktd.id = :id" )})
public class KnowledgeTreeDao {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name="creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToMany(mappedBy="knowledgetrees", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = UserDao.class )
	private Set<UserDao> users = new HashSet<UserDao>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "ktrid")
	private Set<KnowledgeTemplateDao> knowledgeTemplates;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "ktrid")
	private Set<KnowledgeNodeDao> knowledgeNodes;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "ktrid")
	private Set<KnowledgeNodeLinkDao> knowledgeNodeLinks;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "ktrid")
	private Set<DictionaryDao> dictionaries;
	
	public KnowledgeTreeDao() {
		
	}
	
	public KnowledgeTreeDao(String name) {
		setName(name);
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
	
	public void setCreationDate(Date creation_time) {
		this.creation_time = creation_time;
	}
	
	public Date getLastModificationTime() {
		return last_modification_time;
	}
	
	public void setLastModificationTime(Date modification_time) {
		this.last_modification_time = modification_time;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("KnowledgeTree: ");
		result.append(" Id: ");
		result.append(this.getId());
		result.append(" Creation time: ");
		result.append(this.getCreationTime());
		result.append(" Modification time: ");
		result.append(this.getLastModificationTime());
		result.append(" Name: ");
		result.append(this.getName());
		return result.toString();
	}
	
	public Set<UserDao> getUsers() {
		return users;
	}
	
	public void setUsers(Set<UserDao> users) {
		this.users = users;
	}
	
	@Override
	public int hashCode() {
	    return id;
	}

	public Set<KnowledgeTemplateDao> getKnowledgeTemplates() {
		return knowledgeTemplates;
	}

	public void setKnowledgeTemplates(Set<KnowledgeTemplateDao> knowledgeTemplates) {
		this.knowledgeTemplates = knowledgeTemplates;
	}
	
	public void deleteKnowledgeTemplate(KnowledgeTemplateDao template) {
		getKnowledgeTemplates().remove(template);
	}

	public Set<KnowledgeNodeDao> getKnowledgeNodes() {
		return knowledgeNodes;
	}

	public void setKnowledgeNodes(Set<KnowledgeNodeDao> knowledgeNodes) {
		this.knowledgeNodes = knowledgeNodes;
	}
	
	public void deleteKnowledgeNode(KnowledgeNodeDao node) {
		getKnowledgeNodes().remove(node);
	}

	public Set<KnowledgeNodeLinkDao> getKnowledgeNodeLinks() {
		return knowledgeNodeLinks;
	}

	public void setKnowledgeNodeLinks(Set<KnowledgeNodeLinkDao> knowledgeNodeLinks) {
		this.knowledgeNodeLinks = knowledgeNodeLinks;
	}
	
	public Set<DictionaryDao> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(Set<DictionaryDao> dictionaries) {
		this.dictionaries = dictionaries;
	}
	
	public void deleteDictionary(DictionaryDao dictionary) {
		getDictionaries().remove(dictionary);
	}
}
