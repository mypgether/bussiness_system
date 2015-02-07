package net.bussiness.tools;

import net.bussiness.activities.R;
import net.bussiness.module.base.NavActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtils {
	private static NotificationManager mNotificationManager = null;

	/**
	 * <pre>
	 * Purpose:显示通知
	 * @author Myp
	 * Create Time: 2015-1-22 下午3:30:29
	 * @param context
	 * @param title 通知标题
	 * @param text 通知内容
	 * @param notificationId 指定通知ID
	 * Version: 1.0
	 * </pre>
	 */
	public static void showNotification(Context context, String title,
			String text, int notificationId) {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				text, System.currentTimeMillis());
		// 填充Notification的各个属性：
		Intent notificationIntent = new Intent(context, NavActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, text, contentIntent);

		// Notification提供了丰富的手机提示方式：
		// a)在状态栏(Status Bar)显示的通知文本提示，如：
		notification.tickerText = text;

		// b)发出提示音，如：
		notification.defaults |= Notification.DEFAULT_SOUND;
		// notification.sound = Uri
		// .parse("file:///sdcard/notification/ringer.mp3");
		// notification.sound = Uri.withAppendedPath(
		// Audio.Media.INTERNAL_CONTENT_URI, "6");

		// c)手机振动，如：
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;

		// d)LED灯闪烁，如：
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// Notification像Android QQ一样能出现在 “正在运行的”栏目下面
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// 4）发送通知：
		mNotificationManager.notify(notificationId, notification);
	}

	/**
	 * <pre>
	 * Purpose:清除通知
	 * @author Myp
	 * Create Time: 2015-1-22 下午3:30:01
	 * @param context
	 * @param notificationId 通知ID
	 * Version: 1.0
	 * </pre>
	 */
	public static void clearNotification(Context context, int notificationId) {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(notificationId);
	}
}
