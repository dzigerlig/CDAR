package cdar.bll.wiki;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

import cdar.dal.exceptions.UsernameInvalidException;
import cdar.dal.exceptions.WikiCreateUserException;

public class WikiRegistrationManager {
	private final String URL = "http://152.96.56.36/mediawiki/api.php/?";

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
			throw new WikiCreateUserException();
		}
	}

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

	private OutputStreamWriter flushWriter(String body,
			HttpURLConnection connection) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(
				connection.getOutputStream());
		writer.write(body);
		writer.flush();
		return writer;
	}

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