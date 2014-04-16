package cdar.bll.export;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class CDAR_TreeExportModel {

	public String getTreeSimpleXmlString(int treeid) {
		CDAR_TreeSimpleExport tse = new CDAR_TreeSimpleExport(treeid);
		try {
			final Marshaller m = JAXBContext.newInstance(
					CDAR_TreeSimpleExport.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter w = new StringWriter();
			m.marshal(tse, w);
			return w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
