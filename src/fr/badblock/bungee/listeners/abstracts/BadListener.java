package fr.badblock.bungee.listeners.abstracts;

import fr.badblock.bungee.BadBungee;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;

public class BadListener implements Listener
{

	@SuppressWarnings("deprecation")
	public BadListener()
	{
		PluginManager pluginManager = BungeeCord.getInstance().getPluginManager();
		pluginManager.registerListener(BadBungee.getInstance(), this);
		BungeeCord.getInstance().getConsole().sendMessage("§e[BadBungee] §aLoaded listener: " + getClass().getSimpleName());
	}
	
}
