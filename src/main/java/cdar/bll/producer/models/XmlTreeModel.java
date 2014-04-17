package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cdar.bll.export.CDAR_TreeExportModel;
import cdar.bll.export.CDAR_TreeSimple;
import cdar.bll.producer.XmlTree;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.NodeLinkDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;
import cdar.dal.persistence.jdbc.producer.TemplateDao;
import cdar.dal.persistence.jdbc.producer.TreeDao;
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

	public boolean cleanTree(int treeid) {
		for (NodeDao node : pdc.getNodes(treeid)) {
			if(!node.delete()) {
				return false;
			}
		}
		
		for (DirectoryDao directory : pdc.getDirectories(treeid)) {
			if (!directory.delete()) {
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
		
//		private TreeDao tree;
//		private List<TemplateDao> templates;
//		private List<NodeDao> nodes;
//		private List<SubnodeDao> subnodes;
//		private List<NodeLinkDao> links;
//		private List<DirectoryDao> directories;
		
		
		
		
		int treeId = cts.getTree().getId();
		
//		for (TemplateDao template : cts.getTemplates()) {
//			TemplateDao newTemplate = new TemplateDao(treeId);
//			newTemplate.setIsDefault(template.getIsDefault());
//			newTemplate.setTemplatetext(template.getTemplatetext());
//			newTemplate.setTitle(template.getTitle());
//			template.create();
//		}
		
//		for (Di7rectoryDao directory : cts.getDirectories()) {
//			System.out.println("creating directory");
//			directory.create();
//		}
		
//		for (NodeDao node : cts.getNodes()) {
//			node.create();
//		}
//		
//		for (SubnodeDao subnode : cts.getSubnodes()) {
//			subnode.create();
//		}
//		
//		for (NodeLinkDao nodeLink : cts.getLinks()) {
//			nodeLink.create();
//		}
		
		
		return true;
	}
}
