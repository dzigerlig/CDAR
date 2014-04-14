package cdar.bll.wiki;

import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.bll.WikiEntity;
import cdar.bll.consumer.ProjectNode;
import cdar.bll.producer.Node;
import cdar.bll.producer.Subnode;

public class WikiEntry extends WikiEntity {
	//private MediaWikiModel mwm = new MediaWikiModel();
	
	private String wikicontentplain;
	private String wikicontenthtml;

	public WikiEntry() {

	}

	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikiTitle());
		fillWikiContent();
	}

	public WikiEntry(Node node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikiTitle());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(node.getWikiTitle())) {
			setWikiContentPlain(wec.getValue(node.getWikiTitle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	public WikiEntry(Subnode subnode) {
		super(subnode.getId(), subnode.getCreationTime(), subnode
				.getLastModificationTime(), subnode.getTitle(), subnode
				.getWikiTitle());
		fillWikiContent();
	}

	private void fillWikiContent() {
		Wiki wiki = new Wiki();
		try {
			setWikiContentPlain(wiki.getPageText(getWikiTitle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} catch (Exception e) {
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
