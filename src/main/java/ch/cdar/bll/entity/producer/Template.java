package ch.cdar.bll.entity.producer;

import info.bliki.wiki.model.WikiModel;
import ch.cdar.bll.entity.BasicEntity;

/**
 * The Class Template.
 */
public class Template extends BasicEntity {
	/** The tree id. */
	private int treeId;
	
	/** The title. */
	private String title;
	
	/** The templatetext. */
	private String templatetext;
	
	/** The templatetexthtml. */
	private String templatetexthtml;
	
	/** The is default. */
	private boolean isDefault = false;
	
	/** The decision made. */
	private boolean decisionMade = false;
	
	/** The is subnode. */
	private boolean isSubnode = false;
	
	/**
	 * Instantiates a new template.
	 */
	public Template() {
		
	}

	/**
	 * Gets the tree id.
	 *
	 * @return the tree id
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId the new tree id
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the templatetext.
	 *
	 * @return the templatetext
	 */
	public String getTemplatetext() {
		return templatetext;
	}

	/**
	 * Sets the templatetext.
	 *
	 * @param templatetext the new templatetext
	 */
	public void setTemplatetext(String templatetext) {
		if (templatetext != null) {
			setTemplatetexthtml(WikiModel.toHtml(templatetext));
		}
		this.templatetext = templatetext;
	}

	/**
	 * Gets the templatetexthtml.
	 *
	 * @return the templatetexthtml
	 */
	public String getTemplatetexthtml() {
		return templatetexthtml;
	}

	/**
	 * Sets the templatetexthtml.
	 *
	 * @param templatetexthtml the new templatetexthtml
	 */
	public void setTemplatetexthtml(String templatetexthtml) {
		this.templatetexthtml = templatetexthtml;
	}

	/**
	 * Gets the checks if the template is default.
	 *
	 * @return the checks if is default
	 */
	public boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * Sets the checks if the template is default.
	 *
	 * @param isDefault the new checks if is default
	 */
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	/**
	 * Gets the boolean decision made.
	 *
	 * @return the decision made
	 */
	public boolean getDecisionMade() {
		return decisionMade;
	}

	/**
	 * Sets the boolean decision made.
	 *
	 * @param decisionMade the new decision made
	 */
	public void setDecisionMade(boolean decisionMade) {
		this.decisionMade = decisionMade;
	}
	
	/**
	 * Gets the checks if the template is a subnode template.
	 *
	 * @return the checks if is subnode
	 */
	public boolean getIsSubnode() {
		return isSubnode;
	}
	
	/**
	 * Sets the checks if the template is a subnode template.
	 *
	 * @param isSubnode the new checks if is subnode
	 */
	public void setIsSubnode(boolean isSubnode) {
		this.isSubnode = isSubnode;
	}
}
