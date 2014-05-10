package cdar.dal.producer;

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
import cdar.dal.DBConnection;
import cdar.dal.DBTableHelper;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;
import cdar.dal.exceptions.UnknownTreeException;

public class SubnodeRepository {
	public List<Subnode> getSubnodes(int nodeId) throws EntityException, UnknownNodeException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION FROM %s WHERE KNID = ?",DBTableHelper.SUBNODE);

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setTitle(result.getString(4));
					subnode.setWikititle(result.getString(5));
					subnode.setNodeId(nodeId);
					subnode.setPosition(result.getInt(6));
					subnodes.add(subnode);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownNodeException();
		}
		return subnodes;
	}

	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException, EntityException {
		final String sql = String.format("SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KNID, TITLE, WIKITITLE, POSITION FROM %s WHERE ID = ?",DBTableHelper.SUBNODE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, subnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					return subnode;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownSubnodeException();
		}
		throw new UnknownSubnodeException();
	}
	
	public List<Subnode> getParentSubnode(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM (SELECT NODE.ID FROM(SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ? = LINKTO.TARGETID) AS SUB,  %s AS NODE, %s AS MAPPING WHERE SUB.SOURCEID = NODE.ID AND NODE.ID = MAPPING.KNID) AS NODES, %s AS SUBN WHERE SUBN.KNID=NODES.ID;",DBTableHelper.NODELINK, DBTableHelper.NODE,DBTableHelper.NODEMAPPING,DBTableHelper.SUBNODE);

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
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

	public List<Subnode> getSiblingSubnode(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM( SELECT DISTINCT  NODE.ID FROM ( SELECT* FROM %s AS LINK WHERE ( SELECT LINKTO.SOURCEID FROM %s AS LINKTO WHERE ?=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>%d) AS NODES %s AS SUBN WHERE SUBN.KNID=NODES.ID",DBTableHelper.NODELINK,DBTableHelper.NODELINK,DBTableHelper.NODE,DBTableHelper.NODEMAPPING,DBTableHelper.SUBNODE);
		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);
			preparedStatement.setInt(2, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
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
	
	public List<Subnode> getFollowerSubnode(int nodeId) throws EntityException {
		final String sql = String.format("SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM ( SELECT  NODE.ID FROM( SELECT LINKTO.TARGETID FROM %s AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, %s AS NODE, %s AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID) AS NODES, %s AS SUBN WHERE SUBN.KNID=NODES.ID",DBTableHelper.NODELINK,DBTableHelper.NODE,DBTableHelper.NODEMAPPING,DBTableHelper.SUBNODE);

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(DateHelper.getDate(result.getString(2)));
					subnode.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
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

	public int getNextSubnodePosition(int nodeId) throws EntityException, UnknownNodeException {
		int position = 0;

		for (Subnode subnode : getSubnodes(nodeId)) {
			if (subnode.getPosition() > position) {
				position = subnode.getPosition();
			}
		}

		return ++position;
	}

	public List<Subnode> getSubnodesByTree(int treeId) throws UnknownTreeException {
		final String sql = String.format("SELECT SUBNODE.ID, SUBNODE.CREATION_TIME, SUBNODE.LAST_MODIFICATION_TIME, SUBNODE.KNID, SUBNODE.TITLE, SUBNODE.WIKITITLE, POSITION FROM %s AS SUBNODE JOIN %s AS NODE ON NODE.ID = SUBNODE.KNID WHERE NODE.KTRID = ?",DBTableHelper.SUBNODE,DBTableHelper.NODE);

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, treeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setNodeId(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikititle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException e) {
			throw new UnknownTreeException();
		}
		return subnodes;
	}
	
	public Subnode createSubnode(Subnode subnode) throws UnknownNodeException, CreationException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, KNID, TITLE, POSITION) VALUES (?, ?, ?, ?)",DBTableHelper.SUBNODE);
		final String sqlUpdate = String.format("UPDATE %s SET WIKITITLE = ? where id = ?",DBTableHelper.SUBNODE);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, subnode.getNodeId());
			preparedStatement.setString(3, subnode.getTitle());
			preparedStatement.setInt(4, subnode.getPosition());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					subnode.setId(generatedKeys.getInt(1));
					subnode.setWikititle(String.format("SUBNODE_%d", subnode.getId()));
				}
			}
		} catch (Exception ex) {
			throw new UnknownNodeException();
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlUpdate)) {
			preparedStatement.setString(1, subnode.getWikititle());
			preparedStatement.setInt(2, subnode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new CreationException();
		}
		
		return subnode;
	}
	
	public Subnode updateSubnode(Subnode subnode) throws UnknownSubnodeException {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, KNID = ?, TITLE = ?, POSITION = ? WHERE id = ?",DBTableHelper.SUBNODE);
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, subnode.getNodeId());
			preparedStatement.setString(3, subnode.getTitle());
			preparedStatement.setInt(4, subnode.getPosition());
			preparedStatement.setInt(5, subnode.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownSubnodeException();
		}
		return subnode;
	}
	
	public void deleteSubnode(int subnodeId) throws UnknownSubnodeException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.SUBNODE);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, subnodeId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownSubnodeException();
			}
		} catch (Exception ex) {
			throw new UnknownSubnodeException();
		}
	}
}
