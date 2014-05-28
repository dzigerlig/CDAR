package ch.cdar.bll.entity;

import java.util.Date;

/**
 * Class representing a Tree, used in Role Producer and Consumer.
 *
 * @author dzigerli
 * @author mtinner
 */
public class Tree extends BasicEntity {
	
	/** The title. */
	private String title;
	
	/** The user id. */
	private int userId;
	
	/**
	 * Default Constructor calling the Constructor of the Basic Entity.
	 */
	public Tree() {
		super();
	}

	/**
	 * Constructor with all values of a Tree to be set.
	 *
	 * @param id as int Value of the Tree to be set
	 * @param creationDate as Date Value of the Creation Time
	 * @param lastModification as Date Value of the Last Modification Time
	 * @param title as String Value containing the Title or Name of the Tree
	 */
	public Tree(int id, Date creationDate, Date lastModification,
			String title) {
		super(id, creationDate, lastModification);
		setTitle(title);
	}

	/**
	 * Gets the title.
	 *
	 * @return Title or Name of the specified Tree
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title or name of the Tree to be set as String Value
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the user id.
	 *
	 * @return user id of the User which created the Tree
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId of the user which created the Tree to be set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
