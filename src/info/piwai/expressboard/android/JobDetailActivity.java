package info.piwai.expressboard.android;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_TEXT;
import static info.piwai.expressboard.android.StringUtils.isEmpty;
import info.piwai.expressboard.android.rest.Job;
import info.piwai.expressboard.android.view.EllipsizingTextView;
import info.piwai.expressboard.android.view.EllipsizingTextView.EllipsizeListener;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.HtmlRes;

@EActivity(R.layout.job_detail)
@OptionsMenu(R.menu.job_detail)
public class JobDetailActivity extends SherlockActivity implements EllipsizeListener {

	@Extra
	Job job;

	@HtmlRes
	CharSequence jobDetailTitle;

	@ViewById
	TextView offer;

	@ViewById
	EllipsizingTextView experience;

	@ViewById
	EllipsizingTextView salary;

	@ViewById
	EllipsizingTextView contract;

	@ViewById
	EllipsizingTextView location;

	@ViewById
	TextView company;

	@AfterViews
	void init() {

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		UiUtils.addTextInnerShadow(offer);
		UiUtils.addTextInnerShadow(experience);
		UiUtils.addTextInnerShadow(salary);
		UiUtils.addTextInnerShadow(contract);
		UiUtils.addTextInnerShadow(location);
		UiUtils.addTextInnerShadow(company);

		experience.setEllipsizeListener(this);
		salary.setEllipsizeListener(this);
		contract.setEllipsizeListener(this);
		location.setEllipsizeListener(this);

		company.setText(job.company);

		experience.setText(isEmpty(job.experience) ? getString(R.string.no_experience) : getString(R.string.experience_prefix) + job.experience);
		salary.setText(isEmpty(job.salary) ? getString(R.string.no_salary) : getString(R.string.salary_prefix) + job.salary);
		contract.setText(isEmpty(job.contract) ? getString(R.string.no_contract) : job.contract);
		location.setText(isEmpty(job.city) ? getString(R.string.no_location) : job.city);

		/*
		 * We disable the textviews, they'll be reenabled if their content is
		 * ellipsized.
		 */

		experience.setEnabled(false);
		salary.setEnabled(false);
		contract.setEnabled(false);
		location.setEnabled(false);
	}

	@AfterViews
	void customTitle() {
		TextView titleView = (TextView) View.inflate(this, R.layout.custom_title, null);

		titleView.setText(jobDetailTitle);

		UiUtils.addTextInnerShadow(titleView);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(titleView);
		actionBar.setDisplayShowCustomEnabled(true);

		// This is a workaround for http://b.android.com/15340 from
		// http://stackoverflow.com/a/5852198/132047
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_striped_dark);
			background.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			getSupportActionBar().setBackgroundDrawable(background);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * Sharing the job offer
		 */

		MenuItem actionItem = menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
		actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);

		Intent shareIntent = new Intent(ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(EXTRA_TEXT, getString(R.string.share_job) + job.url);

		actionProvider.setShareIntent(shareIntent);

		return super.onCreateOptionsMenu(menu);
	}

	@OptionsItem
	void homeSelected() {
		finish();
	}

	@Click
	void offerClicked() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.url));
		startActivity(intent);
	}

	@Click
	void companyClicked() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.company_url));
		startActivity(intent);
	}

	@Click
	void experienceClicked() {
		showDetails(R.string.experience, job.experience);
	}

	@Click
	void salaryClicked() {
		showDetails(R.string.salary, job.salary);
	}

	@Click
	void contractClicked() {
		showDetails(R.string.contract, job.contract);
	}

	@Click
	void locationClicked() {
		showDetails(R.string.location, job.city);
	}

	private void showDetails(int titleId, String details) {
		DetailsActivity_ //
				.intent(this) //
				.title(getString(titleId)) //
				.details(details) //
				.start();
	}

	@Override
	public void ellipsizeStateChanged(EllipsizingTextView textView, boolean ellipsized) {
		textView.setEnabled(ellipsized);
	}

}
