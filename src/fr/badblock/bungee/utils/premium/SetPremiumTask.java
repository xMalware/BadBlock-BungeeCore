package fr.badblock.bungee.utils.premium;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.bungee.players.BadOfflinePlayer;

class SetPremiumTask implements Callable<String> {

	private static String[] urls = new String[]
			{
					"https://mcapi.de/api/user/"
			};

	private BadOfflinePlayer player;
	private static long		reset = System.currentTimeMillis() + 3600_000L;
	private static Map<String, Boolean> premiums = new HashMap<>();

	public SetPremiumTask(BadOfflinePlayer player)
	{
		this.player = player;
	}

	@Override
	public String call() throws Exception {
		assert player != null;

		if (reset > System.currentTimeMillis())
		{
			reset = System.currentTimeMillis() + 3600_000L;
			premiums.clear();
		}

		try
		{

			if (player.isOnlineMode())
			{
				return null;
			}

			boolean premium = false;
			String name = player.getName().toLowerCase();

			if (premiums.containsKey(name))
			{
				premium = premiums.get(name);
			}
			else
			{
				String data = fetchSourceCode(urls[new Random().nextInt(urls.length)] + name);
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(data);

				if (!jsonElement.isJsonObject())
				{
					return null;
				}

				JsonObject jsonObject = jsonElement.getAsJsonObject();

				if (!jsonObject.has("premium"))
				{
					return null;
				}

				premium = jsonObject.get("premium").getAsBoolean();
				if (!premium)
				{
					return null;
				}
				
				premiums.put(name, premium);
			}
			
			if (premium)
			{
				player.setOnlineMode(premium);
				player.saveData();
			}

			return "done";
		}
		catch (Exception error)
		{
			return null;
		}
	}

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
			// Returns 0 (I don't remember why)
			return "{}";
		}
	}

}