package cdar.dal.persistence.jdbc.consumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cdar.dal.persistence.CdarJdbcHelper;
import cdar.dal.persistence.JDBCUtil;
import cdar.dal.persistence.jdbc.producer.NodeDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class ConsumerDaoController extends CdarJdbcHelper {
	public List<ProjectTreeDao> getProjectTrees() {
		return getProjectTrees(0);
	}
	
	public List<ProjectTreeDao> getProjectTrees(int uid) {
		String getUsers = null;
		if (uid==0) {
			getUsers = "SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE";
		} else {
			getUsers = String.format("SELECT ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE LEFT JOIN knowledgeprojecttreemapping ON knowledgeprojecttreemapping.kptid = knowledgeprojecttree.id where knowledgeprojecttreemapping.uid = %d;", uid);
		}
		

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectTreeDao> projecttrees = new ArrayList<ProjectTreeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUsers);
			while (result.next()) {
				ProjectTreeDao projecttree = new ProjectTreeDao(uid);
				projecttree.setId(result.getInt(1));
				projecttree.setCreationTime(result.getDate(2));
				projecttree.setLastModificationTime(result.getDate(3));
				projecttree.setName(result.getString(4));
				projecttree.setUid(uid);
				projecttrees.add(projecttree);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projecttrees;
	}
	
	public ProjectTreeDao getProjectTreeById(int id) {
		final String getTreeByIdStatement = String.format("SELECT UID,ID,CREATION_TIME,LAST_MODIFICATION_TIME,NAME FROM KNOWLEDGEPROJECTTREE JOIN KNOWLEDGEPROJECTTREEMAPPING ON KNOWLEDGEPROJECTTREEMAPPING.kptid = KNOWLEDGEPROJECTTREE.id WHERE ID = %d;" , id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectTreeDao projecttree = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getTreeByIdStatement);
			if (result.next()) {
				projecttree = new ProjectTreeDao(result.getInt(1));
				projecttree.setId(result.getInt(2));
				projecttree.setCreationTime(result.getDate(3));
				projecttree.setLastModificationTime(result.getDate(4));
				projecttree.setName(result.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projecttree;
	}
	
	public List<ProjectNodeDao> getProjectNodes(int projecttreeid) {
		String getNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, NODESTATUS, KPTID FROM KNOWLEDGEPROJECTNODE WHERE KPTID = %d;", projecttreeid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectNodeDao> projectnodes = new ArrayList<ProjectNodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNodes);
			while (result.next()) {
				ProjectNodeDao projectnode = new ProjectNodeDao(projecttreeid);
				projectnode.setId(result.getInt(1));
				projectnode.setCreationTime(result.getDate(2));
				projectnode.setLastModificationTime(result.getDate(3));
				projectnode.setTitle(result.getString(4));
				projectnode.setWikititle(result.getString(5));
				projectnode.setNodestatus(result.getInt(6));
				projectnode.setKptid(result.getInt(7));
				projectnodes.add(projectnode);
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectnodes;
	}
	
	public ProjectNodeDao getProjectNode(int id) {
		String getNode = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, NODESTATUS, KPTID FROM KNOWLEDGEPROJECTNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectNodeDao projectnode = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getNode);
			while (result.next()) {
				projectnode = new ProjectNodeDao(result.getInt(7));
				projectnode.setId(result.getInt(7));
				projectnode.setCreationTime(result.getDate(2));
				projectnode.setLastModificationTime(result.getDate(3));
				projectnode.setTitle(result.getString(4));
				projectnode.setWikititle(result.getString(5));
				projectnode.setNodestatus(result.getInt(6));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return projectnode;
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
		String getProjectNodeLink = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, SOURCEID, TARGETID, KPTID, KSNID FROM KNOWLEDGESUBNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectNodeLinkDao projectnodelink = null;

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
	
	public List<ProjectSubNodeDao> getProjectSubNodes(int kpnid) {
		String getProjectSubNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE FROM KNOWLEDGEPROJECTSUBNODE WHERE KPNID = %d;", kpnid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<ProjectSubNodeDao> projectsubnodes = new ArrayList<ProjectSubNodeDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectSubNodes);
			while (result.next()) {
				ProjectSubNodeDao projectsubnode = new ProjectSubNodeDao(kpnid);
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
	
	public ProjectSubNodeDao getProjectSubNode(int id) {
		String getProjectSubNode = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, TITLE, WIKITITLE FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ProjectSubNodeDao projectsubnode = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectSubNode);
			while (result.next()) {
				projectsubnode = new ProjectSubNodeDao(result.getInt(4));
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
	
	public List<CommentDao> getUserComments(int kpnid) {
		String getProjectSubNodes = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, UID, COMMENT FROM USERCOMMENT WHERE KPNID = %d;", kpnid);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		List<CommentDao> usercomments = new ArrayList<CommentDao>();

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getProjectSubNodes);
			while (result.next()) {
				CommentDao usercomment = new CommentDao(kpnid, result.getInt(4), result.getString(5));
				usercomment.setId(result.getInt(1));
				usercomment.setCreationTime(result.getDate(2));
				usercomment.setLastModificationTime(result.getDate(3));
				usercomments.add(usercomment);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return usercomments;
	}
	
	public CommentDao getUserComment(int id) {
		String getUserComment = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, UID, COMMENT FROM USERCOMMENT WHERE ID = %d;", id);

		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		CommentDao usercomment = null;

		try {
			connection = JDBCUtil.getConnection();
			statement = connection.createStatement();

			result = statement.executeQuery(getUserComment);
			while (result.next()) {
				usercomment = new CommentDao(result.getInt(4), result.getInt(5), result.getString(6));
				usercomment.setId(result.getInt(1));
				usercomment.setCreationTime(result.getDate(2));
				usercomment.setLastModificationTime(result.getDate(3));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeConnections(connection, null, statement, null);
		}
		return usercomment;
	}

	public void addKnowledgeTreeToProjectTree(int ktreeid, int ptreeid) {
		// TODO only adding nodelinks!
		ProducerDaoController pdc = new ProducerDaoController();
		for (NodeDao node : pdc.getNodes(ktreeid)) {
			ProjectNodeDao projectnode = new ProjectNodeDao(ptreeid);
			projectnode.setTitle(node.getTitle());
			projectnode.setWikititle(node.getWikititle());
		}
	}
}
