package cdar.bll.wiki;

import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.WikiEntity;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.exceptions.UnknownUserException;

public class WikiEntry extends WikiEntity {
	private String wikicontentplain;
	private String wikicontenthtml;

	public WikiEntry() {

	}

	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikititle());
		fillWikiContent();
	}

	public WikiEntry(Node node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikititle());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(node.getWikititle())) {
			setWikiContentPlain(wec.getValue(node.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	public WikiEntry(Subnode subnode) {
		super(subnode.getId(), subnode.getCreationTime(), subnode
				.getLastModificationTime(), subnode.getTitle(), subnode
				.getWikititle());
		fillWikiContent();
	}

	private void fillWikiContent() {
		Wiki wiki = new Wiki();
		
		try {
			setWikiContentPlain(wiki.getPageText(getWikititle()));
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

	public WikiEntry saveEntry(String username, String password) throws UnknownUserException {
		try {
			Wiki c = new Wiki();
			c.login(username, password);
			c.edit(getWikititle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, "http://152.96.56.36/mediawiki/images/${image}", "http://152.96.56.36/mediawiki/index.php/${title}");
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			throw new UnknownUserException();
		}
		return this;
	}
}
