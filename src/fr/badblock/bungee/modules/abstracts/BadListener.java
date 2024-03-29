package fr.badblock.bungee.modules.abstracts;

import fr.badblock.bungee.BadBungee;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * 
 * Abstract class that lets you listen to BungeeCord events and record them when
 * you instantiate them
 * 
 * @author xMalware
 *
 */
public class BadListener implements Listener {

	/**
	 * Constructor of the BadListener class
	 */
	public BadListener() {
		this(BadBungee.getInstance());
	}

	/**
	 * Constructor of the BadListener class
	 */
	@SuppressWarnings("deprecation")
	public BadListener(Plugin plugin) {
		// We get the BungeeCord plugin manager
		PluginManager pluginManager = BungeeCord.getInstance().getPluginManager();
		// We register the listener
		pluginManager.registerListener(plugin, this);
		// We mark in the logs that the listener is loaded
		BungeeCord.getInstance().getConsole()
				.sendMessage("§e[BadBungee] §aLoaded listener: " + getClass().getSimpleName() + " by " + plugin.getDescription().getName());
	}

}
