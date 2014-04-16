package cdar.bll;

import java.util.List;

public class CDAR_BooleanChanges<T> extends CDAR_Boolean {
	private List<T> changedEntities;

	public CDAR_BooleanChanges(boolean bool, List<T> changes) {
		super(bool);
		setChangedEntities(changes);
	}

	public List<T> getChangedEntities() {
		return changedEntities;
	}

	public void setChangedEntities(List<T> changedEntities) {
		this.changedEntities = changedEntities;
	}

}
