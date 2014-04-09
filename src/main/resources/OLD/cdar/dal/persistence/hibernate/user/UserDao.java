package cdar.dal.persistence.hibernate.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.NamedQuery;

import OLD.cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectTreeDao;
import OLD.cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;

@Entity
@Table(name = "user")
@NamedQueries({ @NamedQuery(name = "findUserByName", query = "from UserDao u where u.username = :username" ),
	@NamedQuery(name = "findUserByAccessToken", query = "from UserDao u where u.accesstoken = :accesstoken")})
public class UserDao {
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "accesstoken")
	private String accesstoken;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = KnowledgeTreeDao.class )
	@JoinTable(name = "knowledgetreemapping", joinColumns = { @JoinColumn(name = "uid")}, inverseJoinColumns = { @JoinColumn(name = "ktrid")})
	private Set<KnowledgeTreeDao> knowledgetrees = new HashSet<KnowledgeTreeDao>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = KnowledgeProjectTreeDao.class )
	@JoinTable(name = "knowledgeprojecttreemapping", joinColumns = { @JoinColumn(name = "uid")}, inverseJoinColumns = { @JoinColumn(name = "kptid")})
	private Set<KnowledgeProjectTreeDao> knowledgeprojecttrees = new HashSet<KnowledgeProjectTreeDao>();
	
	public UserDao() {
		
	}
	
	public UserDao(String username, String password) {
		this.username = username;
		this.password = password;
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Id: ");
		result.append(this.getId());
		result.append(" Creation time: ");
		result.append(this.getCreationTime());
		result.append(" Modification time: ");
		result.append(this.getLastModificationTime());
		result.append(" Username: ");
		result.append(this.getUsername());
		result.append(" Password: ");
		result.append(this.getPassword());
		return result.toString();
	}
	
	public Set<KnowledgeTreeDao> getKnowledgeTrees() {
		return knowledgetrees;
	}
	
	public void setKnowledgeTrees(Set<KnowledgeTreeDao> knowledgetrees) {
		this.knowledgetrees = knowledgetrees;
	}

	public void deleteKnowledgeTree(KnowledgeTreeDao tree) {
		getKnowledgeTrees().remove(tree);
	}
	
	public Set<KnowledgeProjectTreeDao> getKnowledgeProjectTrees() {
		return knowledgeprojecttrees;
	}
	
	public void setKnowledgeProjectTrees(Set<KnowledgeProjectTreeDao> knowledgeprojecttrees) {
		this.knowledgeprojecttrees = knowledgeprojecttrees;
	}
	
	public void deleteKnowledgeProjectTree(KnowledgeProjectTreeDao tree) {
		getKnowledgeProjectTrees().remove(tree);
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
}
