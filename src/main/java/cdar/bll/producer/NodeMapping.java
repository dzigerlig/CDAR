package cdar.bll.producer;

public class NodeMapping {	
	private int did;
	private int knid;
	
	public NodeMapping(int did, int knid) {
		super();
		this.did = did;
		this.knid = knid;
	}
	
	public int getDid() {
		return did;
	}
	
	public void setDid(int did) {
		this.did = did;
	}
	
	public int getKnid() {
		return knid;
	}
	
	public void setKnid(int knid) {
		this.knid = knid;
	}
}
