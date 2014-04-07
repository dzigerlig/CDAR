package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.hibernate.knowledgeproducer.KnowledgeSubNodeDao;
import cdar.dal.persistence.jdbc.producer.SubNodeDao;

public class SubNode extends WikiEntity {
	private int knid;

	public SubNode() {
		super();
	}

	public SubNode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int knid) {
		super(id, creationDate, lastModification, title, wikititle);
		this.knid = knid;
	}

	public SubNode(SubNodeDao snd) {
		this(snd.getId(), snd.getCreationTime(), snd.getLastModificationTime(),snd.getTitle(),snd.getWikititle(),snd.getKnid());
	}

	public int getKnid() {
		return knid;
	}

	public void setKnid(int knid) {
		this.knid = knid;
	}
}
