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
		/*for (DictionaryDao knd : kpdc.get) {
			ln.add(new Dictionary(knd));
		}*/
		return ln;
	}

	public void removeNodeById(int id) {
		System.out.println("remove node"+id);
		kpdc.removeKnowledgeNode(1, id);
	}

	public Node addNode(Node n)	
	{ 
		return new Node(kpdc.addKnowledgeNode(n.getRefTreeId(),n.getTitle()));
	}

}
