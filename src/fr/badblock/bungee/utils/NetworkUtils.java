package fr.badblock.bungee.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class NetworkUtils {

	public static String fetchSourceCode(String link) {
		try {
			System.setProperty("http.agent", "Mozilla/5.0");
			URL url = new URL(link);
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			InputStream in = con.getInputStream();
			String body = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			return body;
		} catch (Exception error) {
			error.printStackTrace();
			return "0";
		}
	}

	public static String fetchSourceCodeWithAPI(String link, String apiKey) {
		try {
			System.setProperty("http.agent", "Mozilla/5.0");
			URL url = new URL(link);
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("X-Key", apiKey);
			InputStream in = con.getInputStream();
			String body = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
			return body;
		} catch (Exception error) {
			error.printStackTrace();
			return "0";
		}
	}

}