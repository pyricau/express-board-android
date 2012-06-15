package info.piwai.expressboard.android;

import info.piwai.expressboard.android.rest.Job;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.job_list_item)
public class JobListItem extends FrameLayout {

	@ViewById
	TextView title;

	@ViewById
	TextView postedAt;

	@ViewById
	TextView company;

	@ViewById
	TextView tags;

	public JobListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public JobListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public JobListItem(Context context) {
		super(context);
	}

	@AfterViews
	void addInnerShadows() {
		UiUtils.addTextInnerShadow(title);
		UiUtils.addTextInnerShadow(company);
	}

	public void update(Job job) {
		title.setText(job.title);
		postedAt.setText(job.postedAt);
		company.setText(job.company);

		tags.setText(job.getTagsAsString());

	}

}
