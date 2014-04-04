package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

public class Template extends WikiEntity {
	private int treeid;
	
	public Template() {
		
	}

	public Template(TemplateDao template) {
		this(template.getId(), template.getCreationTime(), template.getLastModificationTime(), template.getTitle(), template.getWikititle(), template.getKtrid());
	}

	public Template(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int treeid) {
		super(id, creationDate, lastModification, title, wikititle);
		this.setTreeid(treeid);
	}

	public Template(int id) {
		setId(id);
	}

	public int getTreeid() {
		return treeid;
	}

	public void setTreeid(int treeid) {
		this.treeid = treeid;
	}
}
