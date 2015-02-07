package net.bussiness.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public class VibrateUtils {
	public static final int SHORT_TIME = 40;
	public static final int MEDIUM_TIME = 80;
	public static final int LONG_TIME = 120;

	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public static void Vibrate(final Activity activity, long[] pattern,
			boolean isRepeat) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
}
