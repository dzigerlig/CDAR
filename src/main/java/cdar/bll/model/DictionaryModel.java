package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Dictionary;
import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class DictionaryModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public Set<Dictionary> getDictionaries() {
		Set<Dictionary> ln = new HashSet<Dictionary>();
		for (DictionaryDao knd : kpdc.getKnowledgeTreeById(1).getDictionaries()) {
			ln.add(new Dictionary(knd));			
		}
		return ln;
	}

	public void removeDictionaryById(int id) {
		kpdc.removeDictionary(1, id);
	}

	public Dictionary addDictionary(Dictionary d)	
	{ 
		return new Dictionary(kpdc.addDictionary(d.getRefTreeId(), d.getParentId(), d.getTitle()));
	}

	public void renameDictionary(Dictionary d) {
		DictionaryDao dd = kpdc.getDictionaryById(d.getId());
		dd.setTitle(d.getTitle());
		kpdc.updateDictionary(dd);		
	}

	public void moveDictionary(Dictionary d) {
System.out.println("dictionary moved");		
	}
}
