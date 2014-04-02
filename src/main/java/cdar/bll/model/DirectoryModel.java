package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Directory;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class DirectoryModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public Set<Directory> getDictionaries(int treeId) {
		Set<Directory> ln = new HashSet<Directory>();
		for (DictionaryDao knd : kpdc.getKnowledgeTreeById(treeId).getDictionaries()) {
			ln.add(new Directory(knd));			
		}
		return ln;
	}

	public void removeDictionaryById(int id) {
		kpdc.removeDictionary(1, id);
	}

	public Directory addDictionary(Directory d)	
	{ 
		return new Directory(kpdc.addDictionary(d.getRefTreeId(), d.getParentId(), d.getTitle()));
	}

	public void renameDictionary(Directory d) {
		DictionaryDao dd = kpdc.getDictionaryById(d.getId());
		dd.setTitle(d.getTitle());
		kpdc.updateDictionary(dd);		
	}

	public void moveDictionary(Directory d) {
System.out.println("dictionary moved");		
	}
}
