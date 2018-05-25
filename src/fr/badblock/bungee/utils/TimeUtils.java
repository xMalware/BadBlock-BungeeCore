package fr.badblock.bungee.utils;

public class TimeUtils
{

	public static boolean isExpired(long time)
	{
		return time() > time;
	}

	public static boolean isValid(long time)
	{
		return !isExpired(time);
	}
	
	public static long nextTime(long milliseconds)
	{
		return time() + milliseconds;
	}
	
	public static long nextTimeWithSeconds(long seconds)
	{
		return nextTime(seconds * 1000);
	}
	
	public static long time()
	{
		return System.currentTimeMillis();
	}
	
}
