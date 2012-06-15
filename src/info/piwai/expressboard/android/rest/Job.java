package info.piwai.expressboard.android.rest;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import android.text.Html;

public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	public String title;

	public String url;

	public String companyUrl;

	public String contract;

	public String company;

	public String city;

	public String area;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public String postedAt;

	public List<String> tags;

	private transient CharSequence tagsAsString;

	public CharSequence getTagsAsString() {
		if (tagsAsString == null) {
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

			tagsAsString = Html.fromHtml(sb.toString());
		}
		return tagsAsString;
	}
}
