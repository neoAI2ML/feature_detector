package tomketao.featuredetector.data.match;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureDetectObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"query", "functions", "score_mode","boost_mode"})
public class MatchFunctionScore extends FeatureDetectObject {
	private static final long serialVersionUID = -7681574519000363163L;
	
	@JsonProperty("query")
	private MatchFunctionScoreQuery query;
	
	@JsonProperty("score_mode")
	private String score_mode;
	
	@JsonProperty("boost_mode")
	private String boost_mode;

	@JsonProperty("functions")
	private ArrayList<MatchScriptScoreFunction> functions;

	public MatchFunctionScoreQuery getQuery() {
		return query;
	}

	public String getScore_mode() {
		return score_mode;
	}

	public String getBoost_mode() {
		return boost_mode;
	}

	public ArrayList<MatchScriptScoreFunction> getFunctions() {
		return functions;
	}

	public void setQuery(MatchFunctionScoreQuery query) {
		this.query = query;
	}

	public void setScore_mode(String score_mode) {
		this.score_mode = score_mode;
	}

	public void setBoost_mode(String boost_mode) {
		this.boost_mode = boost_mode;
	}

	public void setFunctions(ArrayList<MatchScriptScoreFunction> functions) {
		this.functions = functions;
	}
}
