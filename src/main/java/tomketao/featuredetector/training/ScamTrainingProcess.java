package tomketao.featuredetector.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import tomketao.featuredetector.data.FeatureKnowledge;
import tomketao.featuredetector.data.TrainingSetting;

public class ScamTrainingProcess {
	public static final Logger LOGGER = LoggerFactory.getLogger(ScamTrainingProcess.class);
	private static String delimiter = "\t";
	static FeatureKnowledge knowledge = new FeatureKnowledge();
	private static int seq = 0;
	private static TrainingSetting trainingSetting = new TrainingSetting();

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		if (args.length != 2) {
			LOGGER.error("usage: ScamTraningProcess <config-path> <input.path>");
			return;
		}
		
		trainingSetting = TrainingSetting.loadFromFile(args[0]);
		
		LOGGER.info(trainingSetting.getStoreUrl());

		FileReader fileReader = new FileReader(args[1]);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;

		while ((line = bufferedReader.readLine()) != null) {

			// read input to get recordid
			String[] fields = line.split(delimiter);
			if (fields.length == 2) {
				knowledge.put_feature(fields[0], fields[1], seq);
			}

			seq++;

			LOGGER.info(line);
		}

		// Always close files.
		fileReader.close();
		bufferedReader.close();
		
		// before alignment
		LOGGER.info("Knowledge Base Size before alignment: " + knowledge.size());
		
		//after alignment
		knowledge.alignment(trainingSetting);
		LOGGER.info("Knowledge Base Size after alignment: " + knowledge.size());
	}

}
