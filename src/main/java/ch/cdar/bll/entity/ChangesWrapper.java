package ch.cdar.bll.entity;

import java.util.List;

/**
 * Wrapper class used to return entities which are connected to a specific change and operation .
 *
 * @author dzigerli
 * @author mtinner
 * @param <T> Generic parameter which specifies the type of returned entities
 */
public class ChangesWrapper<T> {
	
	/** The changed entities. */
	private List<T> changedEntities;
	
	/** The operation. */
	private String operation;

	/**
	 * Default Constructor.
	 *
	 * @param changedEntities list of all connected entities (generic)
	 * @param operation type of operation as string
	 */
	public ChangesWrapper(List<T> changedEntities, String operation) {
		setChangedEntities(changedEntities);
		setOperation(operation);
	}

	/**
	 * Gets the changed entities.
	 *
	 * @return all changed entities as a generic list
	 */
	public List<T> getChangedEntities() {
		return changedEntities;
	}

	/**
	 * Sets the changed entities.
	 *
	 * @param changedEntities generic list of changed entities to be set
	 */
	public void setChangedEntities(List<T> changedEntities) {
		this.changedEntities = changedEntities;
	}

	/**
	 * Gets the operation.
	 *
	 * @return the type of operation as String
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Sets the operation.
	 *
	 * @param operation as String variable to be set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
