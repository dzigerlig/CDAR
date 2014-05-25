package cdar.bll.reporting;

import java.util.Date;

import cdar.bll.entity.CdarDescriptions;
import cdar.bll.entity.User;
import cdar.bll.manager.UserManager;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;
import cdar.dal.exceptions.UnknownUserException;

public class ReportingBean {
	private int treeId;
	private Date creationTime;
	private CdarDescriptions cdarDescriptions;
	private String username;
	
	public ReportingBean() throws Exception {
		setCreationTime(new Date());
		setCdarDescriptions(new CdarDescriptions());
	}
	
	public void checkCredentials(int uid, String accesstoken) throws Exception {
		UserManager um = new UserManager();
		User user = um.getUser(uid);

		if (!user.getAccesstoken().equals(accesstoken)) {
			throw new UnknownUserException();
		}
		
		setUsername(user.getUsername());
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
