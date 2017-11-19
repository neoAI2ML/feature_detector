package tomketao.featuredetector.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author tketao
 * 
 */
public class CommonUtils {
	/**
	 * Checks if provided date is before current date
	 * @param date Date in String format
	 * @param dateFormat Date format
	 * @return returns true is given date is before current date, otherwise returns false
	 */
	public static boolean isBeforeDate(String date, String dateFormat) {
		
		if(StringUtils.isBlank(date)) {
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date dateObj = new Date();
		try {
			dateObj = format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		
		Date currentDate = new Date();
		if(dateObj.before(currentDate)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if date1 is before than date2.
	 * if equals it returns false.
	 * 
	 * @param date1 - Should be in given dateFormat param.
	 * @param date2 - Should be in given dateFormat param.
	 * @param dateFormat - format of date like YYYYMMdd.
	 * @return returns true if date1 is before date2, otherwise returns false
	 */
	public static boolean isBeforeDate(String date1, String date2, String dateFormat) {

		if (StringUtils.isBlank(date1) || StringUtils.isBlank(date2)) {
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date dateObj1 = new Date();
		Date dateObj2 = new Date();
		try {
			dateObj1 = format.parse(date1);
			dateObj2 = format.parse(date2);
		} catch (ParseException e) {
			return false;
		}

		if (dateObj1.before(dateObj2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if provided date is after current date
	 * @param date Date in String format
	 * @param dateFormat Date format
	 * @return returns true is given date is after current date, otherwise returns false
	 */
	public static boolean isAfterDate(String date, String dateFormat) {
		
		if(StringUtils.isBlank(date)) {
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date dateObj = new Date();
		try {
			dateObj = format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		
		Date currentDate = new Date();
		if(dateObj.after(currentDate)) {
			return true;
		}
		return false;
	}
	
	public static String decodeISO_8859_1AndUtf_8Format(String requestString) {
		try {
			byte[] utf8 = new String(requestString.getBytes(), "ISO-8859-1").getBytes("UTF-8");
			return new String(utf8, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return requestString;
		}
	}
}
