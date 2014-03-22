package cdar.bll.model.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.model.BasicEntity;
import cdar.bll.model.knowledgeconsumer.ProjectTree;
import cdar.bll.model.knowledgeproducer.Tree;
import cdar.dal.persistence.hibernate.knowledgeconsumer.KnowledgeProjectTreeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeTreeDao;
import cdar.dal.persistence.hibernate.user.UserDao;

public class User extends BasicEntity {
	private String username;
	private String password;
	private String accesstoken;
	
	private Set<Tree> knowledgeTrees = new HashSet<Tree>();
	private Set<ProjectTree> projectTrees = new HashSet<ProjectTree>();
	
	public User() {
		
	}
	
	public User(int id, Date creationTime, Date lastModificationTime,
			String username, String password, String accessToken) {
		super(id, creationTime, lastModificationTime);
		this.setUsername(username);
		this.setPassword(password);
		this.setAccesstoken(accessToken);
	}
	
	public User(int id) {
		super(id);
	}
	
	public User(String username, String accesstoken) {
		setUsername(username);
		setAccesstoken(accesstoken);
	}
	
	public User(UserDao userDao) {
		this(userDao.getId(), userDao.getCreationTime(), userDao.getLastModificationTime(), userDao.getUsername(), userDao.getPassword(), userDao.getAccesstoken());
		setKnowledgeTrees(userDao.getKnowledgeTrees());
		setProjectTrees(userDao.getKnowledgeProjectTrees());
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
	
	public Set<Tree> getKnowledgeTrees() {
		return knowledgeTrees;
	}
	
	public void setKnowledgeTrees(Set<KnowledgeTreeDao> set) {
		for (KnowledgeTreeDao ktd : set) {
			knowledgeTrees.add(new Tree(ktd.getId(),ktd.getCreationTime(),ktd.getLastModificationTime(),ktd.getName()));
		}
	}
	
	public Set<ProjectTree> getProjectTrees() {
		return projectTrees;
	} 
	
	public void setProjectTrees(Set<KnowledgeProjectTreeDao> set) {
		for (KnowledgeProjectTreeDao kptd : set) {
			projectTrees.add(new ProjectTree(kptd));
		}
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
}
