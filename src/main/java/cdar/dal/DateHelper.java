package cdar.dal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateHelper {

	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	
	public static String getDate(Date date) {
		df.setTimeZone(tz);
		return df.format(date);
	}
	
	public static Date getDate(String value) throws ParseException {
		return value == null ? null : df.parse(value);
	}
}
