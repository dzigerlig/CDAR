package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Template;
import cdar.dal.persistence.jdbc.producer.ProducerDaoRepository;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

public class TemplateModel {
	private ProducerDaoRepository pdc = new ProducerDaoRepository();

	public Set<Template> getKnowledgeTemplates(int ktreeid) {
		Set<Template> templates = new HashSet<Template>();
		for (TemplateDao template : pdc.getTemplates(ktreeid)) {
			templates.add(new Template(template));
		}
		return templates;
	}

	public Template getKnowledgeTemplate(int templateid) {
		return new Template(pdc.getTemplate(templateid));
	}

	public Template addKnowledgeTemplate(int treeid, String title, String text, boolean decisionMade) {
		TemplateDao templatedao = new TemplateDao(treeid, title, text, decisionMade);
		return new Template(templatedao.create());
	}

	public boolean deleteTemplate(int id) {
		TemplateDao templatedao = pdc.getTemplate(id);
		return templatedao.delete();
	}

	public Template updateTemplate(Template template) {
		TemplateDao templatedao = pdc.getTemplate(template.getId());
		templatedao.setKtrid(template.getTreeid());
		templatedao.setTemplatetext(template.getTemplatetext());
		templatedao.setTitle(template.getTitle());
		return new Template(templatedao.update());
	}

	public Set<Template> setDefaultTemplate(int treeid, int templateId) {
		TemplateDao srcTemplate = pdc.getTemplate(templateId);
		
		if (srcTemplate.getIsDefault()) {
			TemplateDao templatedao = pdc.getTemplate(templateId);
			templatedao.setIsDefault(false);
			templatedao.update();
		} else {
			for (TemplateDao templatedao : pdc.getTemplates(treeid)) {
				if (templatedao.getDecisionMade() == srcTemplate.getDecisionMade()) {
					templatedao.setIsDefault(templatedao.getId() == templateId);
					templatedao.update();
				}
			}
		}
		return getKnowledgeTemplates(treeid);
	}

	public String getDefaultKnowledgeTemplate(int ktrid) {
		for (TemplateDao templatedao : pdc.getTemplates(ktrid)) {
			if (templatedao.getIsDefault() && !templatedao.getDecisionMade()) {
				return templatedao.getTemplatetext();
			}
		}
		return null;
	}

	public Template renameTemplate(Template template) {
		TemplateDao templatedao = pdc.getTemplate(template.getId());
		templatedao.setTitle(template.getTitle());
		return new Template(templatedao.update());
	}
}
