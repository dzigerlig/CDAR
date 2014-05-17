package cdar.bll.entity;
import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.PropertyHelper;
import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.UnknownUserException;
import cdar.dal.wiki.WikiRepositoryq;

public class WikiEntry extends WikiEntity {
	private int nodeId;
	private int subnodeId;
	private String wikicontentplain;
	private String wikicontenthtml;
	
	private PropertyHelper propertyHelper = new PropertyHelper();

	public WikiEntry() {
	}

	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikititle());
		setNodeId(node.getId());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(node.getWikititle())) {
			setWikiContentPlain(wec.getValue(node.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	public WikiEntry(Node node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikititle());
		setNodeId(node.getId());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(node.getWikititle())) {
			setWikiContentPlain(wec.getValue(node.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	public WikiEntry(Subnode subnode)  {
		super(subnode.getId(), subnode.getCreationTime(), subnode
				.getLastModificationTime(), subnode.getTitle(), subnode
				.getWikititle());
		setSubnodeId(subnode.getId());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(subnode.getWikititle())) {
			setWikiContentPlain(wec.getValue(subnode.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	private void fillWikiContent()  {
		Wiki wiki = new Wiki(propertyHelper.getProperty("MEDIAWIKI_CONNECTION"), "");

		try {
			setWikiContentPlain(wiki.getPageText(getWikititle()));
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("http://%s/images/${image}", propertyHelper.getProperty("MEDIAWIKI_CONNECTION")), String.format("http://%s/index.php/${title}", propertyHelper.getProperty("MEDIAWIKI_CONNECTION")));
			setWikiContentHtml(sb.toString());
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

	public WikiEntry saveEntry(String username, String password)
			throws UnknownUserException {
		try {
			WikiRepositoryq wikiConnection = new WikiRepositoryq();
			Wiki wiki = wikiConnection.getConnection(username, password);
			wiki.edit(getWikititle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("%s/images/${image}", propertyHelper.getProperty("MEDIAWIKI_CONNECTION")), String.format("%s/index.php/${title}", propertyHelper.getProperty("MEDIAWIKI_CONNECTION")));
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownUserException();
		}
		return this;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getSubnodeId() {
		return subnodeId;
	}

	public void setSubnodeId(int subnodeId) {
		this.subnodeId = subnodeId;
	}
}
