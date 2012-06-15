package info.piwai.expressboard.android.rest;

import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest("http://express-board.fr/remote")
public interface RestClient {
	@Get("/jobs")
	JobsResponse getJobs();

	void setRestTemplate(RestTemplate restTemplate);
}
