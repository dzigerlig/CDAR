package cdar.bll.producer;

import java.util.Date;

import cdar.bll.WikiEntity;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result
				+ ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
		result = prime * result + getId();
		result = prime
				* result
				+ ((getLastModificationTime() == null) ? 0 : getLastModificationTime()
						.hashCode());		
		result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
		result = prime * result
				+ ((getWikiTitle() == null) ? 0 : getWikiTitle().hashCode());
		result = prime * result + knid;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Subnode))
			return false;
		Subnode other = (Subnode) obj;		

		if (getCreationTime() == null) {
			if (other.getCreationTime() != null)
				return false;
		} else if (!getCreationTime().equals(other.getCreationTime()))
			return false;
		if (getId() != other.getId())
			return false;
		if (getLastModificationTime() == null) {
			if (other.getLastModificationTime() != null)
				return false;
		} else if (!getLastModificationTime().equals(
				other.getLastModificationTime()))
			return false;
		if (getTitle() == null) {
			if (other.getTitle() != null)
				return false;
		} else if (!getTitle().equals(other.getTitle()))
			return false;
		if (getWikiTitle() == null) {
			if (other.getWikiTitle() != null)
				return false;
		} else if (!getWikiTitle().equals(other.getWikiTitle()))
			return false;
		if (knid != other.knid)
			return false;
		if (position != other.position)
			return false;
		return true;
	}
	
	
}