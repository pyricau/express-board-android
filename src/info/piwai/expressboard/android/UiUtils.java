package info.piwai.expressboard.android;

import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.widget.TextView;

public class UiUtils {

	/**
	 * The textView must have <code>android:layerType="software"</code>
	 */
	public static void addTextInnerShadow(TextView textView) {
		/*
		 * Trick for inner shadow, see here:
		 * http://wiresareobsolete.com/wordpress/2012/04/textview-inner-shadows/
		 */
		float[] direction = new float[] { 0f, -1.0f, 0.5f };
		MaskFilter filter = new EmbossMaskFilter(direction, 0.8f, 15f, 1f);
		textView.getPaint().setMaskFilter(filter);
	}

}
