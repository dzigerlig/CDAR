package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Template;
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

	public Template addKnowledgeTemplate(int treeid, String title, String text) {
		TemplateDao templatedao = new TemplateDao(treeid, title, text);
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
}
