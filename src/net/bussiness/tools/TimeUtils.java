package net.bussiness.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATETIME_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM HH:mm");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String[] WEEK = { "周一", "周二", "周三", "周四", "周五", "周六",
			"周日" };

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 使用预设格式将字符串转为Date
	 * 
	 * @throws ParseException
	 */
	public static Date parseStringInDate(String strDate) throws ParseException {
		return StringUtils.isBlank(strDate) ? null : parseStringInDate(strDate,
				DEFAULT_DATE_PATTERN);
	}

	/**
	 * 使用参数Format将字符串转为Date
	 * 
	 * @throws ParseException
	 */
	public static Date parseStringInDate(String strDate, String pattern)
			throws ParseException {
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}

	/**
	 * <pre>
	 * Purpose:判断是不是这个星期
	 * @author Myp
	 * Create Time: 2015-1-16 下午3:59:32
	 * @param date
	 * @return
	 * </pre>
	 */
	public static boolean isThisWeek(Date date) {
		Calendar input = Calendar.getInstance();
		input.setTime(date);
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		return input.get(Calendar.WEEK_OF_MONTH) == now
				.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param date
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static String getDayForWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 1;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return WEEK[dayForWeek - 1];
	}
}
