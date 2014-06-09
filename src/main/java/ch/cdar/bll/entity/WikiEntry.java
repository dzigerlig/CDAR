package ch.cdar.bll.entity;
import info.bliki.wiki.model.WikiModel;

import org.wikipedia.Wiki;

import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.bll.helper.WikiEntryConcurrentHelper;
import ch.cdar.dal.exception.UnknownUserException;
import ch.cdar.dal.helper.PropertyHelper;
import ch.cdar.dal.repository.wiki.WikiRepository;

/**
 * Class which loads wiki page of the specified node or subnode containing raw-string of the text and the html formatted string of the text.
 *
 * @author dzigerli
 * @author mtinner
 */
public class WikiEntry extends WikiEntity {
	
	/** The node id. */
	private int nodeId;
	
	/** The subnode id. */
	private int subnodeId;
	
	/** The wikicontentplain. */
	private String wikicontentplain;
	
	/** The wikicontenthtml. */
	private String wikicontenthtml;
	
	/** The property helper. */
	private PropertyHelper propertyHelper = new PropertyHelper();

	/**
	 * Default constructor.
	 */
	public WikiEntry() {
	}

	/**
	 * Constructor passing the values of a ProjectNode, calls Constructor of WikiEntity and gets the current wiki page.
	 *
	 * @param projectNode the project node
	 */
	public WikiEntry(ProjectNode projectNode) {
		super(projectNode.getId(), projectNode.getCreationTime(), projectNode
				.getLastModificationTime(), projectNode.getTitle(), projectNode
				.getWikititle());
		setNodeId(projectNode.getId());
		WikiEntryConcurrentHelper wec = new WikiEntryConcurrentHelper();
		if (wec.isKeyInMap(projectNode.getWikititle())) {
			setWikiContentPlain(wec.getValue(projectNode.getWikititle()));
			setWikiContentHtml(WikiModel.toHtml(getWikiContentPlain()));
		} else {
			fillWikiContent();
		}
	}

	/**
	 * Constructor passing the values of a Node, calls Constructor of WikiEntity and gets the current wiki page.
	 *
	 * @param node the node
	 */
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

	/**
	 * Constructor passing the values of a Subnode, calls Constructor of WikiEntity and gets the current wiki page.
	 *
	 * @param subnode the subnode
	 */
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

	/**
	 * Private method which gets the current wiki page and sets the raw and html-formatted String.
	 */
	private void fillWikiContent()  {
		Wiki wiki = new Wiki(propertyHelper.getProperty("CDAR_MEDIAWIKI_CONNECTION"), "");

		try {
			setWikiContentPlain(wiki.getPageText(getWikititle()));
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("http://%s/images/${image}", propertyHelper.getProperty("CDAR_MEDIAWIKI_CONNECTION")), String.format("http://%s/index.php/${title}", propertyHelper.getProperty("CDAR_MEDIAWIKI_CONNECTION")));
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the wiki content plain.
	 *
	 * @return raw String of the current Wiki Page
	 */
	public String getWikiContentPlain() {
		return wikicontentplain;
	}

	/**
	 * Sets the wiki content plain.
	 *
	 * @param wikicontentplain String value of the current Wiki Page containing the raw-text
	 */
	public void setWikiContentPlain(String wikicontentplain) {
		this.wikicontentplain = wikicontentplain;
	}

	/**
	 * Gets the wiki content html.
	 *
	 * @return content of the Wiki Page as html formatted String
	 */
	public String getWikiContentHtml() {
		return wikicontenthtml;
	}

	/**
	 * Sets the wiki content html.
	 *
	 * @param wikicontenthtml String value of the current Wiki Page containing the html-formatted text as a String
	 */
	public void setWikiContentHtml(String wikicontenthtml) {
		this.wikicontenthtml = wikicontenthtml;
	}

	/**
	 * method which saves the current values to the specified Wiki Page.
	 *
	 * @param username String value of the Username of the Wiki User
	 * @param password String value of the Password of the Wiki User
	 * @return the current WikiEntry-Object
	 * @throws UnknownUserException Exception which gets thrown if the User could not log in
	 */
	public WikiEntry saveEntry(String username, String password)
			throws UnknownUserException {
		try {
			WikiRepository wikiConnection = new WikiRepository();
			Wiki wiki = wikiConnection.getConnection(username, password);
			wiki.edit(getWikititle(), getWikiContentPlain(), "");
			StringBuilder sb = new StringBuilder();
			WikiModel.toHtml(getWikiContentPlain(), sb, String.format("%s/images/${image}", propertyHelper.getProperty("CDAR_MEDIAWIKI_CONNECTION")), String.format("%s/index.php/${title}", propertyHelper.getProperty("CDAR_MEDIAWIKI_CONNECTION")));
			setWikiContentHtml(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownUserException();
		}
		return this;
	}

	/**
	 * Gets the node id.
	 *
	 * @return id of the specified Node as int value
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 *
	 * @param nodeId of the specified node as int value
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the subnode id.
	 *
	 * @return id of the specified Subnode as int value
	 */
	public int getSubnodeId() {
		return subnodeId;
	}

	/**
	 * Sets the subnode id.
	 *
	 * @param subnodeId of the specified Subnode as int value
	 */
	public void setSubnodeId(int subnodeId) {
		this.subnodeId = subnodeId;
	}
}
