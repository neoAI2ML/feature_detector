package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureDetectObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"name","objects"})
public class FieldComparator extends FeatureDetectObject {
	private static final long serialVersionUID = 873458998616378936L;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("objects")
	private ComparatorObjects objects;
	
	public String getName() {
		return name;
	}

	public ComparatorObjects getObjects() {
		return objects;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObjects(ComparatorObjects objects) {
		this.objects = objects;
	}
}
