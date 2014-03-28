package cdar.bll.model.knowledgeproducer;

import java.util.HashSet;
import java.util.Set;

import cdar.dal.persistence.hibernate.knowledgeproducer.DictionaryDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeNodeDao;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeProducerDaoController;

public class DictionaryModel {
	private KnowledgeProducerDaoController kpdc = new KnowledgeProducerDaoController();


	public Set<Dictionary> getDictionaries() {
		Set<Dictionary> ln = new HashSet<Dictionary>();
		for (DictionaryDao knd : kpdc.getKnowledgeTreeById(1).getDictionaries()) {
			ln.add(new Dictionary(knd));
			if(knd.getParentDictionaryDao()!=null)
			{System.out.println(knd.getParentDictionaryDao().getId());}
			
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
}
