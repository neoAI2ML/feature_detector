package tomketao.featuredetector.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.conf.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapReduceConfig {
	public static Configuration loadConfiguration(String configFileName) throws ParserConfigurationException, SAXException, IOException {
		Configuration conf = new Configuration();

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(configFileName));

		// normalize text representation
		doc.getDocumentElement().normalize();
		NodeList listOfProperties = doc.getElementsByTagName("property");

		for (int s = 0; s < listOfProperties.getLength(); s++) {
			Node firstNode = listOfProperties.item(s);
			if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstElement = (Element) firstNode;

				// -------
				NodeList propertyName = firstElement
						.getElementsByTagName("name");
				Element propertyNameElement = (Element) propertyName.item(0);
				NodeList textNameList = propertyNameElement.getChildNodes();
				String propertyKeyString = ((Node) textNameList.item(0)).getNodeValue()
						.trim();
//				System.out.println("name : " + propertyKeyString);

				// -------
				NodeList propertyValue = firstElement
						.getElementsByTagName("value");
				Element propertyValueElement = (Element) propertyValue.item(0);
				NodeList textValueList = propertyValueElement.getChildNodes();
				String propertyValueString = ((Node) textValueList.item(0)).getNodeValue();
//				System.out.println("value : " + propertyValueString);
				// ------
				
				conf.set(propertyKeyString, propertyValueString);
			}
		}
		
		return conf;
	}
	
	public static Properties loadProperties(String configFileName) throws ParserConfigurationException, SAXException, IOException {
		Properties conf = new Properties();

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(configFileName));

		// normalize text representation
		doc.getDocumentElement().normalize();
		NodeList listOfProperties = doc.getElementsByTagName("property");

		for (int s = 0; s < listOfProperties.getLength(); s++) {
			Node firstNode = listOfProperties.item(s);
			if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstElement = (Element) firstNode;

				// -------
				NodeList propertyName = firstElement
						.getElementsByTagName("name");
				Element propertyNameElement = (Element) propertyName.item(0);
				NodeList textNameList = propertyNameElement.getChildNodes();
				String propertyKeyString = ((Node) textNameList.item(0)).getNodeValue()
						.trim();
//				System.out.println("name : " + propertyKeyString);

				// -------
				NodeList propertyValue = firstElement
						.getElementsByTagName("value");
				Element propertyValueElement = (Element) propertyValue.item(0);
				NodeList textValueList = propertyValueElement.getChildNodes();
				String propertyValueString = ((Node) textValueList.item(0)).getNodeValue()
						.trim();
//				System.out.println("value : " + propertyValueString);
				// ------
				
				conf.setProperty(propertyKeyString, propertyValueString);
			}
		}
		
		return conf;
	}
}
