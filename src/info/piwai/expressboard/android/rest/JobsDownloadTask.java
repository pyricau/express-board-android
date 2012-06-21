package info.piwai.expressboard.android.rest;

import static java.util.Arrays.asList;
import info.piwai.expressboard.android.BuildConfig;
import info.piwai.expressboard.android.JobListActivity;
import info.piwai.expressboard.android.R;
import info.piwai.expressboard.android.rest.expressboard.ExpressJob;
import info.piwai.expressboard.android.rest.expressboard.ExpressJobsResponse;
import info.piwai.expressboard.android.rest.expressboard.ExpressRestClient;
import info.piwai.expressboard.android.rest.humancoders.HumanJob;
import info.piwai.expressboard.android.rest.humancoders.HumanJobsResponse;
import info.piwai.expressboard.android.rest.humancoders.HumanRestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.ocpsoft.pretty.time.PrettyTime;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EBean
public class JobsDownloadTask {

	private static final String TAG = JobsDownloadTask.class.getSimpleName();

	private final PrettyTime prettyTime = new PrettyTime();

	@RootContext
	JobListActivity activity;

	@RestService
	ExpressRestClient expressRestClient;

	@RestService
	HumanRestClient humanRestClient;

	boolean isDownloading;

	@AfterInject
	void prepareRestClient() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		objectMapper.configure(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS, false);
		objectMapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);

		RestTemplate restTemplate = new RestTemplate();

		/*
		 * Not needed in stable release of Spring Android, works the other way
		 */
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> httpMessageConverter : messageConverters) {
			if (httpMessageConverter instanceof MappingJacksonHttpMessageConverter) {
				MappingJacksonHttpMessageConverter jacksonConverter = (MappingJacksonHttpMessageConverter) httpMessageConverter;
				MediaType expressJsonType = MediaType.parseMediaType("text/json;charset=utf-8");
				List<MediaType> supportedMediaTypes = Arrays.asList(expressJsonType, MediaType.APPLICATION_JSON);
				jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
				jacksonConverter.setObjectMapper(objectMapper);
			}
		}
		expressRestClient.setRestTemplate(restTemplate);
		humanRestClient.setRestTemplate(restTemplate);
	}

	public void downloadJobs() {
		isDownloading = true;
		downloadJobsInBackground();
	}

	@Background
	void downloadJobsInBackground() {

		List<Job> result = new ArrayList<Job>();

		boolean expressError = false;
		try {
			ExpressJobsResponse response = expressRestClient.getJobs();

			for (ExpressJob expressJob : response.jobs) {
				Job job = new Job();
				job.title = expressJob.title;
				job.url = expressJob.url;
				job.companyUrl = expressJob.company_url;
				job.contract = expressJob.contract;
				job.company = expressJob.company;
				job.location = expressJob.area;
				job.publishedAtdate = expressJob.postedAt;
				job.publishedAtString = prettyTime.format(expressJob.postedAt);
				job.salary = expressJob.salary;
				job.experience = expressJob.experience;
				job.tags = expressJob.tags;
				result.add(job);
			}

		} catch (RestClientException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Could not download express jobs", e);
			}
			expressError = true;
		}

		boolean humanError = false;
		try {
			HumanJobsResponse response = humanRestClient.getJobs();

			for (HumanJob humanJob : response) {
				Job job = new Job();
				job.title = humanJob.title;
				job.url = humanJob.url;
				job.companyUrl = null;
				job.contract = humanJob.job_type;
				job.company = humanJob.company_name;
				job.location = humanJob.location;
				job.publishedAtdate = humanJob.published_at;
				job.publishedAtString = prettyTime.format(humanJob.published_at);
				job.salary = null;
				job.experience = null;
				job.tags = asList(humanJob.site);
				result.add(job);
			}

		} catch (RestClientException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Could not download human jobs", e);
			}
			humanError = true;
		}

		Collections.sort(result);

		if (expressError && humanError) {
			onDownloadError();
		} else {
			onJobsDownloaded(result, expressError, humanError);
		}

	}

	@UiThread
	void onJobsDownloaded(List<Job> result, boolean expressError, boolean humanError) {

		if (expressError) {
			Toast.makeText(activity, R.string.express_download_error, Toast.LENGTH_SHORT).show();
		} else if (humanError) {
			Toast.makeText(activity, R.string.human_download_error, Toast.LENGTH_SHORT).show();
		}

		isDownloading = false;
		activity.onJobsDownloaded(result);
	}

	@UiThread
	void onDownloadError() {
		isDownloading = false;
		activity.onDownloadError();
	}

	public boolean isDownloading() {
		return isDownloading;
	}

}
