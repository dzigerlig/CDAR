package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Node;
import cdar.bll.wiki.MediaWikiCreationModel;
import cdar.bll.wiki.WikiEntryConcurrentHelper;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class NodeModel {

	private ProducerDaoController pdc = new ProducerDaoController();
	private WikiEntryConcurrentHelper wikiHelper = new WikiEntryConcurrentHelper();

	public Set<Node> getNodes(int treeid) {
		Set<Node> nodes = new HashSet<Node>();
		for (NodeDao node : pdc.getNodes(treeid)) {
			nodes.add(new Node(node));
		}
		return nodes;
	}

	public Node getNode(int nodeid) {
		return new Node(pdc.getNode(nodeid));
	}

	public boolean deleteNode(int id) {
		return pdc.getNode(id).delete();
	}

	public Node addNode(int treeid, String title, int did) {
		TemplateModel tm = new TemplateModel();
		String templateContent = tm.getDefaultKnowledgeTemplate(treeid);

		NodeDao node = new NodeDao(treeid, title, did);
		node.create();
		
		if (templateContent==null) {
			templateContent = "== CDAR ==";
		}
		
		wikiHelper.addWikiEntry(node.getWikititle(), templateContent);

		MediaWikiCreationModel mwm = new MediaWikiCreationModel(treeid,
				node.getWikititle(), templateContent, wikiHelper);
		mwm.start();
		return new Node(node);
	}

	public Node dropNode(int id) {
		NodeDao node = pdc.getNode(id);
		node.setDynamicTreeFlag(1);
		return new Node(node.update());
	}

	public Node renameNode(Node n) {
		NodeDao node = pdc.getNode(n.getId());
		node.setTitle(n.getTitle());
		node.setDid(n.getDid());
		return new Node(node.update());
	}

	public Node undropNode(int id) {
		NodeDao node = pdc.getNode(id);
		node.setDynamicTreeFlag(0);
		return new Node(node.update());
	}

	public Node moveNode(Node n) {
		NodeDao node = pdc.getNode(n.getId());
		node.setDid(n.getDid());
		return new Node(node.update());

		// TODO
		// kpdc.moveKnowledgeNode(nodemapping.getKnid(), nodemapping.getDid());
		// KnowledgeNodeDao node =
		// kpdc.getKnowledgeNodeById(nodemapping.getKnid());
		// return new Node(node);
	}

	public Node updateNode(Node node) {
		NodeDao nodedao = pdc.getNode(node.getId());
		nodedao.setDid(node.getDid());
		nodedao.setDynamicTreeFlag(node.getDynamicTreeFlag());
		nodedao.setTitle(node.getTitle());
		nodedao.update();
		return new Node(nodedao);
	}
}
