package tomketao.featuredetector.config;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "param_fields", "template" })
public class MatchRuleset extends FeatureObject {
	private static final long serialVersionUID = 8062190994766626417L;

	@JsonProperty("param_fields")
	private ArrayList<String> param_fields;

	@JsonProperty("template")
	private String template;

	public ArrayList<String> getParam_fields() {
		return param_fields;
	}

	public String getTemplate() {
		return template;
	}

	public void setParam_fields(ArrayList<String> param_fields) {
		this.param_fields = param_fields;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
