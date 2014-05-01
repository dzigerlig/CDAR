package cdar.bll.wiki;

import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.WikiEntity;
import cdar.bll.entity.consumer.ProjectNode;

public class WikiEntry extends WikiEntity {
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
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, "http://152.96.56.36/mediawiki/images/${image}", "http://152.96.56.36/mediawiki/index.php/${title}");
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			/*TODO
			new WikiEntryConcurrentHelper().addWikiEntry(getWikiTitle(), getWikiContentPlain());

			MediaWikiCreationModel mwm = new MediaWikiCreationModel(uid, treeid,
					node.getWikititle(), templateContent, wikiHelper);
			mwm.start();*/
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

	public WikiEntry saveEntry(String username, String password) {
		try {
			Wiki c = new Wiki();
			c.login(username, password);
			c.edit(getWikiTitle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, "http://152.96.56.36/mediawiki/images/${image}", "http://152.96.56.36/mediawiki/index.php/${title}");
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			//TODO: create new wiki entry
			e.printStackTrace();
		}
		return this;
	}
}
