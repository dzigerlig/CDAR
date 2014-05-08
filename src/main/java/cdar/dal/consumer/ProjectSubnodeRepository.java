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

import cdar.bll.entity.Subnode;
import cdar.bll.entity.consumer.ProjectSubnode;
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownProjectNodeException;
import cdar.dal.exceptions.UnknownProjectNodeLinkException;
import cdar.dal.exceptions.UnknownProjectSubnodeException;

public class ProjectSubnodeRepository {
	public List<ProjectSubnode> getProjectSubnodes(int kpnid) throws UnknownProjectNodeLinkException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION, SUBNODESTATUS FROM KNOWLEDGEPROJECTSUBNODE WHERE KPNID = ?";

		List<ProjectSubnode> projectsubnodes = new ArrayList<ProjectSubnode>();

		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection
				.prepareStatement(sql)){
			preparedStatement.setInt(1, kpnid);
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
					projectSubnode.setNodeId(kpnid);
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
	
	public ProjectSubnode getProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, TITLE, WIKITITLE, POSITION, SUBNODESTATUS FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = ?";


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
	
	public ProjectSubnode createProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeException, CreationException {
		final String sql = "INSERT INTO KNOWLEDGEPROJECTSUBNODE (CREATION_TIME, KPNID, TITLE, POSITION, SUBNODESTATUS) VALUES (?, ?, ?, ?, ?)";
		final String sqlUpdate = "UPDATE KNOWLEDGEPROJECTSUBNODE SET WIKITITLE = ? where id = ?";
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, projectSubnode.getNodeId());
			preparedStatement.setString(3, projectSubnode.getTitle());
			preparedStatement.setInt(4, projectSubnode.getPosition());
			preparedStatement.setInt(5, 0);
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
	
	public ProjectSubnode updateProjectSubnode(ProjectSubnode projectSubnode) throws UnknownProjectNodeLinkException {
		final String sql = "UPDATE KNOWLEDGEPROJECTSUBNODE SET LAST_MODIFICATION_TIME = ?, KPNID = ?, TITLE = ?, WIKITITLE = ?, POSITION = ?, SUBNODESTATUS = ? WHERE id = ?";
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
	
	public void deleteProjectSubnode(int projectSubnodeId) throws UnknownProjectSubnodeException {
		final String sql = "DELETE FROM KNOWLEDGEPROJECTSUBNODE WHERE ID = ?";
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
	
	public List<ProjectSubnode> getParentSubnode(int nodeId) throws EntityException {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS FROM (SELECT NODE.ID FROM(SELECT LINKTO.SOURCEID FROM KNOWLEDGEPROJECTNODELINK AS LINKTO WHERE ? = LINKTO.TARGETID) AS SUB,  KNOWLEDGEPROJECTNODE AS NODE, KNOWLEDGEPROJECTNODEMAPPING AS MAPPING WHERE SUB.SOURCEID = NODE.ID AND NODE.ID = MAPPING.KPNID) AS NODES, KNOWLEDGEPROJECTSUBNODE AS SUBN WHERE SUBN.KPNID=NODES.ID;";

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

	public List<ProjectSubnode> getSiblingSubnode(int nodeId) throws EntityException {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS FROM( SELECT DISTINCT  NODE.ID FROM ( SELECT* FROM KNOWLEDGEPROJECTNODELINK AS LINK WHERE ( SELECT LINKTO.SOURCEID FROM KNOWLEDGEPROJECTNODELINK AS LINKTO WHERE ?=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, KNOWLEDGEPROJECTNODE AS NODE, KNOWLEDGEPROJECTNODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID AND NODE.ID<>%d) AS NODES, KNOWLEDGEPROJECTSUBNODE AS SUBN WHERE SUBN.KPNID=NODES.ID";
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
	
	public List<ProjectSubnode> getFollowerSubnode(int nodeId) throws EntityException {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KPNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION, SUBN.SUBNODESTATUS FROM ( SELECT  NODE.ID FROM( SELECT LINKTO.TARGETID FROM KNOWLEDGEPROJECTNODELINK AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, KNOWLEDGEPROJECTNODE AS NODE, KNOWLEDGEPROJECTNODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KPNID) AS NODES, KNOWLEDGEPROJECTSUBNODE AS SUBN WHERE SUBN.KPNID=NODES.ID";

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
