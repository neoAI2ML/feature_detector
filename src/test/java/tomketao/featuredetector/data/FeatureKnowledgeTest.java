package tomketao.featuredetector.data;

import java.io.IOException;
import java.util.Map;

import org.testng.Assert;

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

//	@Test
	public void saveKnowledge() {
		try {
			ScamTrainingProcess.learningProcess(knowledge, trainingSetting,
					"data/scam-training");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		knowledge.save(trainingSetting);
	}
	
//	@Test
	public void loadKnowledge() {
		knowledge.load(trainingSetting);
		
		int size_data = knowledge.size();
		
		System.out.println("Size: " + size_data);
	}
	
//	@Test
	public void featureDetecting() {
		knowledge.load(trainingSetting);
		
		int size_data = knowledge.size();
		
		System.out.println("Size: " + size_data);
		
		String dataSpam1 = "Urgent! Please call 09061213237 from a landline. £5000 cash or a 4* holiday await collection. T &Cs SAE PO Box 177 M227XY. 16+";
		
		Map<String, Float> result = knowledge.feature_probalities(dataSpam1, trainingSetting);
		for(String ft : result.keySet()) {
			System.out.println(ft + ": " + result.get(ft));
		}
		
		Assert.assertTrue(result.get("SPAM") > result.get("HAM"));
		
		System.out.println();

		String dataSpam2 = "WINNER!! As a valued network customer you have been selected to receivea £900 prize reward! To claim call 09061701461. Claim code KL341. Valid 12 hours only.";
		
		Map<String, Float> result2 = knowledge.feature_probalities(dataSpam2, trainingSetting);
		for(String ft : result2.keySet()) {
			System.out.println(ft + ": " + result2.get(ft));
		}
		
		Assert.assertTrue(result2.get("SPAM") > result2.get("HAM"));

		System.out.println();
		
		String dataSpam3 = "As per your request 'Melle Melle (Oru Minnaminunginte Nurungu Vettam)' has been set as your callertune for all Callers. Press *9 to copy your friends Callertune";
		
		Map<String, Float> result3 = knowledge.feature_probalities(dataSpam3, trainingSetting);
		for(String ft : result3.keySet()) {
			System.out.println(ft + ": " + result3.get(ft));
		}
		
		Assert.assertTrue(result3.get("SPAM") < result3.get("HAM"));
		
		System.out.println();
		
		String dataSpam4 = "So ü pay first lar... Then when is da stock comin...";
		
		Map<String, Float> result4 = knowledge.feature_probalities(dataSpam4, trainingSetting);
		for(String ft : result4.keySet()) {
			System.out.println(ft + ": " + result4.get(ft));
		}
		
		Assert.assertTrue(result4.get("SPAM") < result4.get("HAM"));
		
		System.out.println();
		
		String dataSpam5 = "I see the letter B on my car";
		
		Map<String, Float> result5 = knowledge.feature_probalities(dataSpam5, trainingSetting);
		for(String ft : result5.keySet()) {
			System.out.println(ft + ": " + result5.get(ft));
		}
		
		Assert.assertTrue(result5.get("SPAM") < result5.get("HAM"));
	}
}
