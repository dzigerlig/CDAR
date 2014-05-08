package cdar.bll.wiki;

import java.io.InputStream;
import java.util.Properties;

import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import cdar.bll.entity.Node;
import cdar.bll.entity.Subnode;
import cdar.bll.entity.WikiEntity;
import cdar.bll.entity.consumer.ProjectNode;
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
		fillWikiContent();
		setNodeId(node.getId());
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
		fillWikiContent();
		setSubnodeId(subnode.getId());
	}

	private void fillWikiContent() {
		Wiki wiki = new Wiki(wikiConnection,"");

		try {
			setWikiContentPlain(wiki.getPageText(getWikititle()));
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, "http://"+wikiConnection+"/images/${image}", "http://"+wikiConnection+"/index.php/${title}");
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			/*
			 * TODO new WikiEntryConcurrentHelper().addWikiEntry(getWikiTitle(),
			 * getWikiContentPlain());
			 * 
			 * MediaWikiCreationModel mwm = new MediaWikiCreationModel(uid,
			 * treeid, node.getWikititle(), templateContent, wikiHelper);
			 * mwm.start();
			 */
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
			Wiki c = new Wiki(wikiConnection,"");
			c.login(username, password);
			c.edit(getWikititle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb,
					wikiConnection+"/images/${image}",
					wikiConnection+"/index.php/${title}");
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
		try (InputStream resourceStream = loader
				.getResourceAsStream(resourceName)) {			prop.load(resourceStream);
			wikiConnection = prop.getProperty("MEDIAWIKI_CONNECTION");
			System.out.println(wikiConnection);
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
