package cdar.dal.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.Subnode;
import cdar.dal.DBConnection;
import cdar.dal.exceptions.UnknownNodeException;
import cdar.dal.exceptions.UnknownSubnodeException;

public class SubnodeRepository {
	public List<Subnode> getSubnodes(int nodeId) throws SQLException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, TITLE, WIKITITLE, POSITION FROM KNOWLEDGESUBNODE WHERE KNID = ?";

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setTitle(result.getString(4));
					subnode.setWikiTitle(result.getString(5));
					subnode.setKnid(nodeId);
					subnode.setPosition(result.getInt(6));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return subnodes;
	}

	public Subnode getSubnode(int subnodeId) throws UnknownSubnodeException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KNID, TITLE, WIKITITLE, POSITION FROM KNOWLEDGESUBNODE WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, subnodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setKnid(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikiTitle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					return subnode;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownSubnodeException();
		}
		throw new UnknownSubnodeException();
	}
	
	public List<Subnode> getParentSubnode(int nodeId) {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM (SELECT NODE.ID FROM(SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO WHERE ? = LINKTO.TARGETID) AS SUB,  KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.SOURCEID = NODE.ID AND NODE.ID = MAPPING.KNID) AS NODES, KNOWLEDGESUBNODE AS SUBN WHERE SUBN.KNID=NODES.ID;";

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setKnid(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikiTitle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			//e.printStackTrace();
		}
		return subnodes;
	}

	public List<Subnode> getSiblingSubnode(int nodeId) {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM( SELECT DISTINCT  NODE.ID FROM ( SELECT* FROM NODELINK AS LINK WHERE ( SELECT LINKTO.SOURCEID FROM NODELINK AS LINKTO WHERE ?=LINKTO.TARGETID)=LINK.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID AND NODE.ID<>%d) AS NODES, KNOWLEDGESUBNODE AS SUBN WHERE SUBN.KNID=NODES.ID";
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
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setKnid(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikiTitle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return subnodes;
	}
	
	public List<Subnode> getFollowerSubnode(int nodeId) {
		final String sql = "SELECT SUBN.ID, SUBN.CREATION_TIME, SUBN.LAST_MODIFICATION_TIME, SUBN.KNID, SUBN.TITLE, SUBN.WIKITITLE, SUBN.POSITION FROM ( SELECT  NODE.ID FROM( SELECT LINKTO.TARGETID FROM NODELINK AS LINKTO WHERE ?=LINKTO.SOURCEID) AS SUB, KNOWLEDGENODE AS NODE, KNOWLEDGENODEMAPPING AS MAPPING WHERE SUB.TARGETID=NODE.ID AND NODE.ID=MAPPING.KNID) AS NODES, KNOWLEDGESUBNODE AS SUBN WHERE SUBN.KNID=NODES.ID";

		List<Subnode> subnodes = new ArrayList<Subnode>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Subnode subnode = new Subnode();
					subnode.setId(result.getInt(1));
					subnode.setCreationTime(result.getDate(2));
					subnode.setLastModificationTime(result.getDate(3));
					subnode.setKnid(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikiTitle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		//	e.printStackTrace();
		}
		return subnodes;
	}

	public int getNextSubnodePosition(int nodeId) throws SQLException {
		int position = 0;

		for (Subnode subnode : getSubnodes(nodeId)) {
			if (subnode.getPosition() > position) {
				position = subnode.getPosition();
			}
		}

		return ++position;
	}

	public List<Subnode> getSubnodesByTree(int treeId) {
		final String sql = "SELECT SUBNODE.ID, SUBNODE.CREATION_TIME, SUBNODE.LAST_MODIFICATION_TIME, SUBNODE.KNID, SUBNODE.TITLE, SUBNODE.WIKITITLE, POSITION FROM KNOWLEDGESUBNODE AS SUBNODE JOIN KNOWLEDGENODE ON KNOWLEDGENODE.ID = SUBNODE.KNID WHERE KNOWLEDGENODE.KTRID = ?";

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
					subnode.setKnid(result.getInt(4));
					subnode.setTitle(result.getString(5));
					subnode.setWikiTitle(result.getString(6));
					subnode.setPosition(result.getInt(7));
					subnodes.add(subnode);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return subnodes;
	}
	
	public Subnode createSubnode(Subnode subnode) throws Exception {
		final String sql = "INSERT INTO KNOWLEDGESUBNODE (CREATION_TIME, KNID, TITLE, POSITION) VALUES (?, ?, ?, ?)";
		final String sqlUpdate = "UPDATE KNOWLEDGESUBNODE SET WIKITITLE = ? where id = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, subnode.getKnid());
			preparedStatement.setString(3, subnode.getTitle());
			preparedStatement.setInt(4, subnode.getPosition());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					subnode.setId(generatedKeys.getInt(1));
					subnode.setWikiTitle(String.format("SUBNODE_%d", subnode.getId()));
				}
			}
		} catch (Exception ex) {
			throw new UnknownNodeException();
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sqlUpdate)) {
			preparedStatement.setString(1, subnode.getWikiTitle());
			preparedStatement.setInt(2, subnode.getId());
			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			System.out.println("SUBNODE UPDATE EXCEPTION");
			ex.printStackTrace();
			throw ex;
		}
		
		return subnode;
	}
	
	public Subnode updateSubnode(Subnode subnode) throws UnknownSubnodeException {
		final String sql = "UPDATE KNOWLEDGESUBNODE SET LAST_MODIFICATION_TIME = ?, KNID = ?, TITLE = ?, POSITION = ? WHERE id = ?";
		try (Connection connection = DBConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, subnode.getKnid());
			preparedStatement.setString(3, subnode.getTitle());
			preparedStatement.setInt(4, subnode.getPosition());
			preparedStatement.setInt(5, subnode.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownSubnodeException();
		}
		return subnode;
	}
	
	public boolean deleteSubnode(int subnodeId) throws UnknownSubnodeException {
		final String sql = "DELETE FROM KNOWLEDGESUBNODE WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, subnodeId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownSubnodeException();
			}
		} catch (Exception ex) {
			throw new UnknownSubnodeException();
		}
	}
}
