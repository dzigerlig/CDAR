package cdar.bll.entity.producer;

import info.bliki.wiki.model.WikiModel;

import cdar.bll.entity.BasicEntity;

public class Template extends BasicEntity {
	private int treeId;
	private String title;
	private String templatetext;
	private String templatetexthtml;
	private boolean isDefault = false;
	private boolean decisionMade = false;
	private boolean isSubnode = false;
	
	public Template() {
		
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
	
	public boolean getIsSubnode() {
		return isSubnode;
	}
	
	public void setIsSubnode(boolean isSubnode) {
		this.isSubnode = isSubnode;
	}
}
