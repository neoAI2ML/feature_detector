package tomketao.featuredetector.data;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import tomketao.featuredetector.training.ScamTrainingProcess;

public class FeatureKnowledgeTest {
	private FeatureKnowledge knowledge = new FeatureKnowledge();
	private TrainingSetting trainingSetting;

	@BeforeClass
	public void beforeClass() {
		trainingSetting = TrainingSetting.loadFromFile("conf/fd-config.json");
		knowledge = new FeatureKnowledge();

	}

	@Test
	public void saveKnowledge() {
		try {
			ScamTrainingProcess.learningProcess(knowledge, trainingSetting,
					"data/scam-training");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		knowledge.save(trainingSetting);
	}
	
	@Test
	public void loadKnowledge() {
		knowledge.load(trainingSetting);
		
		int size_data = knowledge.size();
		
		System.out.println("Size: " + size_data);
	}
}
