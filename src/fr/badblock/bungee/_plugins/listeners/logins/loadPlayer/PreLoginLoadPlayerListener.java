package fr.badblock.bungee._plugins.listeners.logins.loadPlayer;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.api.events.PlayerJoinEvent;
import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to load the player's data to the connection and warn other proxies to synchronize the data.
 * It also sends a special event when the player logs in to add actions when a player tries to log in.
 * 
 * @author xMalware
 *
 */
public class PreLoginLoadPlayerListener extends BadListener
{

	/**
	 * When a player joins the server
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPreLogin(PreLoginEvent event)
	{
		// If the connection attempt is cancelled first
		if (event.isCancelled())
		{
			// So we stop there
			return;
		}
		
		// We create a BadPlayer object
        BadPlayer badPlayer = new BadPlayer(event.getConnection());
		
    	// We synchronize all the other proxies
		BungeeTask.keepAlive();
	
		// We send an event that says the player tried to join the server
		ProxyServer.getInstance().getPluginManager().callEvent(new PlayerJoinEvent(badPlayer, event));
	}
	
}
