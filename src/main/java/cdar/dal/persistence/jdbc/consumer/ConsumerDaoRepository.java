package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;

public class ConsumerDaoRepository extends CdarJdbcHelper {
	
	public int getNextProjectSubnodePosition(int projectnodeid) {
		int position = 0;
		
		for (ProjectSubnodeDao projectsubnode : getProjectSubnodes(projectnodeid)) {
			if (projectsubnode.getPosition() > position) {
				position = projectsubnode.getPosition();
			}
		}
		
		return ++position;
	}
	
	public List<ProjectNodeLinkDao> getProjectNodeLinks(int treeid) {
		String getProjectNodeLinks = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE KPTID = %d;", treeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectNodeLinkDao> projectnodelinks = new ArrayList<ProjectNodeLinkDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectNodeLinks);
			while (result.next()) {
				ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(result.getInt(4), result.getInt(5), treeid);
				projectnodelink.setId(result.getInt(1));
				projectnodelink.setCreationTime(result.getDate(2));
				projectnodelink.setLastModificationTime(result.getDate(3));
				projectnodelink.setKpnsnid(result.getInt(6));
				projectnodelinks.add(projectnodelink);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectnodelinks;
	}
	
	public ProjectNodeLinkDao getProjectNodeLink(int id) {
		String getProjectNodeLink = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KPNSNID FROM KNOWLEDGEPROJECTNODELINK WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectNodeLinkDao projectnodelink = new ProjectNodeLinkDao(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectNodeLink);
			while (result.next()) {
				projectnodelink = new ProjectNodeLinkDao(result.getInt(4), result.getInt(5), result.getInt(6));
				projectnodelink.setId(result.getInt(1));
				projectnodelink.setCreationTime(result.getDate(2));
				projectnodelink.setLastModificationTime(result.getDate(3));
				projectnodelink.setKpnsnid(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectnodelink;
	}
	
	public List<ProjectSubnodeDao> getProjectSubnodes(int kpnid) {
		String getProjectSubNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION FROM KNOWLEDGEPROJECTSUBNODE WHERE KPNID = %d;", kpnid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectSubnodeDao> projectsubnodes = new ArrayList<ProjectSubnodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectSubNodes);
			while (result.next()) {
				ProjectSubnodeDao projectsubnode = new ProjectSubnodeDao(kpnid, result.getInt(6));
				projectsubnode.setId(result.getInt(1));
				projectsubnode.setCreationTime(result.getDate(2));
				projectsubnode.setLastModificationTime(result.getDate(3));
				projectsubnode.setTitle(result.getString(4));
				projectsubnode.setWikititle(result.getString(5));
				projectsubnodes.add(projectsubnode);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectsubnodes;
	}
	
	public ProjectSubnodeDao getProjectSubnode(int id) {
		String getProjectSubNode = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, TITLE, WIKITITLE, POSITION FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectSubnodeDao projectsubnode = new ProjectSubnodeDao();
		projectsubnode.setId(-1);

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectSubNode);
			while (result.next()) {
				projectsubnode = new ProjectSubnodeDao(result.getInt(4), result.getInt(7));
				projectsubnode.setId(result.getInt(1));
				projectsubnode.setCreationTime(result.getDate(2));
				projectsubnode.setLastModificationTime(result.getDate(3));
				projectsubnode.setTitle(result.getString(5));
				projectsubnode.setWikititle(result.getString(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectsubnode;
	}
}
