package info.piwai.expressboard.android.rest;

import info.piwai.expressboard.android.BuildConfig;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.ocpsoft.pretty.time.PrettyTime;

import android.util.Log;

public class CustomJsonDateDeserializer extends JsonDeserializer<String> {
	private static final String TAG = CustomJsonDateDeserializer.class.getSimpleName();

	private final PrettyTime prettyTime = new PrettyTime();

	@Override
	public String deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String date = jsonparser.getText();
		try {
			return prettyTime.format(format.parse(date));
		} catch (ParseException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Could not parse date", e);
			}
			return "";
		}

	}

}