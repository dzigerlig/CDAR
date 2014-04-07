package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

public class Template extends BasicEntity {
	private int treeid;
	private String title;
	private String templatetext;
	
	public Template() {
		
	}

	public Template(TemplateDao template) {
		this(template.getId(), template.getCreationTime(), template.getLastModificationTime(), template.getTitle(), template.getTemplatetext(), template.getKtrid());
	}

	public Template(int id, Date creationTime, Date lastModificationTime,
			String title, String templatetext, int treeid) {
		super(id, creationTime, lastModificationTime);
		this.setTreeid(treeid);
		setTitle(title);
		setTemplatetext(templatetext);
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplatetext() {
		return templatetext;
	}

	public void setTemplatetext(String templatetext) {
		this.templatetext = templatetext;
	}
}
