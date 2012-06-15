package info.piwai.expressboard.android.rest;

import java.io.Serializable;
import java.util.List;

public class JobsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public int size;

	public List<Job> jobs;

}
