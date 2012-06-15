package info.piwai.expressboard.android;

import info.piwai.expressboard.android.rest.Job;
import info.piwai.expressboard.android.rest.JobsResponse;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

@EBean
public class JobAdapter extends BaseAdapter {

	@RootContext
	Context context;

	private JobsResponse jobsResponse;

	@Override
	public int getCount() {
		return jobsResponse == null ? 0 : jobsResponse.jobs.size();
	}

	@Override
	public Job getItem(int position) {
		return jobsResponse.jobs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		JobListItem jobItemView;
		if (convertView == null) {
			jobItemView = JobListItem_.build(context);
		} else {
			jobItemView = (JobListItem) convertView;
		}

		Job job = getItem(position);

		jobItemView.update(job);

		return jobItemView;
	}

	public void updateJobs(JobsResponse jobsResponse) {
		this.jobsResponse = jobsResponse;
		notifyDataSetChanged();
	}

}
