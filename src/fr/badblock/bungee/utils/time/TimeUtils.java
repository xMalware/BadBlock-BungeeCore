package fr.badblock.bungee.utils.time;

/**
 * 
 * TimeUtils
 * 
 * @author xMalware
 *
 */
public class TimeUtils {

	/**
	 * If something is expired
	 * 
	 * @param timestamp
	 * @return yes or no something is expired
	 */
	public static boolean isExpired(long time) {
		// time() > given time
		return time() > time;
	}

	/**
	 * If something is valid
	 * 
	 * @param time
	 * @return yes/no something stills valid
	 */
	public static boolean isValid(long time) {
		// Check if it's not expired, that's easy
		return !isExpired(time);
	}

	/**
	 * Get the next time with milliseconds adder
	 * 
	 * @param milliseconds
	 * @return the next time
	 */
	public static long nextTime(long milliseconds) {
		// Add a time
		return time() + milliseconds;
	}

	/**
	 * Get the next time with seconds adder
	 * 
	 * @param seconds
	 * @return the next time
	 */
	public static long nextTimeWithSeconds(long seconds) {
		// Use with a multiplicator
		return nextTime(seconds * 1000);
	}

	/**
	 * Sleep
	 * 
	 * @param Time
	 *            (ms)
	 */
	public static void sleep(long time) {
		// So we try
		try {
			// To sleep
			Thread.sleep(time);
		}
		// If something gone wrong
		catch (InterruptedException exception) {
			// We print the stack trace
			exception.printStackTrace();
		}
	}

	/**
	 * Sleep in seconds
	 * 
	 * @param Time
	 *            (seconds)
	 */
	public static void sleepInSeconds(long time) {
		// Sleep with * 1000
		sleep(time * 1_000);
	}

	/**
	 * Returns the current timestamp
	 * 
	 * @return the current timestamp
	 */
	public static long time() {
		// Get the timestamp
		return System.currentTimeMillis();
	}

}
