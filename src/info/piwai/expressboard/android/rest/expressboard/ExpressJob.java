package info.piwai.expressboard.android.rest.expressboard;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class ExpressJob {

	public String title;

	public String url;

	public String company_url;

	public String contract_code;

	public String contract;

	public String company;

	public String city;

	public String area;

	public String salary;

	public String experience;

	public String instructions;

	@JsonDeserialize(using = ExpressDateDeserializer.class)
	public Date postedAt;

	public List<String> tags;

}
