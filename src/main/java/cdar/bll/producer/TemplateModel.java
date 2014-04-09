package cdar.bll.producer;

import java.util.HashSet;
import java.util.Set;

import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.TemplateDao;

public class TemplateModel {
	private ProducerDaoController pdc = new ProducerDaoController();
	
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

	public Template addKnowledgeTemplate(Template template) {
		try {
			TemplateDao templatedao = new TemplateDao(template.getTreeid(),
					template.getTitle(), template.getTemplatetext());
			return new Template(templatedao.create());
		} catch (Exception ex) {
			return new Template(-1);
		}
	}

	public boolean deleteTemplate(int id) {
		TemplateDao templatedao = pdc.getTemplate(id);
		return templatedao.delete();
	}
}
