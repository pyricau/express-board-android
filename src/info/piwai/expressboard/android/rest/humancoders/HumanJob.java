package info.piwai.expressboard.android.rest.humancoders;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class HumanJob {

	public String title;

	public String location;

	public String company_name;

	@JsonDeserialize(using = HumanDateDeserializer.class)
	public Date published_at;

	public String job_type;

	public String site;

	public String url;
}
