package tomketao.featuredetector.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import tomketao.featuredetector.data.FeatureDetectObject;
import tomketao.featuredetector.data.response.RespHit;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "param_fields", "field_params", "match_comp",
		"qualified_matches", "blank_is_match_fields", "expansion_fields",
		"qualification_score", "groupid_field", "rulesets", "back_fill",
		"new_group", "time_stamp", "rejected_flag", "disqualified_fill",
		"skip_grouped" })
public class MatchConfig extends FeatureDetectObject {
	private static final long serialVersionUID = 2530337448945097752L;

	@JsonProperty("param_fields")
	private ArrayList<String> param_fields;

	@JsonProperty("field_params")
	private Map<String, FieldParams> field_params;

	public Map<String, FieldParams> getField_params() {
		return field_params;
	}

	public void setField_params(Map<String, FieldParams> field_params) {
		this.field_params = field_params;
	}

	@JsonProperty("match_comp")
	private Map<String, Map<String, String>> match_comp;

	@JsonProperty("qualified_matches")
	private ArrayList<ArrayList<String>> qualified_matches;

	@JsonProperty("blank_is_match_fields")
	private ArrayList<String> blank_is_match_fields;

	@JsonProperty("expansion_fields")
	private Map<String, String> expansion_fields;

	@JsonProperty("qualification_score")
	private double qualification_score;

	@JsonProperty("skip_grouped")
	private boolean skip_grouped;

	@JsonProperty("groupid_field")
	private String groupid_field;

	@JsonProperty("rulesets")
	private ArrayList<MatchRuleset> rulesets;

	@JsonProperty("back_fill")
	private ArrayList<String> back_fill;

	@JsonProperty("new_group")
	private String new_group;

	@JsonProperty("time_stamp")
	private String time_stamp;

	@JsonProperty("rejected_flag")
	private String rejected_flag;

	@JsonProperty("disqualified_fill")
	private Map<String, String> disqualified_fill;

	public String getRejected_flag() {
		return rejected_flag;
	}

	public void setRejected_flag(String rejected_flag) {
		this.rejected_flag = rejected_flag;
	}

	public Map<String, String> getDisqualified_fill() {
		return disqualified_fill;
	}

	public void setDisqualified_fill(Map<String, String> disqualified_fill) {
		this.disqualified_fill = disqualified_fill;
	}

	public boolean isSkip_grouped() {
		return skip_grouped;
	}

	public void setSkip_grouped(boolean skip_grouped) {
		this.skip_grouped = skip_grouped;
	}

	public Map<String, String> getExpansion_fields() {
		return expansion_fields;
	}

	public void setExpansion_fields(Map<String, String> expansion_fields) {
		this.expansion_fields = expansion_fields;
	}

	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getNew_group() {
		return new_group;
	}

	public void setNew_group(String new_group) {
		this.new_group = new_group;
	}

	public ArrayList<String> getBack_fill() {
		return back_fill;
	}

	public void setBack_fill(ArrayList<String> back_fill) {
		this.back_fill = back_fill;
	}

	public String getGroupid_field() {
		return groupid_field;
	}

	public void setGroupid_field(String groupid_field) {
		this.groupid_field = groupid_field;
	}

	public double getQualification_score() {
		return qualification_score;
	}

	public Map<String, Map<String, String>> getMatch_comp() {
		return match_comp;
	}

	public void setMatch_comp(Map<String, Map<String, String>> match_comp) {
		this.match_comp = match_comp;
	}

	public void setQualification_score(double qualification_score) {
		this.qualification_score = qualification_score;
	}

	public ArrayList<String> getParam_fields() {
		return param_fields;
	}

	public ArrayList<MatchRuleset> getRulesets() {
		return rulesets;
	}

	public ArrayList<ArrayList<String>> getQualified_matches() {
		return qualified_matches;
	}

	public ArrayList<String> getBlank_is_match_fields() {
		return blank_is_match_fields;
	}

	public void setBlank_is_match_fields(ArrayList<String> blank_is_match_fields) {
		this.blank_is_match_fields = blank_is_match_fields;
	}

	public void setQualified_matches(
			ArrayList<ArrayList<String>> qualified_matches) {
		this.qualified_matches = qualified_matches;
	}

	public void setParam_fields(ArrayList<String> param_fields) {
		this.param_fields = param_fields;
	}

	public void setRulesets(ArrayList<MatchRuleset> rulesets) {
		this.rulesets = rulesets;
	}

	public static MatchConfig loadFromFile(String filePath) {
		ObjectMapper obm = new ObjectMapper();
		obm.setSerializationInclusion(Inclusion.NON_NULL);

		try {
			InputStream configInputStream = MatchConfig.class.getClassLoader()
					.getResourceAsStream(filePath);
			MatchConfig req = obm.readValue(configInputStream,
					MatchConfig.class);
			configInputStream.close();
			return req;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public boolean isEmptyForGrouping(Map<String, Object> inputRec) {
		for (String field : getParam_fields()) {
			Object fieldObj = inputRec.get(field);
			if (fieldObj != null) {
				String fieldVal = StringUtils.strip(fieldObj.toString(),
						" _,.@-!#$%^&*");
				if (StringUtils.isNotBlank(fieldVal)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isExactMatch(Map<String, Object> src, Map<String, Object> tgt) {
		boolean equalFlag = true;
		for (String fieldKey : getParam_fields()) {
			Object field = src.get(fieldKey);
			Object cfield = tgt.get(fieldKey);
			String fieldVal = field == null ? "" : field.toString();
			fieldVal = fieldVal == null ? "" : fieldVal;
			String cfieldVal = cfield == null ? "" : cfield.toString();
			cfieldVal = cfieldVal == null ? "" : cfieldVal;
			if (fieldVal.compareTo(cfieldVal) == 0) {
			} else {
				equalFlag = false;
				break;
			}
		}

		return equalFlag;
	}

	public Map<String, String> getDisqualifiedFillObject(RespHit hit) {
		if (hit != null) {
			String specialGoldenID = specialGoldenID(hit);
			return getDisqualifiedFillObject(hit, specialGoldenID);
		}
		return null;
	}

	public Map<String, String> getDisqualifiedFillObject(RespHit hit,
			String defaultString) {
		if (hit != null) {
			HashMap<String, String> ret = new HashMap<String, String>();
			if (disqualified_fill != null && disqualified_fill.size() > 0) {
				for (String key : disqualified_fill.keySet()) {
					String ffld = hit.getFieldStringValue(
							disqualified_fill.get(key), defaultString);
					if (StringUtils.isNotBlank(ffld)) {
						ret.put(key, ffld);
					}
				}
			}
			if(StringUtils.isNotBlank(rejected_flag)) {
				ret.put(rejected_flag, "true");
			}

			if (ret.size() > 0) {
				return ret;
			}
		}

		return null;
	}

	public String specialGoldenID(RespHit hit) {
		String specialGoldenID = hit.getId();
		int year = Integer.parseInt(specialGoldenID.substring(0, 4)) + 2000;
		String first4 = String.valueOf(year);
		specialGoldenID = first4 + specialGoldenID.substring(4);

		return specialGoldenID;
	}
}
