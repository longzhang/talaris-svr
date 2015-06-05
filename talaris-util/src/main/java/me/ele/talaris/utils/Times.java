package me.ele.talaris.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Times {

	private static final SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private static final SimpleDateFormat formatYYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdf_yyyyMMdd_HHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final SimpleDateFormat sdf_HHmm = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat sdf_yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String dateToYYYYMMDD(Date date) {
		if (date == null)
			return "";

		return formatYYYYMMDD.format(date);
	}

	public static Date dateFromYYYYMMDD(String dateStr) {
		try {
			return formatYYYYMMDD.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date dateFromYYYY_MM_DD(String dateStr) {
		try {
			return formatYYYY_MM_DD.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date dateFromYYYYMMDDHHMMSS(String dateStr) {
		try {
			return sdf_yyyyMMdd_HHmmss.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static long utcTimes(Date date) {
		return date.getTime() + Calendar.getInstance().getTimeZone().getRawOffset();
	}

	private static DateFormat cnDateFormat = new DateFormat();

	public static String dateToCNString(Date date) {
		if (date == null)
			return "";
		return cnDateFormat.format(date);
	}

	public static Date dateFromCNString(String cnStr) throws ParseException {
		return cnDateFormat.parse(cnStr);
	}

	public static String currentYYYYMMDD_HHMMSS_SSS() {
		return sdf_yyyyMMdd_HHmmssSSS.format(new Date());
	}

	public static int toSecondFromHHMMSS(String HHMMSS) {
		String[] h_m_s = HHMMSS.split(":");
		return (Integer.parseInt(h_m_s[0]) * 60 + Integer.parseInt(h_m_s[1])) * 60 + Integer.parseInt(h_m_s[2]);
	}

	public static int toSecondFromHHMM(String HHMM) {
		String[] h_m = HHMM.split(":");
		return (Integer.parseInt(h_m[0]) * 60 + Integer.parseInt(h_m[1])) * 60;
	}

	public static String toHHMMFromSecond(int second) {
		if (second >= ONE_DAY) {
			second -= ONE_DAY;
		}
		return String.format("%02d:%02d", second / ONE_HOUR, (second % ONE_HOUR) / 60);
	}

	public static int retrieveMonth(long ms) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ms);
		return c.get(Calendar.MONTH);
	}

	public static final int ONE_HOUR = 60 * 60;
	public static final int SIX_HOUR = 6 * ONE_HOUR;

	public static final int ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 获得刚刚过去的一个完整的月份，如果当月已经完全过去，则返回当月，否则返回上月。
	 * 
	 * @param last_passed_day
	 *            刚刚过去的一天
	 * @return
	 */
	public static Pair<Date, Date> getPassedFullMonth(Date last_passed_day) {
		Calendar current = Calendar.getInstance();
		current.setTime(last_passed_day);

		Calendar next = Calendar.getInstance();
		next.setTimeInMillis(last_passed_day.getTime() + ONE_DAY * 1000);

		if (next.get(Calendar.MONTH) != current.get(Calendar.MONTH)) {
			Date month_begin_date = dateFromYYYYMMDD(dateToYYYYMMDD(last_passed_day).substring(0, 6) + "01");
			return new Pair<Date, Date>(month_begin_date, last_passed_day);
		} else {
			Date month_end_date = new Date(last_passed_day.getTime() - ONE_DAY * 1000 * current.get(Calendar.DATE));
			Date month_begin_date = dateFromYYYYMMDD(dateToYYYYMMDD(month_end_date).substring(0, 6) + "01");
			return new Pair<Date, Date>(month_begin_date, month_end_date);
		}

	}

	public static final Map<Integer, String> WeekNameMap = Collections
			.unmodifiableMap(new LinkedHashMap<Integer, String>() {
				private static final long serialVersionUID = 1L;

				{
					put(Calendar.SUNDAY, "星期日");
					put(Calendar.MONDAY, "星期一");
					put(Calendar.TUESDAY, "星期二");
					put(Calendar.WEDNESDAY, "星期三");
					put(Calendar.THURSDAY, "星期四");
					put(Calendar.FRIDAY, "星期五");
					put(Calendar.SATURDAY, "星期六");
				}
			});

	public static final long ONE_YEAR_MILLIS = 365 * ONE_DAY * 1000;

	/**
	 * 获得中文星期几
	 * 
	 * @param dateFromYYYYMMDD
	 * @return
	 */
	public static String retrieveWeekCN(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week = c.get(Calendar.DAY_OF_WEEK);
		return WeekNameMap.get(week);
	}

	/**
	 * 
	 * @return YYYY-MM-DD
	 */
	public static String currentYYYY_MM_DD() {
		return toYYYY_MM_DD(new Date());
	}

	public static String toYYYY_MM_DD(Date date) {
		return formatYYYY_MM_DD.format(date);
	}

	public static String plusDaysOnYYYY_MM_DD(Date date, int days) {
		return formatYYYY_MM_DD.format(date.getTime() + days * 24 * 60 * 60 * 1000);
	}

	public static Date plusDaysOnDate(Date date, int days) {
		return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
	}

	public static String minusDaysOnYYYY_MM_DD(Date date, int days) {
		return formatYYYY_MM_DD.format(date.getTime() + days * 24 * 60 * 60 * 1000);
	}

	public static Date minusDaysOnDate(Date date, int days) {
		return new Date(date.getTime() - days * 24 * 60 * 60 * 1000);
	}

	/**
	 * checkin 方法使用
	 * 
	 * @param dateTime
	 * @param isToday
	 * @return
	 */
	public static String beautifyTime(Timestamp dateTime, boolean isToday) {
		if (isToday) {
			return toHHMMSS(dateTime);
		} else {
			return toYYYY_MM_DD_HHMMSS(dateTime);
		}
	}

	private static String toHHMMSS(Timestamp dateTime) {
		return sdf_HHmm.format(dateTime);
	}

	public static String toYYYY_MM_DD_HHMMSS(Date date) {
		return sdf_yyyyMMdd_HHmmss.format(date);
	}

	/**
	 * Timestamp加分钟 Date是Timestamp父类
	 * 
	 * @param originTime
	 * @return
	 */
	public static Timestamp timestampPlusMiniuts(Timestamp originTime, int addMiniutes) {
		long time = (originTime.getTime() + addMiniutes * 60 * 1000);
		return new Timestamp(time);

	}

	/**
	 * Timestamp加天 Date是Timestamp父类
	 * 
	 * @param originTime
	 * @return
	 */
	public static Timestamp timestampPlusDays(int days) {
		Date date = new Date();
		Timestamp nextTime = new Timestamp(date.getTime());
		Calendar cc = Calendar.getInstance();
		cc.setTime(nextTime);
		cc.add(Calendar.DAY_OF_MONTH, days);
		return new Timestamp(cc.getTimeInMillis());
	}
}
