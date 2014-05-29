package ch.cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.cdar.bll.entity.consumer.ProjectSubnode;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownProjectNodeException;
import ch.cdar.dal.exceptions.UnknownProjectNodeLinkException;
import ch.cdar.dal.exceptions.UnknownProjectSubnodeException;
import ch.cdar.dal.helpers.DBConnection;
import ch.cdar.dal.helpers.DBTableHelper;
import ch.cdar.dal.helpers.DateHelper;

/**
 * The Class ProjectSubnodeRepository.
 */
public class ProjectSubnodeRepository {
	/**
	 * Gets the subnodes.
	 *
	 * @param nodeId the node id
	 * @return the subnodes
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 * @throws EntityException the entity exception
	 */
	public List<ProjectSubnode> getSubnodes(int nodeId) throws UnknownProjectNodeLinkException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION, SUBNODESTATUS, INHERITEDTREEID FROM %s WHERE KPNID = ?", DBTableHelper.PROJECTSUBNODE);

		List<ProjectSubnode> projectsubnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)){
			preparedStatement.setInt(1, nodeId);
			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode projectSubnode = new ProjectSubnode();
					projectSubnode.setId(result.getInt(1));
					projectSubnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectSubnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectSubnode.setTitle(result.getString(4));
					projectSubnode.setWikititle(result.getString(5));
					projectSubnode.setPosition(result.getInt(6));
					projectSubnode.setStatus(result.getInt(7));
					projectSubnode.setInheritedTreeId(result.getInt(8));
					projectSubnode.setNodeId(nodeId);
					projectsubnodes.add(projectSubnode);
				}
			} catch (ParseException ex) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectsubnodes;
	}
	
	/**
	 * Gets the subnode.
	 *
	 * @param projectSubnodeId the project subnode id
	 * @return the subnode
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 * @throws EntityException the entity exception
	 */
	public ProjectSubnode getSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, TITLE, WIKITITLE, POSITION, SUBNODESTATUS, INHERITEDTREEID FROM %s WHERE ID = ?",DBTableHelper.PROJECTSUBNODE);


		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)) {
			preparedStatement.setInt(1, projectSubnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode projectSubnode = new ProjectSubnode();
					projectSubnode.setId(result.getInt(1));
					projectSubnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					projectSubnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					projectSubnode.setTitle(result.getString(5));
					projectSubnode.setWikititle(result.getString(6));
					projectSubnode.setNodeId(result.getInt(4));
					projectSubnode.setPosition(result.getInt(7));
					projectSubnode.setStatus(result.getInt(8));
					projectSubnode.setInheritedTreeId(result.getInt(9));
					return projectSubnode;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownProjectSubnodeException();
		}
		throw new UnknownProjectSubnodeException();
	}
	
	/**
	 * Creates the subnode.
	 *
	 * @param projectSubnode the project subnode
	 * @return the project subnode
	 * @throws UnknownProjectNodeException the unknown project node exception
	 * @throws CreationException the creation exception
	 */
	public ProjectSubnode createSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeException, CreationException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, KPNID, TITLE, POSITION, SUBNODESTATUS, INHERITEDTREEID) VALUES (?, ?, ?, ?, ?, ?)",DBTableHelper.PROJECTSUBNODE);
		final String sqlUpdate = String.format("UPDATE %s SET WIKITITLE = ? where id = ?",DBTableHelper.PROJECTSUBNODE);
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, projectSubnode.getNodeId());
			preparedStatement.setString(3, projectSubnode.getTitle());
			preparedStatement.setInt(4, projectSubnode.getPosition());
			preparedStatement.setInt(5, 0);
			preparedStatement.setInt(6, projectSubnode.getInheritedTreeId());
			preparedStatement.executeUpdate();
			
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projectSubnode.setId(generatedKeys.getInt(1));
					if (projectSubnode.getWikititle()==null || projectSubnode.getWikititle().isEmpty()) {
						projectSubnode.setWikititle(String.format("PROJECTSUBNODE_%d", projectSubnode.getId()));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new UnknownProjectNodeException();
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlUpdate)) {
			preparedStatement.setString(1, projectSubnode.getWikititle());
			preparedStatement.setInt(2, projectSubnode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationException();
		}
		
		return projectSubnode;
	}
	
	/**
	 * Update subnode.
	 *
	 * @param projectSubnode the project subnode
	 * @return the project subnode
	 * @throws UnknownProjectNodeLinkException the unknown project node link exception
	 */
	public ProjectSubnode updateSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, KPNID = ?, TITLE = ?, WIKITITLE = ?, POSITION = ?, SUBNODESTATUS = ? WHERE id = ?",DBTableHelper.PROJECTSUBNODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, projectSubnode.getNodeId());
			preparedStatement.setString(3, projectSubnode.getTitle());
			preparedStatement.setString(4, projectSubnode.getWikititle());
			preparedStatement.setInt(5, projectSubnode.getPosition());
			preparedStatement.setInt(6, projectSubnode.getStatus());
			preparedStatement.setInt(7, projectSubnode.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownProjectNodeLinkException();
		}
		return projectSubnode;
	}
	
	/**
	 * Delete subnode.
	 *
	 * @param projectSubnodeId the project subnode id
	 * @throws UnknownProjectSubnodeException the unknown project subnode exception
	 */
	public void deleteSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.PROJECTSUBNODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, projectSubnodeId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownProjectSubnodeException();
			}
		} catch (Exception ex) {
			throw new UnknownProjectSubnodeException();
		}
	}
	
	/**
	 * Gets the parent subnodes.
	 *
	 * @param nodeId the node id
	 * @return the parent subnodes
	 * @throws EntityException the entity exception
	 */
	public List<ProjectSubnode> getParentSubnodes(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS, SUBN.INHERITEDTREEID FROM (SELECT NODE.ID FROM(SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ? = LINKTO.TARGETID) AS SUB,  %s AS NODE, %s AS MAPPING WHERE SUB.SOURCEID = NODE.ID AND NODE.ID = MAPPING.KPNID) AS NODES, %s AS SUBN WHERE SUBN.KPNID=NODES.ID;",DBTableHelper.PROJECTNODELINK,DBTableHelper.PROJECTNODE,DBTableHelper.PROJECTNODEMAPPING,DBTableHelper.PROJECTSUBNODE);

		List<ProjectSubnode> subnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode subnode = new ProjectSubnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnode.setStatus(result.getInt(8));
					subnode.setInheritedTreeId(result.getInt(9));
					subnodes.add(subnode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return subnodes;
	}

	/**
	 * Gets the sibling subnodes.
	 *
	 * @param nodeId the node id
	 * @return the sibling subnodes
	 * @throws EntityException the entity exception
	 */
	public List<ProjectSubnode> getSiblingSubnodes(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS, SUBN.INHERITEDTREEID FROM( SELECT DISTINCT  NODE.ID FROM ( SELECT* FROM %s AS LINK WHERE LINK.SOURCEID IN ( SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID)) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID AND NODE.ID<>?) AS NODES, %s AS SUBN WHERE SUBN.KPNID=NODES.ID", DBTableHelper.PROJECTNODELINK, DBTableHelper.PROJECTNODELINK, DBTableHelper.PROJECTNODE,DBTableHelper.PROJECTNODEMAPPING,DBTableHelper.PROJECTSUBNODE);
		List<ProjectSubnode> subnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);
			preparedStatement.setInt(2, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode subnode = new ProjectSubnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnode.setStatus(result.getInt(8));
					subnode.setInheritedTreeId(result.getInt(9));
					subnodes.add(subnode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return subnodes;
	}
	
	/**
	 * Gets the follower subnodes.
	 *
	 * @param nodeId the node id
	 * @return the follower subnodes
	 * @throws EntityException the entity exception
	 */
	public List<ProjectSubnode> getFollowerSubnodes(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS, SUBN.INHERITEDTREEID FROM ( SELECT  NODE.ID FROM( SELECT LINKTO.TARGETID FROM %s AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID) AS NODES, %s AS SUBN WHERE SUBN.KPNID=NODES.ID",DBTableHelper.PROJECTNODELINK,DBTableHelper.PROJECTNODE,DBTableHelper.PROJECTNODEMAPPING,DBTableHelper.PROJECTSUBNODE);

		List<ProjectSubnode> subnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					ProjectSubnode subnode = new ProjectSubnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnode.setStatus(result.getInt(8));
					subnode.setInheritedTreeId(result.getInt(9));
					subnodes.add(subnode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return subnodes;
	}
}
