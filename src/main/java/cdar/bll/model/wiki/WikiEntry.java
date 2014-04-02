package cdar.bll.model.wiki;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;

import org.wikipedia.Wiki;

import cdar.bll.WikiEntity;
import cdar.bll.consumer.ProjectNode;
import cdar.bll.producer.KnowledgeNode;

public class WikiEntry extends WikiEntity {
	private String wikicontentplain;
	private String wikicontenthtml;
	
	public WikiEntry() {
		
	}
	
	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node.getLastModified(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}
	
	public WikiEntry(KnowledgeNode node) {
		super(node.getId(), node.getCreationTime(), node.getLastModified(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}

	private void fillWikiContent() {
		Wiki c = new Wiki();
		
		try {
			setWikiContentPlain(c.getPageText(getWikiTitle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getWikiContentPlain() {
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

	public WikiEntry saveEntry() {
		try {
			Wiki c = new Wiki();
			c.login("admin", "password");
			c.edit(getWikiTitle(), getWikiContentPlain(), "");
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
