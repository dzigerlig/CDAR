package cdar.bll;

import java.util.List;

public class CDAR_BooleanChanges<T> {
	private List<T> changedEntities;
	private String operation;

	public CDAR_BooleanChanges(List<T> changes, String operation) {
		setChangedEntities(changes);
		setOperation(operation);
	}

	public List<T> getChangedEntities() {
		return changedEntities;
	}

	public void setChangedEntities(List<T> changedEntities) {
		this.changedEntities = changedEntities;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
