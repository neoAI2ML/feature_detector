package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"name","class"})
public class ComparatorObject extends FeatureObject {
	private static final long serialVersionUID = 1056231653942099316L;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("class")
	private String _class;
	
	public String getName() {
		return name;
	}
	public String get_class() {
		return _class;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
}
