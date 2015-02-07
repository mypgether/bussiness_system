package net.bussiness.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppUtils {
	/**
	 * <pre>
	 * Purpose:获取本应用程序的名字
	 * @author Myp
	 * Create Time: 2015-1-22 下午3:41:06
	 * @param context
	 * @return applicationName 应用名称
	 * Version: 1.0
	 * </pre>
	 */
	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}
}
