package info.piwai.expressboard.android.rest.expressboard;


import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest("http://express-board.fr/remote")
public interface ExpressRestClient {
	@Get("/jobs")
	ExpressJobsResponse getJobs();

	void setRestTemplate(RestTemplate restTemplate);
}
