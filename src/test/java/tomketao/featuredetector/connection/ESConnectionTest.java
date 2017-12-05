package tomketao.featuredetector.connection;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import tomketao.featuredetector.data.TrainingSetting;
import tomketao.featuredetector.data.match.StringQueryRequest;
import tomketao.featuredetector.data.response.MatchResponse;

public class ESConnectionTest {
	private TrainingSetting trainingSetting;
	private ESConnection es;

	@BeforeClass
	public void beforeClass() {
		trainingSetting = TrainingSetting.loadFromFile("conf/fd-config.json");
		es = new ESConnection(trainingSetting.getStoreFeatureDataUrl());
	}


  @Test
  public void scrollStringRequest() {
	  StringQueryRequest stringQuery = StringQueryRequest.loadFromFile("all_query.json");
	  String timeout = "1m";
	  MatchResponse resp = es.scrollStringQuery(stringQuery.convertToString(), timeout);
	  String scroll_id = resp.getScroll_id();
	  
	  while(!resp.getHits().getHits().isEmpty()) {
		  resp = es.scrollStringQueryNext(scroll_id, timeout);
	  }
	  
	  System.out.println("Scroll ID: " + scroll_id);
  }
}
