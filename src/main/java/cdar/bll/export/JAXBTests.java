package cdar.bll.export;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import cdar.dal.persistence.jdbc.user.UserDao;
import cdar.dal.persistence.jdbc.user.UserDaoController;

public class JAXBTests {

	public static void main(String[] args) {
		UserDaoController udc = new UserDaoController();
		
		UserDao user = udc.getUserByName("root");
		
		String marshalledUser = createXmlString(user);
		
		System.out.println(marshalledUser);
	}

	private static String createXmlString(UserDao user) {
		try {
			final Marshaller m = JAXBContext.newInstance(UserDao.class).createMarshaller();
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
