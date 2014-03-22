package cdar.bll.model.knowledgeproducer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeOld implements Serializable {
	
	/**
	 * depricated just for testing
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int parentId;
	private String title;
	public ArrayList<String> options = new ArrayList<String>();
	

	public NodeOld() {
		super();
	}
	
	public NodeOld(int id, int parentId, String title) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.title = title;
		this.options.add(id+"Option1");
		this.options.add(id+"Option2");
		this.options.add(id+"Option3");

	}
	
	public int getParent() {
		return parentId;
	}
	public void setParent(int parent) {
		this.parentId = parent;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
}
