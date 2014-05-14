package cdar.bll.reporting;

import java.util.Date;

import cdar.bll.entity.CdarDescriptions;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class ReportingBean {
	private int treeId;
	private Date creationTime;
	private CdarDescriptions cdarDescriptions;
	
	public ReportingBean() throws Exception {
		setCreationTime(new Date());
		setCdarDescriptions(new CdarDescriptions());
	}

	public int getTreeId() {
		return treeId;
	}

	public void setTreeId(int treeId) throws UnknownTreeException,
			EntityException, UnknownNodeException, UnknownUserException,
			UnknownSubnodeException {
		this.treeId = treeId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public CdarDescriptions getCdarDescriptions() {
		return cdarDescriptions;
	}

	public void setCdarDescriptions(CdarDescriptions cdarDescriptions) {
		this.cdarDescriptions = cdarDescriptions;
	}
}