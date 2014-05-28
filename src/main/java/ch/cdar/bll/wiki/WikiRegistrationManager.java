package ch.cdar.bll.wiki;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ch.cdar.dal.exceptions.WikiCreateUserException;
import ch.cdar.dal.helpers.PropertyHelper;

/**
 * The Class WikiRegistrationManager.
 */
public class WikiRegistrationManager {
	
	/** The url. */
	private String URL = null;
	
	/**
	 * Instantiates a new wiki registration manager.
	 */
	public WikiRegistrationManager() {
		PropertyHelper propertyHelper = new PropertyHelper();
		URL = String.format("http://%s/api.php/?", propertyHelper.getProperty("MEDIAWIKI_CONNECTION"));
	}

	/**
	 * Creates the user.
	 *
	 * @param username the username
	 * @param password the password
	 * @return true, if successful
	 * @throws WikiCreateUserException the wiki create user exception
	 */
	public boolean createUser(String username, String password)
			throws WikiCreateUserException {
		try {
			String body = "format=" + URLEncoder.encode("xml", "UTF-8") + "&"
					+ "action=" + URLEncoder.encode("createaccount", "UTF-8") + "&"
					+ "name=" + URLEncoder.encode(username, "UTF-8") + "&"
					+ "password=" + URLEncoder.encode(password, "UTF-8");
	
			HttpURLConnection connection = establishConnection(body);
	
			OutputStreamWriter writer = flushWriter(body, connection);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream stream = connection.getInputStream();
			String cookie = connection.getHeaderField("Set-Cookie");
	
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList layerConfigList = doc.getElementsByTagName("createaccount");
			Node node = layerConfigList.item(0);
	
			Element e = (Element) node;
			String token = e.getAttribute("token");
			stream.close();
			writer.close();
			connection.disconnect();
			
			return userRequest(username, password, token, cookie);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WikiCreateUserException();
		}
	}

	/**
	 * User request.
	 *
	 * @param username the username
	 * @param password the password
	 * @param token the token
	 * @param cookie the cookie
	 * @return true, if successful
	 * @throws WikiCreateUserException the wiki create user exception
	 * @throws MalformedURLException the malformed url exception
	 * @throws ProtocolException the protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 */
	private boolean userRequest(String username, String password, String token,
			String cookie) throws WikiCreateUserException, MalformedURLException, ProtocolException, IOException, ParserConfigurationException, SAXException {
		String body = "format=" + URLEncoder.encode("xml", "UTF-8") + "&"
				+ "action=" + URLEncoder.encode("createaccount", "UTF-8") + "&"
				+ "name=" + URLEncoder.encode(username, "UTF-8") + "&"
				+ "password=" + URLEncoder.encode(password, "UTF-8") + "&"
				+ "token=" + URLEncoder.encode(token, "UTF-8");

		HttpURLConnection connection = establishConnection(body);
		connection.setRequestProperty("Cookie", cookie);
		OutputStreamWriter writer = flushWriter(body, connection);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream stream = connection.getInputStream();
		Document doc = db.parse(stream);
		doc.getDocumentElement().normalize();
		NodeList layerConfigList = doc.getElementsByTagName("createaccount");
		Node node = layerConfigList.item(0);
		Element e = (Element) node;
		String result = null;

		try {
			result = e.getAttribute("result");
		} catch (NullPointerException ex) {
			try {
				layerConfigList = doc.getElementsByTagName("error");
				node = layerConfigList.item(0);
				e = (Element) node;
				result = e.getAttribute("info");
			} catch (NullPointerException innerEx) {

			}
		}

		stream.close();
		writer.close();
		connection.disconnect();
		if (result.equals("success")) {
			return true; 
		} else {
			System.out.println("exception");
			throw new WikiCreateUserException();
		}
	}

	/**
	 * Flush writer.
	 *
	 * @param body the body
	 * @param connection the connection
	 * @return the output stream writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private OutputStreamWriter flushWriter(String body,
			HttpURLConnection connection) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				connection.getOutputStream());
		writer.write(body);
		writer.flush();
		return writer;
	}

	/**
	 * Establish connection.
	 *
	 * @param body the body
	 * @return the http url connection
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ProtocolException the protocol exception
	 */
	private HttpURLConnection establishConnection(String body)
			throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length",
				String.valueOf(body.length()));
		return connection;
	}
}
