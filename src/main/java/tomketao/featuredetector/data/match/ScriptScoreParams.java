package tomketao.featuredetector.data.match;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"entity"})
public class ScriptScoreParams extends FeatureObject {
	private static final long serialVersionUID = -1644649560577606188L;
	
	@JsonProperty("entity")
	private ParamsEntity entity;
	
	public ParamsEntity getEntity() {
		return entity;
	}

	public void setEntity(ParamsEntity entity) {
		this.entity = entity;
	}
}
