package cdar.bll.producer.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdar.bll.export.CDAR_TreeExportModel;
import cdar.bll.export.CDAR_TreeSimple;
import cdar.bll.producer.Directory;
import cdar.bll.producer.Node;
import cdar.bll.producer.NodeLink;
import cdar.bll.producer.Subnode;
import cdar.bll.producer.Template;
import cdar.bll.producer.XmlTree;
import cdar.dal.exceptions.UnknownXmlTreeException;
import cdar.dal.persistence.jdbc.producer.NodeLinkRepository;
import cdar.dal.persistence.jdbc.producer.NodeRepository;
import cdar.dal.persistence.jdbc.producer.SubnodeRepository;
import cdar.dal.persistence.jdbc.producer.TemplateRepository;
import cdar.dal.persistence.jdbc.producer.XmlTreeRepository;

public class XmlTreeModel {
	private DirectoryModel dm = new DirectoryModel();
	private NodeRepository nr = new NodeRepository();
	private NodeLinkRepository nlr = new NodeLinkRepository();
	private XmlTreeRepository xtr = new XmlTreeRepository();
	private TemplateRepository tr = new TemplateRepository();
	private SubnodeRepository sr = new SubnodeRepository();

	public Set<XmlTree> getXmlTrees(int treeId) throws SQLException {
		Set<XmlTree> xmlTrees = new HashSet<XmlTree>();

		for (XmlTree xmlTree : xtr.getXmlTrees(treeId)) {
			xmlTrees.add(xmlTree);
		}

		return xmlTrees;
	}

	public XmlTree addXmlTree(int uid, int ktrid) throws Exception {
		CDAR_TreeExportModel tem = new CDAR_TreeExportModel();
		final String xmlString = tem.getTreeSimpleXmlString(ktrid);
		XmlTree xmlTree = new XmlTree();
		xmlTree.setUid(uid);
		xmlTree.setKtrid(ktrid);
		xmlTree.setXmlString(xmlString);
		return xtr.createXmlTree(xmlTree);
	}

	public boolean deleteXmlTree(int xmlTreeId) throws UnknownXmlTreeException, Exception {
		return xtr.deleteXmlTree(xtr.getXmlTree(xmlTreeId));
	}

	public XmlTree getXmlTree(int xmlTreeId) throws UnknownXmlTreeException {
		return xtr.getXmlTree(xmlTreeId);
	}

	public XmlTree updateXmlTree(XmlTree xmlTree) throws UnknownXmlTreeException {
		XmlTree updatedXmlTree = getXmlTree(xmlTree.getId());
		updatedXmlTree.setXmlString(xmlTree.getXmlString());
		return xtr.updateXmlTree(updatedXmlTree);
	}

	public boolean cleanTree(int xmlTreeId) throws Exception {
		NodeRepository nr = new NodeRepository();
		
		XmlTree xmlTree = getXmlTree(xmlTreeId);
		int treeId = xmlTree.getKtrid();
		for (Node node : nr.getNodes(treeId)) {
			if (!nr.deleteNode(node)) {
				return false;
			}
		}

		for (Directory directory : dm.getDirectories(treeId)) {
			if (directory.getParentid()!=0 && !dm.deleteDirectory(directory.getId())) {
				return false;
			}
		}

		for (Template template : tr.getTemplates(treeId)) {
			if (!tr.deleteTemplate(template)) {
				return false;
			}
		}
		return true;
	}

	public boolean setXmlTree(int xmlTreeId) throws Exception {
		
		CDAR_TreeExportModel ctem = new CDAR_TreeExportModel();
		XmlTree xmlTree = getXmlTree(xmlTreeId);
		CDAR_TreeSimple cts = ctem.getTreeSimple(xmlTree.getXmlString());

		if (cts.getTemplates()!=null) {
			for (Template template : cts.getTemplates()) {
				Template newTemplate = new Template();
				newTemplate.setDecisionMade(template.getDecisionMade());
				newTemplate.setIsDefault(template.getIsDefault());
				newTemplate.setTemplatetext(template.getTemplatetext());
				newTemplate.setTitle(template.getTitle());
				newTemplate.setTreeId(template.getTreeId());
				tr.createTemplate(newTemplate);
			}
		}

		List<Directory> directoryList = new ArrayList<Directory>(
				cts.getDirectories());

		Collections.sort(directoryList, new Comparator<Directory>() {
			@Override
			public int compare(Directory directory1, Directory directory2) {
				return new Integer(directory1.getParentid())
						.compareTo(directory2.getParentid());
			}
		});

		Map<Integer, Integer> directoryMapping = new HashMap<Integer, Integer>();

		for (Directory directory : directoryList) {
			if (directory.getParentid()==0) {
				directoryMapping.put(directory.getId(), directory.getId());
			} else {
				Directory newDirectory = new Directory();
				newDirectory.setTitle(directory.getTitle());
				newDirectory.setKtrid(directory.getKtrid());
				newDirectory = dm.addDirectory(directory.getKtrid(), directoryMapping.get(directory.getParentid()), directory.getTitle());
				directoryMapping.put(directory.getId(), newDirectory.getId());
			}
		}

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (cts.getNodes() != null) {
			for (Node node : cts.getNodes()) {
				Node newNode = new Node();
				newNode.setKtrid(node.getKtrid());
				newNode.setDid(directoryMapping.get(node.getDid()));
				newNode.setTitle(node.getTitle());
				newNode.setDynamicTreeFlag(node.getDynamicTreeFlag());
				newNode = nr.createNode(newNode);
				nodeMapping.put(node.getId(), newNode.getId());
			}
		}

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (cts.getSubnodes() != null) {
			for (Subnode subnode : cts.getSubnodes()) {
				Subnode newSubnode = new Subnode();
				newSubnode.setPosition(subnode.getPosition());
				newSubnode.setTitle(subnode.getTitle());
				newSubnode.setKnid(nodeMapping.get(subnode.getKnid()));
				newSubnode = sr.createSubnode(newSubnode);
				subnodeMapping.put(subnode.getId(), newSubnode.getId());
			}
		}

		if (cts.getLinks() != null) {
			for (NodeLink nodeLink : cts.getLinks()) {
				NodeLink newNodeLink = new NodeLink();
				newNodeLink.setKtrid(nodeLink.getKtrid());
				newNodeLink
						.setSourceId(nodeMapping.get(nodeLink.getSourceId()));
				newNodeLink
						.setTargetId(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getKsnid() != 0) {
					newNodeLink
							.setKsnid(subnodeMapping.get(nodeLink.getKsnid()));
				}
				nlr.createNodeLink(newNodeLink);
			}
		}

		return true;
	}
}
