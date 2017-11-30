package tomketao.featuredetector.data;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import tomketao.featuredetector.training.ScamTrainingProcess;

public class FeatureKnowledgeTest {
	FeatureKnowledge knowledge = new FeatureKnowledge();
	TrainingSetting trainingSetting;

	@BeforeClass
	public void beforeClass() {
		trainingSetting = TrainingSetting.loadFromFile("conf/fd-config.json");
		knowledge = new FeatureKnowledge();
		try {
			ScamTrainingProcess.learningProcess(knowledge, trainingSetting,
					"data/scam-training");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void alignment() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getCurrentFeatureCount() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void getCurrentSequence() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void maximumDifference() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void put_feature() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void put_feature_key() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void remove_key_wo_impact() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void remove_rare() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void saveKnowledge() {
		knowledge.save(trainingSetting);
	}

	@Test
	public void setCurrentFeatureCount() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void setCurrentSequence() {
		throw new RuntimeException("Test not implemented");
	}

	@Test
	public void wordNormalizer() {
		throw new RuntimeException("Test not implemented");
	}
}
