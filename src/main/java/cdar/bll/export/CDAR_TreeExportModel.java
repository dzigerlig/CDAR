package cdar.bll.export;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cdar.dal.persistence.jdbc.user.UserDao;

public class CDAR_TreeExportModel {

	public String getTreeSimpleXmlString(int treeid) {
		CDAR_TreeSimple tse = new CDAR_TreeSimple(treeid);
		try {
			final Marshaller m = JAXBContext.newInstance(CDAR_TreeSimple.class)
					.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(tse, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CDAR_TreeSimple getTreeSimple(String xmlString) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(CDAR_TreeSimple.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			StringReader reader = new StringReader(xmlString);
			CDAR_TreeSimple treeSimple = (CDAR_TreeSimple) unmarshaller.unmarshal(reader);
			return treeSimple;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static UserDao getUser(String userXml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(UserDao.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(userXml);
			UserDao user = (UserDao) unmarshaller.unmarshal(reader);
			return user;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
