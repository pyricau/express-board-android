package info.piwai.expressboard.android.rest;

import info.piwai.expressboard.android.BuildConfig;
import info.piwai.expressboard.android.JobListActivity;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EBean
public class JobsDownloadTask {

	private static final String TAG = JobsDownloadTask.class.getSimpleName();

	@RootContext
	JobListActivity activity;

	@RestService
	RestClient restClient;

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
				List<MediaType> supportedMediaTypes = Arrays.asList(expressJsonType);
				jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
				jacksonConverter.setObjectMapper(objectMapper);
			}
		}
		restClient.setRestTemplate(restTemplate);
	}

	public void downloadJobs() {
		isDownloading = true;
		downloadJobsInBackground();
	}

	@Background
	void downloadJobsInBackground() {
		try {
			JobsResponse result = restClient.getJobs();
			onJobsDownloaded(result);
		} catch (RestClientException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Could not download jobs", e);
			}
			onDownloadError();
		}

	}

	@UiThread
	void onJobsDownloaded(JobsResponse result) {
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
