package info.piwai.expressboard.android.rest.expressboard;

import java.io.Serializable;
import java.util.List;

public class ExpressJobsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public int size;

	public List<ExpressJob> jobs;

}
