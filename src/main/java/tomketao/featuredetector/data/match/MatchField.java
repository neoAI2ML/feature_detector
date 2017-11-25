package tomketao.featuredetector.data.match;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import tomketao.featuredetector.data.FeatureDetectObject;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "field", "value", "high", "low", "comparator" })
public class MatchField extends FeatureDetectObject {
	private static final long serialVersionUID = -5203821613792804310L;

	@JsonProperty("field")
	private String field;

	@JsonProperty("value")
	private String value;

	@JsonProperty("high")
	private double high;

	@JsonProperty("low")
	private double low;

	@JsonProperty("comparator")
	private FieldComparator comparator;

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public FieldComparator getComparator() {
		return comparator;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public void setComparator(FieldComparator comparator) {
		this.comparator = comparator;
	}
}
