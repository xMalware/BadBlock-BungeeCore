package fr.badblock.bungee.config;

import java.io.File;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.bungee.BadBungee;

/**
 * 
 * The purpose of this class is to load the BadBlockBungee configuration from a file.
 * 
 * @author xMalware
 *
 */
public class ConfigLoader
{

	/**
	 * Load the configuration
	 * @return serialized BadBungeeConfig object
	 */
	public BadBungeeConfig load()
	{
		// We recover the municipal class to recover data with
		BadBungee badBungee = BadBungee.getInstance();
		// We get the configuration file
		File file = new File(badBungee.getDataFolder(), "config.json");
		// We're trying because we're not sure we can do it
		try
		{
			// We get the contents of the file
			String fileContent = FileUtils.readFile(file);
			// We serialize the configuration
			return badBungee.getGson().fromJson(fileContent, BadBungeeConfig.class);
		}
		// If an error occurs
		catch (Exception exception)
		{
			// We display it in the logs
			exception.printStackTrace();
			// We shut down the server
			System.exit(-1);
			// Return an empty object
			return null;
		}
	}
	
}
