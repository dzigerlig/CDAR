package cdar.bll.wiki;

import org.wikipedia.Wiki;

import cdar.bll.producer.models.NodeModel;
import cdar.bll.producer.models.TemplateModel;

public class MediaWikiModel extends Thread{	
	private int ktrid; 
	private String title;
	private NodeModel nodeModel;
	TemplateModel tm = new TemplateModel();
	
	public MediaWikiModel(int ktrid, String title,NodeModel nodeModel) {
		super();
		setKtrid(ktrid);
		setTitle(title);
		setNodeModel(nodeModel);
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNodeModel(NodeModel nodeModel) {
		this.nodeModel = nodeModel;
	}

	public void createNewWikiEntry() {
		System.out.println("Creating new wiki entry: " + this.title);
		Wiki wiki = new Wiki();
		
		try {
			wiki.login("admin", "password");
			final String templateContent = tm.getDefaultKnowledgeTemplate(this.ktrid);
			if (templateContent == null) {
				System.out.println("NO DEFAULT TEMPLATE SET");
				wiki.edit(this.title, templateContent, "== CDAR ==");
			} else {
				System.out.println("DEFAULT TEMPLATE IS SET!");
				wiki.edit(this.title, templateContent, "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	public void run() {
		createNewWikiEntry();
		this.nodeModel.removeWikiEntry(getTitle());
	}
}
