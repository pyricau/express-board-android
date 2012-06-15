package info.piwai.expressboard.android;

import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.res.HtmlRes;

@EActivity(R.layout.main)
public class JobListActivity extends SherlockActivity {

	@HtmlRes
	CharSequence jobListTitle;

	@AfterViews
	void customActionBarBackgroud() {
		// This is a workaround for http://b.android.com/15340 from
		// http://stackoverflow.com/a/5852198/132047
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_striped);
			background.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			getSupportActionBar().setBackgroundDrawable(background);
		}
	}

	@AfterViews
	void customTitle() {
		TextView titleView = (TextView) View.inflate(this, R.layout.job_list_title, null);

		titleView.setText(jobListTitle);

		float[] direction = new float[] { 0f, -1.0f, 0.5f };
		MaskFilter filter = new EmbossMaskFilter(direction, 0.8f, 15f, 1f);
		titleView.getPaint().setMaskFilter(filter);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(titleView);
		actionBar.setDisplayShowCustomEnabled(true);
	}
}