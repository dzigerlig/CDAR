package cdar.bll.entity;

import java.util.Date;

public class Directory extends BasicEntity {
	private int parentid;
	private int ktrid;
	private String title;


	public Directory() {
		super();
	}

	public Directory(int id, Date creationTime, Date lastModificationTime,
			int parentid,int ktrid, String title) {
		super(id, creationTime, lastModificationTime);
		setParentid(parentid);
		setKtrid(ktrid);
		setTitle(title);
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
}
