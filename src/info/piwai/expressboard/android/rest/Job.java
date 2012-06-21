package info.piwai.expressboard.android.rest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.text.Html;

public class Job implements Serializable, Comparable<Job> {
	private static final long serialVersionUID = 1L;

	public String title;

	public String url;

	public String companyUrl;

	public String contract;

	public String company;

	public String location;

	public Date publishedAtdate;

	public String publishedAtString;

	public String salary;

	public String experience;

	public List<String> tags;

	private transient CharSequence tagsAsHtml;

	@Override
	public int compareTo(Job another) {
		return another.publishedAtdate.compareTo(publishedAtdate);
	}

	public CharSequence tagsAsHtml() {
		if (tagsAsHtml == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (String tag : tags) {
				if (first) {
					first = false;
				} else {
					sb.append(" - ");
				}
				sb.append("<font color=\"#996e44\">");
				sb.append(tag);
				sb.append("</font>");
			}
			tagsAsHtml = Html.fromHtml(sb.toString());
		}
		return tagsAsHtml;
	}

}
