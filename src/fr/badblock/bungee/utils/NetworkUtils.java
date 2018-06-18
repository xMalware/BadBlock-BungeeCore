package fr.badblock.bungee.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

/**
 * 
 * Network utils
 * 
 * @author xMalware
 *
 */
public class NetworkUtils {

	/**
	 * Fetch the source code
	 * 
	 * @param link
	 * @return
	 */
	public static String fetchSourceCode(String link) {
		// Try to
		try {
			// Set the user agent
			System.setProperty("http.agent", "Mozilla/5.0");
			// Create url
			URL url = new URL(link);
			// Do the connection
			URLConnection con = url.openConnection();
			// Set the user agent
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			// Get input stream
			InputStream in = con.getInputStream();
			// To string
			String body = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			// Returns the source code
			return body;
		}
		// Error case
		catch (Exception error) {
			// Print stack trace
			error.printStackTrace();
			// Returns 0 (I don't remember why)
			return "0";
		}
	}

	/**
	 * Fetch source code with API
	 * 
	 * @param link
	 * @param apiKey
	 * @return
	 */
	public static String fetchSourceCodeWithAPI(String link, String apiKey) {
		// Try to
		try {
			// Set the user agent
			System.setProperty("http.agent", "Mozilla/5.0");
			// Create url
			URL url = new URL(link);
			// Do the connection
			URLConnection con = url.openConnection();
			// Set the user agent
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			// Set API key
			con.setRequestProperty("X-Key", apiKey);
			// Get input stream
			InputStream in = con.getInputStream();
			// To string
			String body = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			// Returns the source code
			return body;
		}
		// Error case
		catch (Exception error) {
			// Print the error
			error.printStackTrace();
			System.out.println("Error: " + error.getMessage());
			// Returns 0 (I don't remember why)
			return "0";
		}
	}

}