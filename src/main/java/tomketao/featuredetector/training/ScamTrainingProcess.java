package tomketao.featuredetector.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import tomketao.featuredetector.data.FeatureKnowledge;
import tomketao.featuredetector.data.TrainingSetting;
import tomketao.featuredetector.util.MapReduceConfig;

public class ScamTrainingProcess {
	public static final Logger LOGGER = LoggerFactory.getLogger(ScamTrainingProcess.class);
	static String dataFilename = "scam-training";
	private static String delimiter = "\t";
	static FeatureKnowledge knowledge = new FeatureKnowledge();
	private static int seq = 0;
	private static TrainingSetting trainingSetting = new TrainingSetting();

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		if (args.length != 2) {
			LOGGER.error("usage: ScamTraningProcess <config-path> <input.path>");
			return;
		}
		
		Configuration config = MapReduceConfig.loadConfiguration(args[0]);
		delimiter = config.get("input.delimiter");
		trainingSetting.setKeySize(config.getInt("key.size", 1));
		trainingSetting.setKnowledgeLimit(config.getInt("knowledge.limit", 5000));
		trainingSetting.setRareLimit(config.getInt("rare.limit", 1));
		trainingSetting.setValidSeqRange(config.getInt("sequence.range", 2000));
		trainingSetting.setMinimumImpact(config.getFloat("minimum.impact", (float) 0.01));

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
