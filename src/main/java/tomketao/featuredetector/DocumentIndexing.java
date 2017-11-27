package tomketao.featuredetector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import tomketao.featuredetector.connection.ESConnection;
import tomketao.featuredetector.util.MapReduceConfig;

public class DocumentIndexing {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(DocumentIndexing.class);
	private static String[] headers;
	private static ESConnection conn;

	public static void main(String[] args) {
		if (args.length != 3) {
			LOGGER.error("usage: DocumentIndexing ><config-path> <server.url> <input.path>");
			return;
		}

		try {
			Configuration config = MapReduceConfig.loadConfiguration(args[0]);
			config.set("server.url", args[1]);
			config.set("input.path", args[2]);
			headers = config.get("input.header").split(
					config.get("input.delimiter"));
			conn = new ESConnection(config.get("server.url"));
			indexingDocuments(config);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			System.out.println("Config file not found: " + args[0]);
			e.printStackTrace();
			return;
		}
	}

	public static void indexingDocuments(Configuration config)
			throws IOException {
		String delimiter = config.get("input.delimiter");

		int keyColumn = Integer.parseInt(config.get("key.column"));
		String inputFileName = config.get("input.path");
		FileReader fileReader = new FileReader(inputFileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;

		while ((line = bufferedReader.readLine()) != null) {

			// read input to get recordid
			String[] fields = line.split(delimiter);
			String recordid = fields[keyColumn];

			Map<String, Object> doc = new HashMap<String, Object>();
			for (int i = 0; i < fields.length; i++) {
				if (StringUtils.isNotBlank(fields[i])) {
					doc.put(headers[i], fields[i]);
				}
			}
			LOGGER.info(line);
			conn.indexing(recordid, doc);
		}

		// Always close files.
		bufferedReader.close();
		return;
	}
}
