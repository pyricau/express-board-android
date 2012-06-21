package info.piwai.expressboard.android.rest.humancoders;

import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest("http://jobs.humancoders.com")
public interface HumanRestClient {
	@Get("/jobs.json")
	HumanJobsResponse getJobs();

	void setRestTemplate(RestTemplate restTemplate);
}
