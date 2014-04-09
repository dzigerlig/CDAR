package cdar.dal.persistence.hibernate.knowledgeconsumer;

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
@Table(name = "knowledgeprojecttree")
@NamedQueries({ @NamedQuery(name = "findProjectTreeById", query = "from KnowledgeProjectTreeDao kptd where kptd.id = :id") })
public class KnowledgeProjectTreeDao {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "creation_time")
	private Date creation_time;

	@Column(name = "last_modification_time")
	private Date last_modification_time;

	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToMany(mappedBy="knowledgeprojecttrees", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = UserDao.class)
	private Set<UserDao> users = new HashSet<UserDao>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name = "kptid")
	private Set<KnowledgeProjectNodeDao> knowledgeProjectNodes;

	public KnowledgeProjectTreeDao() {

	}

	public KnowledgeProjectTreeDao(String name) {
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
		result.append("KnowledgeProject8Tree: ");
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

	public Set<KnowledgeProjectNodeDao> getKnowledgeProjectNodes() {
		return knowledgeProjectNodes;
	}

	public void setKnowledgeProjectNodes(Set<KnowledgeProjectNodeDao> knowledgeProjectNodes) {
		this.knowledgeProjectNodes = knowledgeProjectNodes;
	}
	
	public void deleteKnowledgeProjectNode(KnowledgeProjectNodeDao node) {
		getKnowledgeProjectNodes().remove(node);
	}
}
