package cdar.bll.entity;

/**
 * Directory class which represents a directory in the static tree
 * extends a BasicEntity.
 *
 * @author dzigerli
 * @author mtinner
 */
public class Directory extends BasicEntity implements Comparable<Directory> {
	
	/** The parent id. */
	private int parentId;
	
	/** The tree id. */
	private int treeId;
	
	/** The title. */
	private String title;

	/**
	 * Default constructor calling the default constructor of BasicEntity.
	 */
	public Directory() {
		super();
	}

	/**
	 * Gets the parent id.
	 *
	 * @return id of the parent directory as a int
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 *
	 * @param parentId as int value representing the id of the parent directory
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the tree id.
	 *
	 * @return tree id of the tree to which the directory belongs to
	 */
	public int getTreeId() {
		return treeId;
	}

	/**
	 * Sets the tree id.
	 *
	 * @param treeId int value of the tree id to be set
	 */
	public void setTreeId(int treeId) {
		this.treeId = treeId;
	}

	/**
	 * Gets the title.
	 *
	 * @return title of the directory, i.e. the directory name as a String value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title of the directory, i.e. the directory name to be set (String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Used for getting the Directories for each hierarchy if represented in a Set
	 *
	 * @param directory the Directory
	 * @return the int
	 */
	@Override
	public int compareTo(Directory directory) {
		return Integer.compare(this.parentId, directory.parentId);
	}
}
