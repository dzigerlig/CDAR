package cdar.bll.entity;

/**
 * Directory class which represents a directory in the static tree
 * extends a BasicEntity
 * @author dzigerli
 * @author mtinner
 *
 */
public class Directory extends BasicEntity implements Comparable<Directory> {
	private int parentId;
	private int treeId;
	private String title;

	/**
	 * Default constructor calling the default constructor of BasicEntity
	 */
	public Directory() {
		super();
	}

	/**
	 * 
	 * @return id of the parent directory as a int
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * 
	 * @param parentId as int value representing the id of the parent directory
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * 
	 * @return tree id of the tree to which the directory belongs to
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * 
	 * @param treeId int value of the tree id to be set
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * 
	 * @return title of the directory, i.e. the directory name as a String value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title of the directory, i.e. the directory name to be set (String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * ???
	 */
	@Override
	public int compareTo(Directory o) {
		return Integer.compare(this.parentId, o.parentId);
	}
}
