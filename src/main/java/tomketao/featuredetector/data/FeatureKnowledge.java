package tomketao.featuredetector.data;

import java.util.HashMap;
import java.util.Map;

import tomketao.featuredetector.util.StaticConstants;
import org.apache.commons.lang3.StringUtils;

public class FeatureKnowledge extends HashMap<Integer, FeatureKey> {
	private static final long serialVersionUID = -6712633715281112680L;

	public boolean put_feature(String feature, String featureData, int sequence) {
		String featureDataNormalized = StringUtils.normalizeSpace(featureData);
		String[] keyWordList = featureDataNormalized.split(StaticConstants.SPACE);
		boolean addKeyFlag = false;

		for (int i = 0; i < keyWordList.length; i++) {
			String keyone = keyWordList[i];
			addKeyFlag = put_feature_key(keyone, feature, sequence, 1);

			if (i + 1 < keyWordList.length) {
				String keytwo = keyone + StaticConstants.SPACE + keyWordList[i + 1];
				addKeyFlag = put_feature_key(keytwo, feature, sequence, 2);

				if (i + 2 < keyWordList.length) {
					String keythree = keytwo + StaticConstants.SPACE + keyWordList[i + 2];
					addKeyFlag = put_feature_key(keythree, feature, sequence, 3);

					if (i + 3 < keyWordList.length) {
						String keyfour = keythree + StaticConstants.SPACE + keyWordList[i + 3];
						addKeyFlag = put_feature_key(keyfour, feature, sequence, 4);
					}
				}
			}
		}

		return addKeyFlag;
	}

	private boolean put_feature_key(String key, String feature, int sequence, int sizeInWords) {
		int hashCode = key.hashCode();
		boolean newKeyFlag = true;
		FeatureKey ft_key = this.get(hashCode);
		if (ft_key == null) {
			ft_key = new FeatureKey(hashCode, key, sequence, sizeInWords);
			this.put(hashCode, ft_key);
			Map<String, Integer> featureCounts = ft_key.getFeatureCounts();
			featureCounts.put(feature, 1);
		} else {
			newKeyFlag = false;
			Map<String, Integer> featureCounts = ft_key.getFeatureCounts();
			if (featureCounts.get(feature) != null) {
				featureCounts.put(feature, featureCounts.get(feature) + 1);
			} else {
				featureCounts.put(feature, 1);
			}
		}

		return newKeyFlag;
	}
}
