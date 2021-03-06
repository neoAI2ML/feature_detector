package tomketao.featuredetector.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomketao.featuredetector.data.FeatureKnowledge;
import tomketao.featuredetector.data.TrainingSetting;
import tomketao.featuredetector.util.CommonUtils;

public class ScamTrainingProcess {
	public static final Logger LOGGER = LoggerFactory.getLogger(ScamTrainingProcess.class);
	static FeatureKnowledge knowledge = new FeatureKnowledge();
	private static int seq = 0;
	private static TrainingSetting trainingSetting;

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			LOGGER.error("usage: ScamTraningProcess <config-path> <input.path>");
			return;
		}
		
		trainingSetting = TrainingSetting.loadFromFile(args[0]);
		
		LOGGER.info(trainingSetting.getStoreMetaDataUrl());
		LOGGER.info(trainingSetting.getStoreFeatureDataUrl());

		knowledge.load(trainingSetting);
		seq = knowledge.getCurrentSequence() + 1;
		
		learningProcess(knowledge, trainingSetting, args[1]);
		
		knowledge.save(trainingSetting);
	}

	public static void learningProcess(FeatureKnowledge knowledge, TrainingSetting trainingSetting, String inputFile) throws IOException {
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;

		while ((line = bufferedReader.readLine()) != null) {

			// read input to get recordid
			//CommonUtils.decodeISO_8859_1AndUtf_8Format
			String[] fields = CommonUtils.normalize(line).split(trainingSetting.getInputDelimiter());
			if (fields.length == 2) {
				knowledge.put_feature(fields[0].toUpperCase(), fields[1], seq, trainingSetting);
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
