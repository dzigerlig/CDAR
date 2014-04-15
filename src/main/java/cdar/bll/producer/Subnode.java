package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;
import cdar.dal.persistence.jdbc.producer.SubnodeDao;

public class Subnode extends WikiEntity {
	private int knid;
	private int position;

	public Subnode() {
		super();
	}

	public Subnode(int id, Date creationDate, Date lastModification,
			String title, String wikititle, int knid, int position) {
		super(id, creationDate, lastModification, title, wikititle);
		setKnid(knid);
		setPosition(position);
	}

	public Subnode(SubnodeDao snd) {
		this(snd.getId(), snd.getCreationTime(), snd.getLastModificationTime(),snd.getTitle(),snd.getWikititle(),snd.getKnid(),snd.getPosition());
	}

	public int getKnid() {
		return knid;
	}

	public void setKnid(int knid) {
		this.knid = knid;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}