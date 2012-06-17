package info.piwai.expressboard.android;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FromHtml;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.about)
public class AboutActivity extends SherlockActivity {

	@FromHtml
	@ViewById
	TextView aboutText;

	@AfterViews
	void initLayout() {
		aboutText.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
