package cdar.bll.entity.producer;

import info.bliki.wiki.model.WikiModel;

import java.util.Date;

import cdar.bll.entity.BasicEntity;

public class Template extends BasicEntity {
	private int treeId;
	private String title;
	private String templatetext;
	private String templatetexthtml;
	private boolean isDefault = false;
	private boolean decisionMade = false;
	
	public Template() {
		
	}

	public Template(int id, Date creationTime, Date lastModificationTime,
			String title, String templatetext, int treeid, boolean isDefault, boolean decisionMade) {
		super(id, creationTime, lastModificationTime);
		setTreeId(treeid);
		setTitle(title);
		setTemplatetext(templatetext);
		setIsDefault(isDefault);
		setDecisionMade(decisionMade);
	}

	public Template(int id) {
		setId(id);
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) {
		this.treeId = treeId;
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
		if (templatetext != null) {
			setTemplatetexthtml(WikiModel.toHtml(templatetext));
		}
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
	
	public boolean getDecisionMade() {
		return decisionMade;
	}

	public void setDecisionMade(boolean decisionMade) {
		this.decisionMade = decisionMade;
	}
}
