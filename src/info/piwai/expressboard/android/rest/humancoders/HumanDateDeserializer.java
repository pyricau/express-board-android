package info.piwai.expressboard.android.rest.humancoders;

import info.piwai.expressboard.android.BuildConfig;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import android.util.Log;

public class HumanDateDeserializer extends JsonDeserializer<Date> {
	private static final String TAG = HumanDateDeserializer.class.getSimpleName();

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String date = jsonparser.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "Could not parse date", e);
			}
			return null;
		}

	}

}