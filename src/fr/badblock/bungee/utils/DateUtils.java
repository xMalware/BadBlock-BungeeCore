package fr.badblock.bungee.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{

	private static SimpleDateFormat hourDate	= new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
	private static SimpleDateFormat date		= new SimpleDateFormat("dd-MM-yyyy");
	
	public static String getHourDate(Date dateObject)
	{
		return hourDate.format(dateObject);
	}
	
	public static String getHourDate()
	{
		return getHourDate(new Date());
	}
	
	public static String getDate(Date dateObject)
	{
		return date.format(dateObject);
	}
	
	public static String getDate()
	{
		return getDate(new Date());
	}
	
}