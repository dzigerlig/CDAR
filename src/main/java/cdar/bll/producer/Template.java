package cdar.bll.producer;

import info.bliki.wiki.model.WikiModel;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

public class Template extends BasicEntity {
	private int treeid;
	private String title;
	private String templatetext;
	private String templatetexthtml;
	private boolean isDefault = false;
	
	public Template() {
		
	}

	public Template(TemplateDao template) {
		this(template.getId(), template.getCreationTime(), template.getLastModificationTime(), template.getTitle(), template.getTemplatetext(), template.getKtrid(), template.getIsDefault());
	}

	public Template(int id, Date creationTime, Date lastModificationTime,
			String title, String templatetext, int treeid, boolean isDefault) {
		super(id, creationTime, lastModificationTime);
		setTreeid(treeid);
		setTitle(title);
		setTemplatetext(templatetext);
		if (getTemplatetext()!=null) {
			setTemplatetexthtml(WikiModel.toHtml(getTemplatetext()));
		}
		setIsDefault(isDefault);
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

	public String getTemplatetexthtml() {
		return templatetexthtml;
	}

	public void setTemplatetexthtml(String templatetexthtml) {
		this.templatetexthtml = templatetexthtml;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
