package cdar.dal.persistence.jdbc.producer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cdar.dal.persistence.CUDHelper;
import cdar.dal.persistence.CdarDao;

public class XmlTreeDao extends CUDHelper<XmlTreeDao> implements CdarDao {
	private int id;
	private int uid;
	private int ktrid;
	private Date creationTime;
	private Date lastModificationTime;
	private String xmlString;
	
	public XmlTreeDao(int id) {
		setId(id);
	}
	
	public XmlTreeDao(int uid, int ktrid) {
		setUid(uid);
		setKtrid(ktrid);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getKtrid() {
		return ktrid;
	}

	public void setKtrid(int ktrid) {
		this.ktrid = ktrid;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	
	@Override
	public XmlTreeDao update() {
		try {
			return super.update();
		} catch (Exception ex) {
			XmlTreeDao treeXmlDao = new XmlTreeDao(-1);
			return treeXmlDao;
		}
	}

	public boolean delete() {
		return delete("KNOWLEDGETREEXML", getId());
	}

	@Override
	public XmlTreeDao create() {
		try {
			return super.create();
		} catch (Exception ex) {
			XmlTreeDao treeXmlDao = new XmlTreeDao(-1);
			return treeXmlDao;
		}
	}

	@Override
	protected XmlTreeDao createVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"INSERT INTO KNOWLEDGETREEXML (CREATION_TIME, uid, ktrid, xmlstring) VALUES (?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getUid());
		preparedStatement.setInt(3, getKtrid());
		preparedStatement.setString(4, getXmlString());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}

	@Override
	protected XmlTreeDao updateVisit(Connection connection,
			PreparedStatement preparedStatement, ResultSet generatedKeys)
			throws SQLException {
		preparedStatement = connection
				.prepareStatement(
						"UPDATE KNOWLEDGETREEXML SET LAST_MODIFICATION_TIME = ?, UID = ?, KTRID = ?, XMLSTRING = ? WHERE id = ?",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
		preparedStatement.setInt(2, getUid());
		preparedStatement.setInt(3, getKtrid());
		preparedStatement.setString(4, getXmlString());
		preparedStatement.setInt(5, getId());

		preparedStatement.executeUpdate();

		generatedKeys = preparedStatement.getGeneratedKeys();
		if (generatedKeys.next()) {
			setId(generatedKeys.getInt(1));
		}
		preparedStatement.close();
		return this;
	}
}
