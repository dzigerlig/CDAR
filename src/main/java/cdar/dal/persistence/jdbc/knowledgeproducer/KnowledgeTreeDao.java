package cdar.dal.persistence.jdbc.knowledgeproducer;

import java.util.Date;

import cdar.dal.persistence.CdarDao;
import cdar.dal.persistence.CdarJdbcHelper;

public class KnowledgeTreeDao extends CdarJdbcHelper implements CdarDao {
	private int id;
	private Date creationTime;
	private Date lastModificationTime;
	private String name;
	
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
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public CdarDao create() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CdarDao update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}
}
