package cdar.bll.manager.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.producer.Template;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownTemplateException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.producer.TemplateRepository;

public class TemplateManager {
	private TemplateRepository tr = new TemplateRepository();

	public Set<Template> getKnowledgeTemplates(int ktreeId)
			throws EntityException, UnknownTreeException {
		Set<Template> templates = new HashSet<Template>();
		for (Template template : tr.getTemplates(ktreeId)) {
			templates.add(template);
		}
		return templates;
	}

	public Template getKnowledgeTemplate(int templateId)
			throws UnknownTemplateException, EntityException {
		return tr.getTemplate(templateId);
	}

	public Template addKnowledgeTemplate(Template template)
			throws UnknownTemplateException {
		return tr.createTemplate(template);
	}

	public void deleteTemplate(int templateId) throws UnknownTemplateException {
		tr.deleteTemplate(templateId);
	}

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
						template.setIsDefault(template.getId() == templateId);
						tr.updateTemplate(template);
					}
				} else {
					if (srcTemplate.getIsSubnode()) {
						if (template.getIsSubnode()) {
							template.setIsDefault(template.getId() == templateId);
							tr.updateTemplate(template);
						}
					} else {
						if (!(template.getIsSubnode() || template.getDecisionMade())) {
							template.setIsDefault(template.getId() == templateId);
							tr.updateTemplate(template);
						}
						
					}
				}
			}
		}
		return getKnowledgeTemplates(treeId);
	}

	public String getDefaultKnowledgeTemplateText(int ktrid)
			throws EntityException, UnknownTreeException {
		for (Template template : tr.getTemplates(ktrid)) {
			if (template.getIsDefault() && !template.getDecisionMade()
					&& !template.getIsSubnode()) {
				return template.getTemplatetext();
			}
		}
		return null;
	}

	public String getDefaultProjectTemplateText(int ktrid)
			throws EntityException, UnknownTreeException {
		for (Template template : tr.getTemplates(ktrid)) {
			if (template.getIsDefault() && template.getDecisionMade()
					&& !template.getIsSubnode()) {
				return template.getTemplatetext();
			}
		}
		return null;
	}

	public Template renameTemplate(Template template)
			throws UnknownTemplateException, UnknownXmlTreeException,
			EntityException {
		Template renamedTemplate = tr.getTemplate(template.getId());
		renamedTemplate.setTitle(template.getTitle());
		return tr.updateTemplate(renamedTemplate);
	}

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
