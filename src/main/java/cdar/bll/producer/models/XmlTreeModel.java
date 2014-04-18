package cdar.bll.producer.models;

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
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;
import cdar.dal.persistence.jdbc.producer.TemplateDao;
import cdar.dal.persistence.jdbc.producer.XmlTreeDao;

public class XmlTreeModel {
	private ProducerDaoController pdc = new ProducerDaoController();

	public Set<XmlTree> getXmlTrees(int treeid) {
		Set<XmlTree> xmlTrees = new HashSet<XmlTree>();

		for (XmlTreeDao xmlTreeDao : pdc.getXmlTrees(treeid)) {
			xmlTrees.add(new XmlTree(xmlTreeDao));
		}

		return xmlTrees;
	}

	public XmlTree addXmlTree(int uid, int ktrid) {
		CDAR_TreeExportModel tem = new CDAR_TreeExportModel();
		final String xmlString = tem.getTreeSimpleXmlString(ktrid);
		XmlTreeDao xmlTreeDao = new XmlTreeDao(uid, ktrid);
		xmlTreeDao.setXmlString(xmlString);
		xmlTreeDao.create();
		return new XmlTree(xmlTreeDao);
	}

	public boolean deleteXmlTree(int xmlTreeId) {
		return pdc.getXmlTree(xmlTreeId).delete();
	}

	public XmlTree getXmlTree(int xmlTreeId) {
		return new XmlTree(pdc.getXmlTree(xmlTreeId));
	}

	public XmlTree updateXmlTree(XmlTree xmlTree) {
		XmlTreeDao xmlTreeDao = pdc.getXmlTree(xmlTree.getId());
		xmlTreeDao.setXmlString(xmlTree.getXmlString());
		return new XmlTree(xmlTreeDao.update());
	}

	public boolean cleanTree(int xmlTreeid) {
		CDAR_TreeExportModel ctem = new CDAR_TreeExportModel();
		XmlTree xmlTree = getXmlTree(xmlTreeid);
		int treeid = xmlTree.getKtrid();
		for (NodeDao node : pdc.getNodes(treeid)) {
			if (!node.delete()) {
				return false;
			}
		}

		for (DirectoryDao directory : pdc.getDirectories(treeid)) {
			if (directory.getParentid()!=0 && !directory.delete()) {
				return false;
			}
		}

		for (TemplateDao template : pdc.getTemplates(treeid)) {
			if (!template.delete()) {
				return false;
			}
		}
		return true;
	}

	public boolean setXmlTree(int xmlTreeId) {
		CDAR_TreeExportModel ctem = new CDAR_TreeExportModel();
		XmlTree xmlTree = getXmlTree(xmlTreeId);
		CDAR_TreeSimple cts = ctem.getTreeSimple(xmlTree.getXmlString());

		if (cts.getTemplates()!=null) {
			for (Template template : cts.getTemplates()) {
				TemplateDao templateDao = new TemplateDao(template);
				templateDao.create();
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
				DirectoryDao directoryDao = new DirectoryDao(directory);
				directoryDao.setParentid(directoryMapping.get(directory.getParentid()));
				directoryDao.create();
				directoryMapping.put(directory.getId(), directoryDao.getId());
			}
		}

		Map<Integer, Integer> nodeMapping = new HashMap<Integer, Integer>();
		if (cts.getNodes() != null) {
			for (Node node : cts.getNodes()) {
				NodeDao nodeDao = new NodeDao(node);
				nodeDao.setDid(directoryMapping.get(node.getDid()));
				nodeDao.create();
				nodeMapping.put(node.getId(), nodeDao.getId());
			}
		}

		Map<Integer, Integer> subnodeMapping = new HashMap<Integer, Integer>();
		if (cts.getSubnodes() != null) {
			for (Subnode subnode : cts.getSubnodes()) {
				SubnodeDao subnodeDao = new SubnodeDao(subnode);
				subnodeDao.setKnid(nodeMapping.get(subnode.getKnid()));
				subnodeDao.create();
				subnodeMapping.put(subnode.getId(), subnodeDao.getId());
			}
		}

		if (cts.getLinks() != null) {
			for (NodeLink nodeLink : cts.getLinks()) {
				NodeLinkDao nodeLinkDao = new NodeLinkDao(nodeLink);
				nodeLinkDao
						.setSourceid(nodeMapping.get(nodeLink.getSourceId()));
				nodeLinkDao
						.setTargetid(nodeMapping.get(nodeLink.getTargetId()));
				if (nodeLink.getKsnid() != 0) {
					nodeLinkDao
							.setKsnid(subnodeMapping.get(nodeLink.getKsnid()));
				}
				nodeLinkDao.create();
			}
		}

		return true;
	}
}