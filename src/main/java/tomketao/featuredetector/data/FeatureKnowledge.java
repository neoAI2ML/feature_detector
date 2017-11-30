package tomketao.featuredetector.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tomketao.featuredetector.connection.ESConnection;
import tomketao.featuredetector.util.CommonUtils;
import tomketao.featuredetector.util.StaticConstants;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "currentSequence", "currentFeatureCount" })
public class FeatureKnowledge extends HashMap<Integer, FeatureKey> {
	private static final long serialVersionUID = -6712633715281112680L;
	public static final Logger LOGGER = LoggerFactory
			.getLogger(FeatureKnowledge.class);

	@JsonProperty("currentSequence")
	private int currentSequence;

	@JsonProperty("currentFeatureCount")
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

	public boolean put_feature(String feature, String featureData, int sequence, TrainingSetting trainingSetting) {
		// update knowledge base global variables
		setCurrentSequence(sequence);
		Integer featureCount = getCurrentFeatureCount().get(feature);
		if (featureCount == null) {
			getCurrentFeatureCount().put(feature, 1);
		} else {
			getCurrentFeatureCount().put(feature, featureCount + 1);
		}

		// update knowledge base feature keys
		String featureDataNormalized = StringUtils.normalizeSpace(featureData)
				.toLowerCase();
		String[] keyWordList = featureDataNormalized
				.split(StaticConstants.SPACE);
		boolean addKeyFlag = false;

		for (int i = 0; i < keyWordList.length; i++) {
			StringBuilder keyStr = new StringBuilder();
			for (int j = 0; j < trainingSetting.getKeySize() && i + j < keyWordList.length; j++) {
				keyStr.append(StaticConstants.SPACE);
				keyStr.append(wordNormalizer(keyWordList[i + j]));
				addKeyFlag = put_feature_key(keyStr.substring(1), feature,
						sequence, j + 1);
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
				if (this.get(item).getSumOfFTCounts() < trainingSetting
						.getRareLimit()) {
					// LOGGER.info("RARE ****** SeqNo:" + updateSeq +
					// "\tFeatureCountSum: " + this.get(item).getSumOfFTCounts()
					// + "\tCurrentSeq:" + getCurrentSequence());
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
		for (Integer keyItem : this.keySet()) {
			List<Float> probList = new ArrayList<Float>();
			for (String ft : getCurrentFeatureCount().keySet()) {
				probList.add(CommonUtils.keyFeatureProbality(ft,
						this.get(keyItem).getFeatureCounts(),
						getCurrentFeatureCount()));
			}

			if (maximumDifference(probList) < trainingSetting
					.getMinimumImpact()) {
				listWOImpact.add(keyItem);
			}
		}

		for (Integer rmKeyitem : listWOImpact) {
			this.remove(rmKeyitem);
		}
	}

	private float maximumDifference(List<Float> dList) {
		Collections.sort(dList);
		return dList.get(dList.size() - 1) - dList.get(0);
	}

	public void save(TrainingSetting trainingSetting) {
		ESConnection esMeta = new ESConnection(trainingSetting.getStoreMetaDataUrl());
		esMeta.indexing("knowledge", mapForSave());
		
		ESConnection esFeature = new ESConnection(trainingSetting.getStoreFeatureDataUrl());
		
		for (Integer key : this.keySet()) {
			esFeature.indexing(key.toString(), this.get(key).mapForSave());
		}
	}
	
	public Map<String, Object> mapForSave() {
		Map<String, Object> store_map = new HashMap<String, Object>();
		
		store_map.put(StaticConstants.UPDATE_SEQ, getCurrentSequence());
		store_map.putAll(getCurrentFeatureCount());
		
		return store_map;
	}
}
