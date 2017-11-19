package tomketao.featuredetector.config;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "confident", "tokenizers", "query_join" })
public class FieldParams extends FeatureObject {
	private static final long serialVersionUID = -4060756715983589644L;

	@JsonProperty("confident")
	private double confident = 1.0;

	@JsonProperty("tokenizers")
	private ArrayList<String> tokenizers;
	
	@JsonProperty("query_join")
	private String query_join = "AND";

	public String getQuery_join() {
		return query_join;
	}

	public void setQuery_join(String query_join) {
		this.query_join = query_join;
	}

	public double getConfident() {
		return confident;
	}

	public void setConfident(double confident) {
		this.confident = confident;
	}

	public ArrayList<String> getTokenizers() {
		return tokenizers;
	}

	public void setTokenizers(ArrayList<String> tokenizers) {
		this.tokenizers = tokenizers;
	}

	public String stringForStripping(String defaultString) {
		if (tokenizers != null && tokenizers.size() > 0) {
			StringBuilder ret = new StringBuilder();
			for (int i = 0; i < tokenizers.size(); i++) {
				ret.append(tokenizers.get(i));
			}
			return ret.toString();
		}
		return defaultString;
	}

	public String stringForSplitting(String defaultString) {
		if (tokenizers != null && tokenizers.size() > 0) {
			StringBuilder ret = new StringBuilder();
			ret.append("[");
			ret.append(tokenizers.get(0));

			for (int i = 1; i < tokenizers.size(); i++) {
				ret.append("|");
				ret.append(tokenizers.get(i));
			}
			ret.append("]");
			return ret.toString();
		}

		return defaultString;
	}
	
	public String stringForQueryJoin(String defaultString) {
		if(StringUtils.isBlank(query_join)) {
			return defaultString;
		} else {
			return " " + getQuery_join() + " ";
		}
	}

}
