package com.scchuangtou.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {

	public final static String FORMAT_H24MM = "HH:mm";

	public final static String FORMAT_YYYYMM = "yyyyMM";

	public final static String FORMAT_YYYYMMDD = "yyyy-MM-dd";

	public final static String FORMAT_YYYYMMDDmm = "yyyy-MM-dd HH:mm";

	public final static String FORMAT_YYYYMMDDH24MM = "yyyy-MM-dd HH:mm:ss";

	public final static String FORMAT_MMDDHHMMSS = "MM-dd HH:mm:ss";

	public final static String DIFF_TYPE_DAY = "day";

	public final static String DIFF_TYPE_YEAR = "year";

	public final static long DAY_TIME = 24 * 60 * 60 * 1000l;

	public static String formatDate(Date date, String pattern) {
		return formatDate(date.getTime(), pattern);
	}

	public static String formatDate(long time, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return format.format(calendar.getTime());
	}
	
	public static long formatDate(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date=sdf.parse(dateStr);
		return date.getTime();
	}

	/**
	 * 获取当月第一天凌晨时间
	 * 
	 * @param time
	 * @return
	 */
	public static long getFirstMonthDayTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getDayTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getDayHourTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getDayEndTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取年龄或者出生天数
	 * 
	 * @param IDCardNum
	 * @param type
	 *            day天数, year年龄
	 * @return
	 */
	public static int getAge(String IDCardNum, String type) {
		int year = 0, month = 0, day = 0, idLength = IDCardNum.length();
		Calendar cal1 = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		if (!IDCardUtil.validate(IDCardNum))
			return -1;
		if (idLength == 18) {
			year = Integer.parseInt(IDCardNum.substring(6, 10));
			month = Integer.parseInt(IDCardNum.substring(10, 12));
			day = Integer.parseInt(IDCardNum.substring(12, 14));
		} else if (idLength == 15) {
			year = Integer.parseInt(IDCardNum.substring(6, 8)) + 1900;
			month = Integer.parseInt(IDCardNum.substring(8, 10));
			day = Integer.parseInt(IDCardNum.substring(10, 12));
		}
		cal1.set(year, month, day);
		if (type.equals(DIFF_TYPE_YEAR))
			return getYearDiff(today, cal1);
		else if (type.equals(DIFF_TYPE_DAY))
			return getDayDiff(today, cal1);
		return -1;
	}

	private static int getYearDiff(Calendar cal, Calendar cal1) {
		int m = (cal.get(Calendar.MONTH)) - (cal1.get(Calendar.MONTH));
		int y = (cal.get(Calendar.YEAR)) - (cal1.get(Calendar.YEAR));
		return (y * 12 + m) / 12;
	}

	private static int getDayDiff(Calendar cal, Calendar cal1) {
		return (int) ((cal.getTime().getTime() - cal1.getTime().getTime()) / DAY_TIME);
	}

	/**
	 * 周一到周日的最后时间
	 * @param n
	 * @return
	 */
	public static List<String> getLastWeekDays(int n) {
		List<String> time=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		cal.add(Calendar.DATE, n * 7);
		// 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String monday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		time.add(monday);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String thursday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		time.add(thursday);
		return time;
	}

	public static List<String> findDates(String dBegin, String dEnd) throws ParseException {
		List<String> lDate = new ArrayList<String>();
		lDate.add(dBegin);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calBegin = Calendar.getInstance();
		Date begin=sdf.parse(dBegin);
		Date end=sdf.parse(dEnd);
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(begin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(end);
		// 测试此日期是否在指定日期之后
		while (end.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(sdf.format(calBegin.getTime()));
		}
		return lDate;
	}
	
//	public static void main(String[] args) throws Exception {
//		List<String> mondayThursday=getLastWeekDays(-1);
//		System.out.println(mondayThursday);
//		List<String> lDate = findDates(mondayThursday.get(0), mondayThursday.get(1));
//		for (String date : lDate) {
//			System.out.println(date);
//		}
//	}
}
