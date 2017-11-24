package tomketao.featuredetector.data;

public class TrainingSetting  extends FeatureDetectObject {
	private static final long serialVersionUID = -1675848364528330441L;
	
	public int getValidSeqRange() {
		return validSeqRange;
	}
	public void setValidSeqRange(int validSeqRange) {
		this.validSeqRange = validSeqRange;
	}
	public int getKnowledgeLimit() {
		return knowledgeLimit;
	}
	public void setKnowledgeLimit(int knowledgeLimit) {
		this.knowledgeLimit = knowledgeLimit;
	}
	public int getRareLimit() {
		return rareLimit;
	}
	public void setRareLimit(int reaeLimit) {
		this.rareLimit = reaeLimit;
	}
	public float getMinimumImpact() {
		return minimumImpact;
	}
	public void setMinimumImpact(float minimumImpact) {
		this.minimumImpact = minimumImpact;
	}
	public int getKeySize() {
		return keySize;
	}
	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}
	
	private int validSeqRange;
	private int knowledgeLimit;
	private int rareLimit;
	private float minimumImpact;
	private int keySize;
}
