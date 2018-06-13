package fr.badblock.bungee.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Date utils
 * 
 * @author xMalware
 *
 */
public class DateUtils {

	private static SimpleDateFormat hourDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	private static SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * Get the human date with the hour
	 * @param dateObject
	 * @return
	 */
	public static String getHourDate(Date dateObject) {
		return hourDate.format(dateObject);
	}
	
	/**
	 * Get the human date with the hour
	 * @return
	 */
	public static String getHourDate() {
		return getHourDate(new Date());
	}

	/**
	 * Get the human date
	 * @return
	 */
	public static String getDate(Date dateObject) {
		return date.format(dateObject);
	}

	/**
	 * Get the human date
	 * @return
	 */
	public static String getDate() {
		return getDate(new Date());
	}

}