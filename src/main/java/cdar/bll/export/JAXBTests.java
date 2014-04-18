package cdar.bll.export;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cdar.dal.persistence.jdbc.producer.XmlTreeDao;
import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class JAXBTests {

	public static void main(String[] args) {
		
		CDAR_TreeExportModel tem = new CDAR_TreeExportModel();
		final String xmlString = tem.getTreeSimpleXmlString(1);
		XmlTreeDao xmlTreeDao = new XmlTreeDao(1, 1);
		xmlTreeDao.setXmlString(xmlString);
		xmlTreeDao.create();
		 
//		UserDaoController udc = new UserDaoController();
//
//		UserDao user = udc.getUserByName("root");
//
//		String marshalledUser = createXmlString(user);
//
//		System.out.println(marshalledUser);
//
//		System.out.println("CDAR:");
//		CDAR_TreeSimpleExport tse = new CDAR_TreeSimpleExport(1);
//		System.out.println("Tree: " + tse.getTree().getTitle());
//		System.out.println("Templates: " + tse.getTemplates().size());
//		System.out.println("Nodes: " + tse.getNodes().size());
//		System.out.println("Subnodes: " + tse.getSubnodes().size());
//		System.out.println("Links: " + tse.getLinks().size());
//		System.out.println("Directories: " + tse.getDirectories().size());
//
//		System.out.println("XML:");
//
//		String marshalledTree = createXmlString(tse);
//		System.out.println(marshalledTree);
//		
//		UserDao unmarshalledUser = getUser(marshalledUser);
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

	private static String createXmlString(CDAR_TreeSimple tse) {
		try {
			final Marshaller m = JAXBContext.newInstance(
					CDAR_TreeSimple.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(tse, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String createXmlString(UserDao user) {
		try {
			final Marshaller m = JAXBContext.newInstance(UserDao.class)
					.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(user, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
