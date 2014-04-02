package cdar.bll.producer;

import java.util.Date;

import cdar.bll.BasicEntity;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;

public class Directory extends BasicEntity {
	private int parentid;
	private int ktrid;
	private String title;
	private int knid;


	public Directory() {
		super();
	}

	public Directory(int id, Date creationTime, Date lastModificationTime,
			int parentid,int ktrid,int knid, String title) {
		super(id, creationTime, lastModificationTime);
		this.setParentid(parentid);
		this.setKtrid(ktrid);
		this.setTitle(title);
	}

	public Directory(DirectoryDao directoryDao) {
		this(directoryDao.getId(), directoryDao.getCreationTime(),directoryDao.getLastModificationTime(), directoryDao.getParentid(), directoryDao.getKtrid(), directoryDao.getKnid(), directoryDao.getTitle());
	}

	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getKnid() {
		return knid;
	}

	public void setKnid(int knid) {
		this.knid = knid;
	}
}
