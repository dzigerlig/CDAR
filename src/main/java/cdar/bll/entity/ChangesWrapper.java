package cdar.bll.entity;

import java.util.List;

public class ChangesWrapper<T> {
	private List<T> changedEntities;
	private String operation;

	public ChangesWrapper(List<T> changedEntities, String operation) {
		setChangedEntities(changedEntities);
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
