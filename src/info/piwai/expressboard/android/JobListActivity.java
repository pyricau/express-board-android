package info.piwai.expressboard.android;

import info.piwai.expressboard.android.rest.Job;
import info.piwai.expressboard.android.rest.JobsDownloadTask;
import info.piwai.expressboard.android.rest.JobsResponse;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.NonConfigurationInstance;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.HtmlRes;

@OptionsMenu(R.menu.job_list)
@EActivity(R.layout.job_list)
public class JobListActivity extends SherlockActivity {

	@HtmlRes
	CharSequence jobListTitle;

	@Bean
	@NonConfigurationInstance
	JobsDownloadTask downloadTask;

	@NonConfigurationInstance
	JobsResponse jobsResponse;

	@ViewById
	ListView jobList;

	@Bean
	JobAdapter adapter;

	@ViewById
	TextView emptyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	@AfterViews
	void initViews() {

		LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.list_slide_in_from_left);
		jobList.setLayoutAnimation(layoutAnimation);

		jobList.setAdapter(adapter);

		if (jobsResponse != null) {
			adapter.updateJobs(jobsResponse);
			emptyList.setVisibility(View.GONE);
		} else {
			emptyList.setVisibility(View.VISIBLE);
		}

		if (downloadTask.isDownloading()) {
			setSupportProgressBarIndeterminateVisibility(true);
		} else {
			if (jobsResponse == null) {
				downloadJobs();
			} else {
				setSupportProgressBarIndeterminateVisibility(false);
			}
		}

		getSupportActionBar().setHomeButtonEnabled(false);
	}

	private void downloadJobs() {
		setSupportProgressBarIndeterminateVisibility(true);
		downloadTask.downloadJobs();
		invalidateOptionsMenu();
	}

	@AfterViews
	void customActionBarBackgroud() {
		// This is a workaround for http://b.android.com/15340 from
		// http://stackoverflow.com/a/5852198/132047
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_striped_dark);
			background.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			getSupportActionBar().setBackgroundDrawable(background);
		}
	}

	@AfterViews
	void customTitle() {
		TextView titleView = (TextView) View.inflate(this, R.layout.custom_title, null);

		titleView.setText(jobListTitle);

		UiUtils.addTextInnerShadow(titleView);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(titleView);
		actionBar.setDisplayShowCustomEnabled(true);
	}

	public void onJobsDownloaded(JobsResponse result) {
		emptyList.setVisibility(View.GONE);
		Toast.makeText(this, R.string.jobs_updated, Toast.LENGTH_SHORT).show();
		jobsResponse = result;
		adapter.updateJobs(result);
		setSupportProgressBarIndeterminateVisibility(false);
		invalidateOptionsMenu();
		jobList.startLayoutAnimation();
	}

	public void onDownloadError() {
		Toast.makeText(this, R.string.job_download_error, Toast.LENGTH_LONG).show();
		setSupportProgressBarIndeterminateVisibility(false);
		invalidateOptionsMenu();
		if (jobsResponse == null) {
			emptyList.setText(R.string.job_download_error);
		}
	}

	@OptionsItem
	void refreshSelected() {
		downloadJobs();
	}

	@OptionsItem
	void aboutSelected() {
		AboutActivity_.intent(this).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.getItem(0).setVisible(!downloadTask.isDownloading());
		return super.onCreateOptionsMenu(menu);
	}

	@ItemClick
	void jobListItemClicked(Job job) {

		JobDetailActivity_ //
				.intent(this) //
				.job(job) //
				.start();
	}

}