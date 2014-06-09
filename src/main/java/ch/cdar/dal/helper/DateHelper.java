package ch.cdar.dal.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The Class DateHelper.
 */
public class DateHelper {
	/** The timezone. */
	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	
	/** The dateformat. */
	private static DateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssX");

	/**
	 * Gets the date.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static String getDate(Date date) {
		df.setTimeZone(tz);
		return df.format(date);
	}

	/**
	 * Gets the date.
	 *
	 * @param value the value
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	public static Date getDate(String value) throws ParseException {
		try {
			return df.parse(value);
		} catch (Exception ex) {
			return null;
		}
	}
}
