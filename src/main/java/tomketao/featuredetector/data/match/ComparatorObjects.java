package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"object"})
public class ComparatorObjects extends FeatureObject {
	private static final long serialVersionUID = 149277127542521602L;
	
	@JsonProperty("object")
	private ComparatorObject object;

	public ComparatorObject getObject() {
		return object;
	}

	public void setObject(ComparatorObject object) {
		this.object = object;
	}
}
