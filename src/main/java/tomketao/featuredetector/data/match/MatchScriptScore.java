package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureDetectObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "script", "lang", "params" })
public class MatchScriptScore extends FeatureDetectObject {
	private static final long serialVersionUID = -7694359896756683828L;

	@JsonProperty("script")
	private String script;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("params")
	private ScriptScoreParams params;

	public String getScript() {
		return script;
	}

	public String getLang() {
		return lang;
	}

	public ScriptScoreParams getParams() {
		return params;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setParams(ScriptScoreParams params) {
		this.params = params;
	}
}
