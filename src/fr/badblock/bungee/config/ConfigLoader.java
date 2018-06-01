package fr.badblock.bungee.config;

import java.io.File;

import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.bungee.BadBungee;

public class ConfigLoader
{

	public ConfigLoader()
	{
	}
	
	public BadBungeeConfig load()
	{
		BadBungee badBungee = BadBungee.getInstance();
		File file = new File(badBungee.getDataFolder(), "config.json");
		try
		{
			String fileContent = FileUtils.readFile(file);
			return badBungee.getGson().fromJson(fileContent, BadBungeeConfig.class);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
			return null;
		}
	}
	
}
