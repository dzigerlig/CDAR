package cdar.bll.wiki;

import info.bliki.wiki.model.WikiModel;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.wikipedia.Wiki;

import cdar.bll.WikiEntity;
import cdar.bll.consumer.ProjectNode;
import cdar.bll.producer.Node;
import cdar.bll.producer.Subnode;

public class WikiEntry extends WikiEntity {
	private String wikicontentplain;
	private String wikicontenthtml;
	
	public WikiEntry() {
		
	}
	
	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node.getLastModificationTime(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}
	
	public WikiEntry(Node node) {
		super(node.getId(), node.getCreationTime(), node.getLastModificationTime(), node.getTitle(), node.getWikiTitle());
		fillWikiContent();
	}

	public WikiEntry(Subnode subnode) {
		super(subnode.getId(), subnode.getCreationTime(), subnode.getLastModificationTime(), subnode.getTitle(), subnode.getWikiTitle());
		fillWikiContent();
	}

	private void fillWikiContent() {
		Wiki c = new Wiki();

		try {
			setWikiContentPlain(c.getPageText(getWikiTitle()));
			if (getWikiContentPlain()!=null) {
				setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
			}
		} catch (FileNotFoundException e) {
			//Entry doesn't exist, create new and continue
			try {
				c.login("admin", "password");
				setWikiContentPlain(getWikiTitle());
				c.edit(getWikiTitle(), getWikiContentPlain(), "");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		catch (IOException e) {
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
