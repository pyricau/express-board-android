package info.piwai.expressboard.android;

import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.details)
public class DetailsActivity extends SherlockActivity {

	@Extra
	String title;

	@Extra
	String details;

	@ViewById
	TextView detailsText;

	@AfterViews
	void initLayout() {
		setTitle(title);
		detailsText.setText(details);
	}

}
