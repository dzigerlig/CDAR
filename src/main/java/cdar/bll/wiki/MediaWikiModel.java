package cdar.bll.wiki;

import org.wikipedia.Wiki;

import cdar.bll.producer.models.TemplateModel;

public class MediaWikiModel {
	TemplateModel tm = new TemplateModel();
	
	public void createNewWikiEntry(int ktrid, String title) {
		System.out.println("Creating new wiki entry: " + title);
		Wiki wiki = new Wiki();
		
		try {
			wiki.login("admin", "password");
			final String templateContent = tm.getDefaultKnowledgeTemplate(ktrid);
			if (templateContent == null) {
				System.out.println("NO DEFAULT TEMPLATE SET");
				wiki.edit(title, templateContent, "== CDAR ==");
			} else {
				System.out.println("DEFAULT TEMPLATE IS SET!");
				wiki.edit(title, templateContent, "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
