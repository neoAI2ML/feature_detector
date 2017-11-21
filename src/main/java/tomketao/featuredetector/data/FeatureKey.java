package tomketao.featuredetector.data;

import java.util.HashMap;
import java.util.Map;

import tomketao.featuredetector.config.FieldParams;
import tomketao.featuredetector.util.StaticConstants;

public class FeatureKey extends FieldParams {
	private static final long serialVersionUID = -1661134457157895278L;

	private Map<String, Object> featureCounts;

	public FeatureKey(String key, int seqNo) {
		featureCounts = new HashMap<String, Object>();
		String[] words = key.split(StaticConstants.SPACE);
		featureCounts.put(StaticConstants.FD_KEY, key);
		featureCounts.put(StaticConstants.FD_SEQUENCE, seqNo);
		featureCounts.put(StaticConstants.FD_KEY_HASHCODE, key.hashCode());
		featureCounts.put(StaticConstants.FD_SIZE_IN_WORDS, words.length);
	}
}
