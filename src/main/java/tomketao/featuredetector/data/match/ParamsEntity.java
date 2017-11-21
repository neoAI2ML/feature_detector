package tomketao.featuredetector.data.match;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureDetectObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"fields"})
public class ParamsEntity extends FeatureDetectObject {
	private static final long serialVersionUID = -4557758584047369662L;
	
	@JsonProperty("fields")
	private ArrayList<MatchField> fields;

	public ArrayList<MatchField> getFields() {
		return fields;
	}

	public void setFields(ArrayList<MatchField> fields) {
		this.fields = fields;
	}
}
