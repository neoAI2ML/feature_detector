package tomketao.featuredetector.matchbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomketao.featuredetector.config.FieldParams;
import tomketao.featuredetector.config.MatchConfig;
import tomketao.featuredetector.config.MatchRuleset;
import tomketao.featuredetector.data.match.MatchFunctionScore;
import tomketao.featuredetector.data.match.MatchFunctionScoreQuery;
import tomketao.featuredetector.data.match.MatchRequest;
import tomketao.featuredetector.data.match.QueryString;
import tomketao.featuredetector.data.match.StringQueryRequest;
import tomketao.featuredetector.data.request.UpdateRequest;
import tomketao.featuredetector.util.DeepClone;

public class MatchRequestBuilder {
	public static HashSet<String> getReservedWords() {
		return reservedWords;
	}

	public static char[] getReservedchars() {
		return reservedChars;
	}

	public static final Logger LOGGER = LoggerFactory
			.getLogger(MatchRequestBuilder.class);
	private MatchConfig matchConfig;
	private Map<String, MatchRequest> templates = new HashMap<String, MatchRequest>();
	private ArrayList<String> categoriesList = new ArrayList<String>();
	private String defaultKey;
	private static HashSet<String> reservedWords = new HashSet<String>(
			Arrays.asList(new String[] { "AND", "OR", "NOT" }));
	private static final char[] reservedChars = new char[] { '+', '-', '&',
			'|', '!', '(', ')', '{', '}', '[', ']', '^', '"', '~', '*', '?',
			':', '\\', '/' };
	private static final char[] dollar = new char[] { '$' };

	public MatchRequestBuilder(MatchConfig matchConfig) {
		this.matchConfig = matchConfig;
		initializeRequestBuilder(getMatchConfig());
	}

	public MatchConfig getMatchConfig() {
		return matchConfig;
	}

	public void setMatchConfig(MatchConfig matchConfig) {
		this.matchConfig = matchConfig;
	}

	private void initializeRequestBuilder(MatchConfig matchConfig) {
		categoriesList.addAll(matchConfig.getMatch_comp().keySet());
		String formatStr = "%0" + categoriesList.size() + "d";

		defaultKey = String.format(formatStr, 0);

		for (MatchRuleset ruleSet : matchConfig.getRulesets()) {
			// form a template id
			Set<String> set = new HashSet<String>(ruleSet.getParam_fields());
			StringBuilder key = new StringBuilder();
			for (int i = 0; i < categoriesList.size(); i++) {
				if (set.contains(categoriesList.get(i))) {
					key.append('1');
				} else {
					key.append('0');
				}
			}

			// add the ruleSet into the ruleSets cache
			// ruleSets.put(key.toString(), ruleSet);

			// load request template
			MatchRequest req = MatchRequest.loadFromFile(ruleSet.getTemplate());

			// add the template into the template cache
			templates.put(key.toString(), req);
		}
	}

	public String getTemplateKey(Map<String, Object> input) {
		StringBuilder key = new StringBuilder();
		for (int i = 0; i < categoriesList.size(); i++) {
			boolean setFlag = false;
			Map<String, String> catMap = getMatchConfig().getMatch_comp().get(
					categoriesList.get(i));
			for (String field : catMap.keySet()) {
				Object fval = input.get(field);
				if (fval != null) {
					String value = fval.toString();
					String nValue = value == null ? null : StringUtils.strip(
							value, " ,.@-!#$%^&*");
					if (StringUtils.isNotBlank(nValue)) {
						if (StringUtils.isBlank(catMap.get(field))
								|| catMap.get(field).compareTo(nValue) == 0) {
							setFlag = true;
							break;
						}
					}
				}
			}

			if (setFlag) {
				key.append('1');
			} else {
				key.append('0');
			}
		}

		String tempKey = key.toString();
		LOGGER.info("Rule Set Key: " + tempKey);
		return tempKey;
	}

	public MatchRequest getRequestTemplate(String key) {
		MatchRequest template = templates.get(key);
		if (template == null) {
			template = templates.get(defaultKey);
		}
		return template;
	}

	public MatchRequest buildMatchRequest(Map<String, Object> input) {
		String templateKey = getTemplateKey(input);
		return buildMatchRequest(templateKey, input);
	}

	public MatchRequest buildMatchRequest(String templateKey,
			Map<String, Object> input) {
		if (templates.containsKey(templateKey)) {
			return buildMatchRequest(input, templateKey);
		} else {
			return null;
			// return buildDefaultMatchRequest(input, defaultKey);
		}
	}

	public StringQueryRequest buildStringQueryRequest(MatchRequest request) {
		StringQueryRequest rqst = new StringQueryRequest();
		MatchFunctionScoreQuery mfsq = (MatchFunctionScoreQuery) DeepClone
				.deepClone(request.getQuery().getFunction_score().getQuery());

		rqst.setQuery(mfsq);
		rqst.setSize(request.getSize());
		return rqst;
	}

	public StringQueryRequest buildNoRuleStringQueryRequest(
			Map<String, Object> input) {
		StringQueryRequest rqst = new StringQueryRequest();
		MatchFunctionScoreQuery mfsq = new MatchFunctionScoreQuery();
		QueryString qs = new QueryString();

		StringBuilder strbuilder = new StringBuilder();
		String forQueryJoin = " AND ";

		for (String field : matchConfig.getParam_fields()) {
			Object fieldValueObject = input.get(field);
			String forStriping = " ,;.@-";
			String forSplitting = "[ ,;@-]";

			if (matchConfig.getField_params() != null) {
				FieldParams fldParams = matchConfig.getField_params()
						.get(field);
				if (fldParams != null) {
					forStriping = fldParams.stringForStripping(forStriping);
					forSplitting = fldParams.stringForSplitting(forSplitting);
				}
			}
			
			if (fieldValueObject == null) {
				strbuilder.append(" AND !");
				strbuilder.append(field);
				strbuilder.append(":");
				strbuilder.append("[* TO *]");
				continue;
			}

			String fieldValue = fieldValueObject.toString();
			if (StringUtils.isBlank(fieldValue)) {
				strbuilder.append(" AND !");
				strbuilder.append(field);
				strbuilder.append(":");
				strbuilder.append("[* TO *]");
				continue;
			}

			String nfieldValue = StringUtils.strip(fieldValue, forStriping);
			if (StringUtils.isBlank(nfieldValue)) {
				continue;
			}

			String[] wordList = nfieldValue.split(forSplitting);
			for (String word : wordList) {
				word = StringUtils.strip(word, forStriping);
				if (StringUtils.isBlank(word)) {
					continue;
				}
				if (reservedWords.contains(word)) {
					word = "'" + word + "'";
				} else if (StringUtils.containsAny(word, reservedChars)) {
					word = QueryParser.escape(QueryParser.escape(word));
				} else {
				}

				if (StringUtils.containsAny(word, dollar)) {
					word = word.replaceAll("\\$", "\\\\\\$");
				}

				strbuilder.append(forQueryJoin);
				strbuilder.append(field);
				strbuilder.append(":");
				strbuilder.append(word);
			}

		}

		if (strbuilder.length() > forQueryJoin.length()) {
			String queryString = strbuilder.substring(forQueryJoin.length());
			qs.setQuery(queryString);
			mfsq.setQuery_string(qs);
			rqst.setQuery(mfsq);
			rqst.setSize("100");
			return rqst;
		} else {
			return null;
		}
	}

	public StringQueryRequest buildStringQueryRequest(String templateKey,
			Map<String, Object> input) {
		if (templates.containsKey(templateKey)) {
			MatchRequest requestPlacehold = (MatchRequest) DeepClone
					.deepClone(getRequestTemplate(templateKey));

			if (requestPlacehold != null) {
				MatchFunctionScore fs = requestPlacehold.getQuery()
						.getFunction_score();
				MatchFunctionScoreQuery mfsq = fs.getQuery();
				QueryString qs = mfsq.getQuery_string();
				String queryString = buildQueryString(
						StringUtils.normalizeSpace(qs.getQuery()), input);

				qs.setQuery(queryString);
				mfsq.setQuery_string(qs);
				StringQueryRequest rqst = new StringQueryRequest();
				rqst.setQuery(mfsq);
				rqst.setSize(requestPlacehold.getSize());
				return rqst;
			}
		}
		return null;
	}

	public MatchRequest buildDefaultMatchRequest(Map<String, Object> input,
			String key) {
		MatchRequest requestPlacehold = (MatchRequest) DeepClone
				.deepClone(getRequestTemplate(key));

		if (requestPlacehold != null) {
			MatchFunctionScore fs = requestPlacehold.getQuery()
					.getFunction_score();
			QueryString qs = fs.getQuery().getQuery_string();
			String queryString = buildQueryString(
					StringUtils.normalizeSpace(qs.getQuery()), input);

			if (StringUtils.isNotBlank(queryString)) {
				qs.setQuery(queryString);
			} else {
				qs.setQuery("*:*");
			}

			requestPlacehold.setMin_score(Double.toString(matchConfig
					.getQualification_score()));
		}
		return requestPlacehold;
	}

	private String replaceNoValue(String queryString, String replaced) {
		queryString = queryString.replaceAll("AND \\(\\(" + replaced
				+ "\\)\\) AND", "AND");
		queryString = queryString.replaceAll("\\(\\(" + replaced
				+ "\\)\\) AND ", "");
		queryString = queryString.replaceAll(" AND \\(\\(" + replaced
				+ "\\)\\)", "");
		queryString = queryString.replaceAll("OR \\(\\(" + replaced
				+ "\\)\\) OR", "OR");
		queryString = queryString.replaceAll(
				"\\(\\(" + replaced + "\\)\\) OR ", "");
		queryString = queryString.replaceAll(
				" OR \\(\\(" + replaced + "\\)\\)", "");

		queryString = queryString.replaceAll("AND \\(" + replaced + "\\) AND",
				"AND");
		queryString = queryString.replaceAll("\\(" + replaced + "\\) AND ", "");
		queryString = queryString.replaceAll(" AND \\(" + replaced + "\\)", "");
		queryString = queryString.replaceAll("OR \\(" + replaced + "\\) OR",
				"OR");
		queryString = queryString.replaceAll("\\(" + replaced + "\\) OR ", "");
		queryString = queryString.replaceAll(" OR \\(" + replaced + "\\)", "");

		queryString = queryString.replaceAll("AND " + replaced + " AND", "AND");
		queryString = queryString.replaceAll(replaced + " AND ", "");
		queryString = queryString.replaceAll(" AND " + replaced, "");
		queryString = queryString.replaceAll("OR " + replaced + " OR", "OR");
		queryString = queryString.replaceAll(replaced + " OR ", "");
		queryString = queryString.replaceAll(" OR " + replaced, "");

		return queryString;
	}

	private String buildQueryString(String queryString,
			Map<String, Object> input) {
		// create query string from the input
		try {
			for (String fld : matchConfig.getParam_fields()) {
				String replaced = fld + ":\\{\\{" + fld + "\\}\\}";
				String replacedBlank = "!" + fld + ":[* TO *]";

				if (matchConfig.getExpansion_fields() != null) {
					String associated = matchConfig.getExpansion_fields().get(
							fld);
					if (StringUtils.isNotBlank(associated)) {
						Object efval = input.get(associated);

						if (efval != null) {
							String val = efval.toString();
							if (StringUtils.isNotBlank(val)) {
								FieldParams fldParams = null;
								if (matchConfig.getField_params() != null) {
									fldParams = matchConfig.getField_params()
											.get(fld);
								}
								String qStr = createExpQueryString(fld, val,
										fldParams);
								if (qStr != null) {
									queryString = queryString.replaceAll(
											replaced, qStr);
									continue;
								}
							}
						}
					}
				}

				Object fval = input.get(fld);
				if (fval != null) {
					String val = fval.toString();
					if (StringUtils.isNotBlank(val)) {
						FieldParams fldParams = null;
						if (matchConfig.getField_params() != null) {
							fldParams = matchConfig.getField_params().get(fld);
						}
						String qStr = createQueryString(fld, val, fldParams);
						if (qStr == null) {
							queryString = replaceNoValue(queryString, replaced);
						} else {
							queryString = queryString
									.replaceAll(replaced, qStr);
						}
					} else {
						if (matchConfig.getBlank_is_match_fields()
								.contains(fld)) {
							queryString = replaceNoValue(queryString, replaced);
						} else {
							queryString = queryString.replaceAll(replaced,
									replacedBlank);
						}
					}
				} else {
					if (matchConfig.getBlank_is_match_fields().contains(fld)) {
						queryString = replaceNoValue(queryString, replaced);
					} else {
						queryString = queryString.replaceAll(replaced,
								replacedBlank);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			LOGGER.error("Query String: " + queryString);
			LOGGER.error("Input: " + input.toString());
		}
		LOGGER.debug("Query String: " + queryString);
		return queryString;
	}

	public MatchRequest buildMatchRequest(Map<String, Object> input, String key) {
		MatchRequest requestPlacehold = (MatchRequest) DeepClone
				.deepClone(getRequestTemplate(key));

		if (requestPlacehold != null) {
			MatchFunctionScore fs = requestPlacehold.getQuery()
					.getFunction_score();
			QueryString qs = fs.getQuery().getQuery_string();
			String queryString = buildQueryString(
					StringUtils.normalizeSpace(qs.getQuery()), input);

			if (StringUtils.isNotBlank(queryString)) {
				qs.setQuery(queryString);
			} else {
				qs.setQuery("*:*");
			}

			requestPlacehold.setMin_score(Double.toString(matchConfig
					.getQualification_score()));
		}
		return requestPlacehold;
	}

	public static String createQueryString(String field, String value,
			FieldParams fldParams) {
		StringBuilder sb = new StringBuilder();
		String forStriping = fldParams == null ? " ,;.@-" : fldParams
				.stringForStripping(" ,;.@-");
		String forSplitting = fldParams == null ? "[ ,;@-]" : fldParams
				.stringForSplitting("[ ,;@-]");
		String forQueryJoin = fldParams == null ? " AND " : fldParams.stringForQueryJoin(" AND ");

		String nfieldValue = StringUtils.strip(value, forStriping);
		String[] wordList = nfieldValue.split(forSplitting);

		for (String word : wordList) {
			word = StringUtils.strip(word, forStriping);
			if (StringUtils.isBlank(word)) {
				continue;
			}
			boolean special = false;
			if (reservedWords.contains(word)) {
				word = "'" + word + "'";
				special = true;
			} else if (StringUtils.containsAny(word, reservedChars)) {
				word = QueryParser.escape(QueryParser.escape(word));
				special = true;
			} else {
			}

			if (StringUtils.containsAny(word, dollar)) {
				word = word.replaceAll("\\$", "\\\\\\$");
				special = true;
			}

			double fuzzySize = 0;
			if (fldParams != null) {
				fuzzySize = (1.0 - fldParams.getConfident()) * (word.length() + 1);
			}

			if (fuzzySize < 1) {
				sb.append(forQueryJoin);
				sb.append(field);
				sb.append(":");
				sb.append(word);
			} else if (fuzzySize < 2) {
				sb.append(forQueryJoin);
				sb.append(field);
				sb.append(":");
				sb.append(word);
				if (word.matches("[_]+") || special) {
				} else {
					sb.append("~1");
				}
			} else {
				sb.append(forQueryJoin);
				sb.append(field);
				sb.append(":");
				sb.append(word);
				if (word.matches("[_]+") || special) {
				} else {
					sb.append("~2");
				}
			}
		}

		if (sb.length() > forQueryJoin.length()) {
			if (wordList.length > 1) {
				return "(" + sb.substring(forQueryJoin.length()) + ")";
			} else {
				return sb.substring(forQueryJoin.length());
			}
		} else {
			return null;
		}
	}

	public static String createExpQueryString(String field, String value,
			FieldParams fldParams) {
		StringBuilder sb = new StringBuilder();

		String forStriping = fldParams == null ? " ,;.@-" : fldParams
				.stringForStripping(" ,;.@-");
		String forSplitting = fldParams == null ? "[ ,;@-]" : fldParams
				.stringForSplitting("[ ,;@-]");

		String nfieldValue = StringUtils.strip(value, forStriping);
		String[] wordList = nfieldValue.split(forSplitting);

		for (String word : wordList) {
			word = StringUtils.strip(word, forStriping);
			if (StringUtils.isBlank(word)) {
				continue;
			}
			boolean special = false;
			if (reservedWords.contains(word)) {
				word = "'" + word + "'";
				special = true;
			} else if (StringUtils.containsAny(word, reservedChars)) {
				word = QueryParser.escape(QueryParser.escape(word));
				special = true;
			} else {
			}

			if (StringUtils.containsAny(word, dollar)) {
				word = word.replaceAll("\\$", "\\\\\\$");
				special = true;
			}

			double fuzzySize = 0;
			if (fldParams != null) {
				fuzzySize = (1.0 - fldParams.getConfident()) * (word.length() + 1);
			}

			if (fuzzySize < 1) {
				sb.append(" ");
				sb.append(word);
			} else if (fuzzySize < 2) {
				sb.append(" ");
				sb.append(word);
				if (word.matches("[_]+") || special) {
				} else {
					sb.append("~1");
				}
			} else {
				sb.append(" ");
				sb.append(word);
				if (word.matches("[_]+") || special) {
				} else {
					sb.append("~2");
				}
			}
		}

		if (sb.length() > 1) {
			if (wordList.length > 1) {
				return field + ":(" + sb.substring(1) + ")";
			} else {
				return field + ":" + sb.substring(1);
			}
		} else {
			return null;
		}
	}

	public UpdateRequest buildUpdateRequest(Map<String, String> input,
			UpdateRequest requestTemplate) {
		UpdateRequest requestPlacehold = (UpdateRequest) DeepClone
				.deepClone(requestTemplate);
		Map<String, Object> doc = requestPlacehold.getDoc();
		doc.put(matchConfig.getGroupid_field(),
				input.get(matchConfig.getGroupid_field()));

		return requestPlacehold;
	}
}
