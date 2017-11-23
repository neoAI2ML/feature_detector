package tomketao.featuredetector.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomketao.featuredetector.data.FeatureKnowledge;

public class ScamTraningProcess {
	public static final Logger LOGGER = LoggerFactory.getLogger(ScamTraningProcess.class);
	static String dataFilename = "scam-training";
	static String delimiter = "\t";
	static FeatureKnowledge knowledge = new FeatureKnowledge();
	private static int seq = 0;

	public static void main(String[] args) throws IOException {

		InputStreamReader fileReader = new InputStreamReader(
				ScamTraningProcess.class.getClassLoader().getResourceAsStream(dataFilename));
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
		bufferedReader.close();
	}

}
