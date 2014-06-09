package ch.cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.producer.Template;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownTemplateException;
import ch.cdar.dal.exception.UnknownTreeException;
import ch.cdar.dal.exception.UnknownXmlTreeException;
import ch.cdar.dal.repository.producer.TemplateRepository;

/**
 * The Class TemplateManager.
 */
public class TemplateManager {
	
	/** The Template Repository. */
	private TemplateRepository tr = new TemplateRepository();

	/**
	 * Gets the knowledge templates.
	 *
	 * @param ktreeId the ktree id
	 * @return the knowledge templates
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public Set<Template> getKnowledgeTemplates(int ktreeId) throws EntityException, UnknownTreeException {
		Set<Template> templates = new HashSet<Template>();
		for (Template template : tr.getTemplates(ktreeId)) {
			templates.add(template);
		}
		return templates;
	}

	/**
	 * Gets the knowledge template.
	 *
	 * @param templateId the template id
	 * @return the knowledge template
	 * @throws UnknownTemplateException the unknown template exception
	 * @throws EntityException the entity exception
	 */
	public Template getKnowledgeTemplate(int templateId)
			throws UnknownTemplateException, EntityException {
		return tr.getTemplate(templateId);
	}

	/**
	 * Adds the knowledge template.
	 *
	 * @param template the template
	 * @return the template
	 * @throws UnknownTemplateException the unknown template exception
	 */
	public Template addKnowledgeTemplate(Template template)
			throws UnknownTemplateException {
		return tr.createTemplate(template);
	}

	/**
	 * Delete template.
	 *
	 * @param templateId the template id
	 * @throws UnknownTemplateException the unknown template exception
	 */
	public void deleteTemplate(int templateId) throws UnknownTemplateException {
		tr.deleteTemplate(templateId);
	}

	/**
	 * Update template.
	 *
	 * @param template the template
	 * @return the template
	 * @throws UnknownTemplateException the unknown template exception
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public Template updateTemplate(Template template)
			throws UnknownTemplateException, UnknownXmlTreeException,
			EntityException, UnknownTreeException {
		Template updatedTemplate = getKnowledgeTemplate(template.getId());

		if (template.getTemplatetext() != null) {
			updatedTemplate.setTemplatetext(template.getTemplatetext());
		}
		if (template.getTitle() != null) {
			updatedTemplate.setTitle(template.getTitle());
		}

		setDefaultTemplate(template.getTreeId(), template.getId());

		updatedTemplate.setIsDefault(template.getIsDefault());

		return tr.updateTemplate(updatedTemplate);
	}

	/**
	 * Sets the default template.
	 *
	 * @param treeId the tree id
	 * @param templateId the template id
	 * @return the sets the
	 * @throws UnknownTemplateException the unknown template exception
	 * @throws UnknownXmlTreeException the unknown xml tree exception
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public Set<Template> setDefaultTemplate(int treeId, int templateId)
			throws UnknownTemplateException, UnknownXmlTreeException,
			EntityException, UnknownTreeException {
		Template srcTemplate = getKnowledgeTemplate(templateId);

		if (srcTemplate.getIsDefault()) {
			srcTemplate.setIsDefault(false);
			tr.updateTemplate(srcTemplate);
		} else {
			for (Template template : tr.getTemplates(treeId)) {
				if (srcTemplate.getDecisionMade()) {
					if (template.getDecisionMade()) {
						updateTemplateDefaultSetting(templateId, template);
					}
				} else {
					if (srcTemplate.getIsSubnode()) {
						if (template.getIsSubnode()) {
							updateTemplateDefaultSetting(templateId, template);
						}
					} else {
						if (!(template.getIsSubnode() || template.getDecisionMade())) {
							updateTemplateDefaultSetting(templateId, template);
						}
						
					}
				}
			}
		}
		return getKnowledgeTemplates(treeId);
	}

	/**
	 * Update template default setting.
	 *
	 * @param templateId the template id
	 * @param template the template
	 * @throws UnknownTemplateException the unknown template exception
	 */
	private void updateTemplateDefaultSetting(int templateId, Template template) throws UnknownTemplateException {
		template.setIsDefault(template.getId() == templateId);
		tr.updateTemplate(template);
	}

	/**
	 * Gets the default knowledge template text.
	 *
	 * @param treeId the tree id
	 * @return the default knowledge template text
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public String getDefaultKnowledgeTemplateText(int treeId)
			throws EntityException, UnknownTreeException {
		for (Template template : tr.getTemplates(treeId)) {
			if (template.getIsDefault() && !template.getDecisionMade()
					&& !template.getIsSubnode()) {
				return template.getTemplatetext();
			}
		}
		return null;
	}

	/**
	 * Gets the default project template text.
	 *
	 * @param treeId the tree id
	 * @return the default project template text
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public String getDefaultProjectTemplateText(int treeId)
			throws EntityException, UnknownTreeException {
		for (Template template : tr.getTemplates(treeId)) {
			if (template.getIsDefault() && template.getDecisionMade()
					&& !template.getIsSubnode()) {
				return template.getTemplatetext();
			}
		}
		return null;
	}

	/**
	 * Gets the default subnode template text.
	 *
	 * @param treeId the tree id
	 * @return the default subnode template text
	 * @throws EntityException the entity exception
	 * @throws UnknownTreeException the unknown tree exception
	 */
	public String getDefaultSubnodeTemplateText(int treeId)
			throws EntityException, UnknownTreeException {
		for (Template template : tr.getTemplates(treeId)) {
			if (template.getIsDefault() && !template.getDecisionMade()
					&& template.getIsSubnode()) {
				return template.getTemplatetext();
			}
		}
		return null;
	}
}
