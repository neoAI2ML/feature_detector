package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "script_score", "weight" })
public class MatchScriptScoreFunction extends FeatureObject {
	private static final long serialVersionUID = -5929090392924086337L;

	@JsonProperty("script_score")
	private MatchScriptScore script_score;

	@JsonProperty("weight")
	private double weight;

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public MatchScriptScore getScript_score() {
		return script_score;
	}

	public void setScript_score(MatchScriptScore script_score) {
		this.script_score = script_score;
	}
}
