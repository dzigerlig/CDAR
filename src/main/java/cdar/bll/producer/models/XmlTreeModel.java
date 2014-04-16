package cdar.bll.producer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.export.CDAR_TreeExportModel;
import cdar.bll.producer.XmlTree;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;
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
	
	public XmlTree getTree(int xmlTreeId) {
		return new XmlTree(pdc.getXmlTree(xmlTreeId));
	}
	
	public XmlTree updateXmlTree(XmlTree xmlTree) {
		XmlTreeDao xmlTreeDao = pdc.getXmlTree(xmlTree.getId());
		xmlTreeDao.setXmlString(xmlTree.getXmlString());
		return new XmlTree(xmlTreeDao.update());
	}

	public boolean setXmlTree(int id) {
		return true;
	}
}
