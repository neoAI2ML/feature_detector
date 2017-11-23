package tomketao.featuredetector.data;

import java.util.HashMap;
import java.util.Map;
import tomketao.featuredetector.util.StaticConstants;

public class FeatureKey extends FeatureDetectObject {
	private static final long serialVersionUID = 6569937637133679136L;
	
	private String keyString;
	private Map<String, Integer> featureCounts;

	public FeatureKey(int hashCode, String key, int seqNo, int wordCount) {
		setKeyString(key);
		featureCounts = new HashMap<String, Integer>();
		featureCounts.put(StaticConstants.FD_SEQUENCE, seqNo);
		featureCounts.put(StaticConstants.FD_KEY_HASHCODE, hashCode);
		featureCounts.put(StaticConstants.FD_SIZE_IN_WORDS, wordCount);
	}

	public Map<String, Integer> getFeatureCounts() {
		return featureCounts;
	}

	public void setFeatureCounts(Map<String, Integer> featureCounts) {
		this.featureCounts = featureCounts;
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}
}
