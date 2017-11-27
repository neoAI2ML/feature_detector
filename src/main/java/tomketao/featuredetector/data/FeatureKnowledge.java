package tomketao.featuredetector.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tomketao.featuredetector.util.StaticConstants;

import org.apache.commons.lang3.StringUtils;

public class FeatureKnowledge extends HashMap<Integer, FeatureKey> {
	private static final long serialVersionUID = -6712633715281112680L;
	private int currentSequence;
	private Map<String, Integer> currentFeatureCount = new HashMap<String, Integer>();

	public int getCurrentSequence() {
		return currentSequence;
	}

	public void setCurrentSequence(int currentSequence) {
		this.currentSequence = currentSequence;
	}

	public Map<String, Integer> getCurrentFeatureCount() {
		return currentFeatureCount;
	}

	public void setCurrentFeatureCount(Map<String, Integer> currentFeatureCount) {
		this.currentFeatureCount = currentFeatureCount;
	}
	
	public boolean put_feature(String feature, String featureData, int sequence) {
		// update knowledge base global variables
		setCurrentSequence(sequence);
		Integer featureCount = getCurrentFeatureCount().get(feature);
		if (featureCount == null) {
			getCurrentFeatureCount().put(feature, 1);
		} else {
			getCurrentFeatureCount().put(feature, featureCount + 1);
		}

		// update knowledge base feature keys
		String featureDataNormalized = StringUtils.normalizeSpace(featureData);
		String[] keyWordList = featureDataNormalized
				.split(StaticConstants.SPACE);
		boolean addKeyFlag = false;

		for (int i = 0; i < keyWordList.length; i++) {
			String keyone = wordNormalizer(keyWordList[i]);
			addKeyFlag = put_feature_key(keyone, feature, sequence, 1);

			if (i + 1 < keyWordList.length) {
				String keytwo = keyone + StaticConstants.SPACE
						+ wordNormalizer(keyWordList[i + 1]);
				addKeyFlag = put_feature_key(keytwo, feature, sequence, 2);

				if (i + 2 < keyWordList.length) {
					String keythree = keytwo + StaticConstants.SPACE
							+ wordNormalizer(keyWordList[i + 2]);
					addKeyFlag = put_feature_key(keythree, feature, sequence, 3);

					if (i + 3 < keyWordList.length) {
						String keyfour = keythree + StaticConstants.SPACE
								+ wordNormalizer(keyWordList[i + 3]);
						addKeyFlag = put_feature_key(keyfour, feature,
								sequence, 4);
					}
				}
			}
		}

		return addKeyFlag;
	}

	private boolean put_feature_key(String key, String feature, int sequence,
			int sizeInWords) {
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
			ft_key.setUpdateSeqNo(sequence);
			if (featureCounts.get(feature) != null) {
				featureCounts.put(feature, featureCounts.get(feature) + 1);
			} else {
				featureCounts.put(feature, 1);
			}
		}

		return newKeyFlag;
	}

	private String wordNormalizer(String word) {

		return word;
	}

	public void alignment(TrainingSetting trainingSetting) {
		remove_rare(trainingSetting);
		remove_key_wo_impact(trainingSetting);
	}

	private void remove_rare(TrainingSetting trainingSetting) {
		List<Integer> rareList = new ArrayList<Integer>();
		for (Integer item : this.keySet()) {
			Integer updateSeq = this.get(item).getUpdateSeqNo();
			if (updateSeq + trainingSetting.getValidSeqRange() < getCurrentSequence()) {
				if (this.get(item).getSumOfFTCounts() < trainingSetting.getRareLimit()) {
					rareList.add(item);
				}
			}
		}

		for (Integer rareItem : rareList) {
			this.remove(rareItem);
		}
	}

	private void remove_key_wo_impact(TrainingSetting trainingSetting) {
		List<Integer> listWOImpact = new ArrayList<Integer>();
		for(Integer keyItem : this.keySet()) {
			List<Float> probList = new ArrayList<Float>();
			for(String ft : getCurrentFeatureCount().keySet()) {
				probList.add(keyFeatureProbality(ft, this.get(keyItem).getFeatureCounts(), getCurrentFeatureCount()));
			}
			
			if(maximumDifference(probList) < trainingSetting.getMinimumImpact()) {
				listWOImpact.add(keyItem);
			}
		}
		
		for(Integer rmKeyitem : listWOImpact) {
			this.remove(rmKeyitem);
		}
	}
	
	private float maximumDifference(List<Float> dList) {
		Collections.sort(dList);
		return dList.get(dList.size() - 1) - dList.get(0);
	}
	
	public float keyFeatureProbality(String feature, Map<String, Integer> keyFeatureCount, Map<String, Integer> globalFeatureCount) {
		float divisor = 0;
		float dividend = 0;
		float averageGlobalFTCount = FeatureKey.getSumOfFTCounts(globalFeatureCount) / globalFeatureCount.size();
		for(String ft : globalFeatureCount.keySet()) {
			Integer cur = keyFeatureCount.get(ft);
			float current = cur == null ? 0 : cur;
			for(String ft_again : globalFeatureCount.keySet()) {
				if(!StringUtils.equals(ft, ft_again)) {
					current = current * globalFeatureCount.get(ft_again) / averageGlobalFTCount;
				}
			}
			
			if(StringUtils.equals(ft, feature)) {
				dividend = current;
			}
			
			divisor = divisor + current;
		}
		
		return dividend / divisor;
	}
}
