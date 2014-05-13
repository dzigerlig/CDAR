package cdar.bll.entity;

import java.io.InputStream;
import java.util.Properties;

import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.exceptions.UnknownUserException;

public class WikiEntry extends WikiEntity {
	private int nodeId;
	private int subnodeId;
	private String wikicontentplain;
	private String wikicontenthtml;
	private String wikiConnection;

	public WikiEntry() {
		setWikiConnection();
	}

	public WikiEntry(ProjectNode node) {
		super(node.getId(), node.getCreationTime(), node
				.getLastModificationTime(), node.getTitle(), node
				.getWikititle());
		setWikiConnection();
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
		setWikiConnection();
		setNodeId(node.getId());
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
		setWikiConnection();
		setSubnodeId(subnode.getId());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(subnode.getWikititle())) {
			setWikiContentPlain(wec.getValue(subnode.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	private void fillWikiContent() {
		Wiki wiki = new Wiki(wikiConnection, "");

		try {
			setWikiContentPlain(wiki.getPageText(getWikititle()));
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("http://%s/images/${image}", wikiConnection), String.format("http://%s/index.php/${title}", wikiConnection));
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
			Wiki c = new Wiki(wikiConnection, "");
			c.login(username, password);
			c.edit(getWikititle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("%s/images/${image}", wikiConnection), String.format("%s/index.php/${title}", wikiConnection));
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownUserException();
		}
		return this;
	}

	private void setWikiConnection() {
		String resourceName = "cdarconfig.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
			prop.load(resourceStream);
			wikiConnection = prop.getProperty("MEDIAWIKI_CONNECTION");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
