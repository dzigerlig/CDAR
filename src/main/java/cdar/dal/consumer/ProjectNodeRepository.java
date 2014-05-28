package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.consumer.ProjectNode;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectTreeException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;

/**
 * The Class ProjectNodeRepository.
 */
public class ProjectNodeRepository {
	
	/**
	 * Gets the nodes.
	 *
	 * @param projectTreeId the project tree id
	 * @return the nodes
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public List<ProjectNode> getNodes(int projectTreeId)
			throws UnknownProjectTreeException, EntityException {
		final String sql = String
				.format("SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, PNODE.INHERITEDTREEID, MAPPING.PDID FROM %s AS PNODE, %s AS MAPPING WHERE PNODE.KPTID = ? AND PNODE.ID = MAPPING.KPNID",
						DBTableHelper.PROJECTNODE,
						DBTableHelper.PROJECTNODEMAPPING);
		List<ProjectNode> projectNodes = new ArrayList<ProjectNode>();
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectTreeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(DateHelper.getDate(result
							.getString(2)));
					projectNode.setLastModificationTime(DateHelper
							.getDate(result.getString(3)));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikititle(result.getString(5));
					projectNode.setDynamicTreeFlag(result.getInt(6));
					projectNode.setStatus(result.getInt(7));
					projectNode.setInheritedTreeId(result.getInt(8));
					projectNode.setDirectoryId(result.getInt(9));
					projectNode.setTreeId(projectTreeId);
					projectNodes.add(projectNode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			throw new UnknownProjectTreeException();
		}
		return projectNodes;
	}

	/**
	 * Gets the node.
	 *
	 * @param projectNodeId the project node id
	 * @return the node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws EntityException the entity exception
	 */
	public ProjectNode getNode(int projectNodeId)
			throws UnknownProjectNodeException, EntityException {
		final String sql = String
				.format("SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, PNODE.KPTID, PNODE.INHERITEDTREEID, MAPPING.PDID FROM %s AS PNODE, %s AS MAPPING WHERE PNODE.ID = ? AND PNODE.ID = MAPPING.KPNID",
						DBTableHelper.PROJECTNODE,
						DBTableHelper.PROJECTNODEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectNodeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode projectNode = new ProjectNode();
					projectNode.setId(result.getInt(1));
					projectNode.setCreationTime(DateHelper.getDate(result
							.getString(2)));
					projectNode.setLastModificationTime(DateHelper
							.getDate(result.getString(3)));
					projectNode.setTitle(result.getString(4));
					projectNode.setWikititle(result.getString(5));
					projectNode.setDynamicTreeFlag(result.getInt(6));
					projectNode.setStatus(result.getInt(7));
					projectNode.setTreeId(result.getInt(8));
					projectNode.setInheritedTreeId(result.getInt(9));
					projectNode.setDirectoryId(result.getInt(10));
					return projectNode;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			throw new UnknownProjectNodeException();
		}
		throw new UnknownProjectNodeException();
	}

	/**
	 * Creates the node.
	 *
	 * @param projectNode the project node
	 * @return the project node
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws CreationException the creation exception
	 */
	public ProjectNode createNode(ProjectNode projectNode)
			throws UnknownProjectTreeException, CreationException {
		final String sqlProjectNode = String
				.format("INSERT INTO %s (CREATION_TIME, TITLE, KPTID, WIKITITLE, DYNAMICTREEFLAG, NODESTATUS, INHERITEDTREEID) VALUES (?, ?, ?, ?, ?, ?, ?)",
						DBTableHelper.PROJECTNODE);
		final String sqlMapping = String.format(
				"INSERT INTO %s (kpnid, pdid) VALUES (?, ?)",
				DBTableHelper.PROJECTNODEMAPPING);
		final String sqlWikiUpdate = String.format(
				"UPDATE %s SET WIKITITLE = ? where id = ?",
				DBTableHelper.PROJECTNODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlProjectNode,
								Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setInt(3, projectNode.getTreeId());
			preparedStatement.setString(4, projectNode.getWikititle());
			preparedStatement.setInt(5, projectNode.getDynamicTreeFlag());
			preparedStatement.setInt(6, projectNode.getStatus());
			preparedStatement.setInt(7, projectNode.getInheritedTreeId());

			preparedStatement.executeUpdate();
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectNode.setId(generatedKeys.getInt(1));
					if (projectNode.getWikititle() == null
							|| projectNode.getWikititle().isEmpty()) {
						projectNode.setWikititle(String.format(
								"PROJECTNODE_%d", projectNode.getId()));
					}
				}
			}
		} catch (Exception ex) {
			throw new UnknownProjectTreeException();
		}

		// wiki update
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlWikiUpdate)) {
			preparedStatement.setString(1, projectNode.getWikititle());
			preparedStatement.setInt(2, projectNode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationException();
		}

		// projectNodeMapping
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, projectNode.getId());
			preparedStatement.setInt(2, projectNode.getDirectoryId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println("mapping");
			throw new UnknownProjectTreeException();
		}

		return projectNode;
	}

	/**
	 * Update node.
	 *
	 * @param projectNode the project node
	 * @return the project node
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	public ProjectNode updateNode(ProjectNode projectNode)
			throws UnknownProjectNodeException, UnknownNodeException {
		final String sql = String
				.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, TITLE = ?, DYNAMICTREEFLAG =?, NODESTATUS = ?, WIKITITLE = ?  WHERE id = ?",
						DBTableHelper.PROJECTNODE);
		final String sqlMapping = String
				.format("INSERT INTO %s (PDID, KPNID) VALUES (?, ?) ON DUPLICATE KEY UPDATE PDID = ?",
						DBTableHelper.PROJECTNODEMAPPING);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setString(2, projectNode.getTitle());
			preparedStatement.setInt(3, projectNode.getDynamicTreeFlag());
			preparedStatement.setInt(4, projectNode.getStatus());
			preparedStatement.setString(5, projectNode.getWikititle());
			preparedStatement.setInt(6, projectNode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeException();
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlMapping)) {
			preparedStatement.setInt(1, projectNode.getDirectoryId());
			preparedStatement.setInt(2, projectNode.getId());
			preparedStatement.setInt(3, projectNode.getDirectoryId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw new UnknownNodeException();
		}
		return projectNode;
	}

	/**
	 * Delete node.
	 *
	 * @param projectNodeId the project node id
	 * @throws UnknownProjectNodeException the unknown project node exception
	 */
	public void deleteNode(int projectNodeId)
			throws UnknownProjectNodeException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",
				DBTableHelper.PROJECTNODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectNodeId);
			if (preparedStatement.executeUpdate() != 1) {
				throw new UnknownProjectNodeException();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

			throw new UnknownProjectNodeException();
		}
	}

	/**
	 * Gets the sibling node.
	 *
	 * @param nodeId the node id
	 * @return the sibling node
	 * @throws EntityException the entity exception
	 */
	public List<ProjectNode> getSiblingNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT DISTINCT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KPTID, NODE.NODESTATUS, NODE.INHERITEDTREEID, MAPPING.PDID FROM (SELECT * FROM %s AS LINK WHERE LINK.SOURCEID IN (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID)) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID AND NODE.ID<>?",
						DBTableHelper.PROJECTNODELINK,
						DBTableHelper.PROJECTNODELINK,
						DBTableHelper.PROJECTNODE,
						DBTableHelper.PROJECTNODEMAPPING);

		List<ProjectNode> nodes = new ArrayList<ProjectNode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);
			preparedStatement.setInt(2, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode node = new ProjectNode();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setStatus(result.getInt(8));
					node.setInheritedTreeId(result.getInt(9));
					node.setDirectoryId(result.getInt(10));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return nodes;
	}

	/**
	 * Gets the parent node.
	 *
	 * @param nodeId the node id
	 * @return the parent node
	 * @throws EntityException the entity exception
	 */
	public List<ProjectNode> getParentNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KPTID, NODE.NODESTATUS, NODE.INHERITEDTREEID, MAPPING.PDID FROM (SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.SOURCEID=NODE.ID AND NODE.ID=MAPPING.KPNID",
						DBTableHelper.PROJECTNODELINK,
						DBTableHelper.PROJECTNODE,
						DBTableHelper.PROJECTNODEMAPPING);

		List<ProjectNode> nodes = new ArrayList<ProjectNode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {

			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode node = new ProjectNode();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setStatus(result.getInt(8));
					node.setInheritedTreeId(result.getInt(9));
					node.setDirectoryId(result.getInt(10));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return nodes;
	}

	/**
	 * Gets the follower node.
	 *
	 * @param nodeId the node id
	 * @return the follower node
	 * @throws EntityException the entity exception
	 */
	public List<ProjectNode> getFollowerNode(int nodeId) throws EntityException {
		final String sql = String
				.format("SELECT  NODE.ID, NODE.CREATION_TIME, NODE.LAST_MODIFICATION_TIME, NODE.TITLE, NODE.WIKITITLE, NODE.DYNAMICTREEFLAG, NODE.KPTID, NODE.NODESTATUS, NODE.INHERITEDTREEID, MAPPING.PDID FROM (SELECT LINKTO.TARGETID FROM %s AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID",
						DBTableHelper.PROJECTNODELINK,
						DBTableHelper.PROJECTNODE,
						DBTableHelper.PROJECTNODEMAPPING);

		List<ProjectNode> nodes = new ArrayList<ProjectNode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode node = new ProjectNode();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setTreeId(result.getInt(7));
					node.setStatus(result.getInt(8));
					node.setInheritedTreeId(result.getInt(9));
					node.setDirectoryId(result.getInt(10));
					nodes.add(node);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return nodes;
	}

	/**
	 * Gets the root.
	 *
	 * @param treeId the tree id
	 * @return the root
	 * @throws UnknownNodeException the unknown node exception
	 * @throws EntityException the entity exception
	 */
	public ProjectNode getRoot(int treeId) throws UnknownNodeException, EntityException {
		final String sql = String
				.format("SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, PNODE.KPTID, PNODE.INHERITEDTREEID, MAPPING.PDID  FROM %s AS PNODE, %s AS MAPPING WHERE PNODE.ID NOT IN (SELECT TARGETID FROM %s) AND PNODE.DYNAMICTREEFLAG = 1 AND PNODE.ID = MAPPING.KPNID AND PNODE.KPTID=? LIMIT 1",
						DBTableHelper.PROJECTNODE, DBTableHelper.PROJECTNODEMAPPING,
						DBTableHelper.PROJECTNODELINK);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectNode node = new ProjectNode();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setStatus(result.getInt(7));
					node.setTreeId(result.getInt(8));
					node.setInheritedTreeId(result.getInt(9));
					node.setDirectoryId(result.getInt(10));
					return node;
				}
				return getMinNode(treeId);
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}

	}

	/**
	 * Gets the min node.
	 *
	 * @param treeId the tree id
	 * @return the min node
	 * @throws EntityException the entity exception
	 * @throws UnknownNodeException the unknown node exception
	 */
	private ProjectNode getMinNode(int treeId) throws EntityException,
			UnknownNodeException {
		final String sql = String
				.format("SELECT PNODE.ID, PNODE.CREATION_TIME, PNODE.LAST_MODIFICATION_TIME, PNODE.TITLE, PNODE.WIKITITLE, PNODE.DYNAMICTREEFLAG, PNODE.NODESTATUS, PNODE.KPTID, PNODE.INHERITEDTREEID, MAPPING.PDID  FROM %S AS PNODE, %s AS MAPPING WHERE PNODE.ID = (SELECT MIN(ID) FROM %s AS INNERNODE WHERE INNERNODE.DYNAMICTREEFLAG = 1 AND INNERNODE.KPTID = ?) AND PNODE.ID = MAPPING.KPNID",
						DBTableHelper.PROJECTNODE, DBTableHelper.PROJECTNODEMAPPING,
						DBTableHelper.PROJECTNODE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				if (result.wasNull()) {
				}
				while (result.next()) {
					ProjectNode node = new ProjectNode();
					node.setId(result.getInt(1));
					node.setCreationTime(DateHelper.getDate(result.getString(2)));
					node.setLastModificationTime(DateHelper.getDate(result
							.getString(3)));
					node.setTitle(result.getString(4));
					node.setWikititle(result.getString(5));
					node.setDynamicTreeFlag(result.getInt(6));
					node.setStatus(result.getInt(7));
					node.setTreeId(result.getInt(8));
					node.setInheritedTreeId(result.getInt(9));
					node.setDirectoryId(result.getInt(10));
					return node;
				}
				return null;
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			throw new UnknownNodeException();
		}
	}
}
