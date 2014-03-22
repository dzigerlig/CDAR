package cdar.bll.model.wiki;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;

import org.wikipedia.Wiki;

import cdar.bll.model.WikiEntity;
import cdar.bll.model.knowledgeconsumer.ProjectNode;
import cdar.bll.model.knowledgeproducer.KnowledgeNode;
import cdar.bll.model.knowledgeproducer.Node;

public class WikiEntry extends WikiEntity {
	private String wikicontentplain;
	private String wikicontenthtml;
	
	
	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationDate(), node.getLastModified(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}
	
	public WikiEntry(KnowledgeNode node) {
		super(node.getId(), node.getCreationDate(), node.getLastModified(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}

	private void fillWikiContent() {
		Wiki c = new Wiki();
		
		try {
			setWikiContentPlain(c.getPageText(getWikiTitle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContenPlain()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getWikiContenPlain() {
		return wikicontentplain;
	}
	
	public void setWikiContentPlain(String wikicontentplain) {
		this.wikicontentplain = wikicontentplain;
	}

	public String getWikiContentHtml() {
		return wikicontenthtml;
	}

	public void setWikiContentHtml(String wikicontenthtml) {
		this.wikicontenthtml = wikicontenthtml;
	}
}
