package fr.badblock.bungee.listeners;

import fr.badblock.bungee.events.PlayerJoinEvent;
import fr.badblock.bungee.link.bungee.BungeeTask;
import fr.badblock.bungee.listeners.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PlayerJoinListener extends BadListener
{

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onLogin(PreLoginEvent event)
	{
		BadPlayer badPlayer = new BadPlayer(event, event.getConnection());
		// Call event
		ProxyServer.getInstance().getPluginManager().callEvent(new PlayerJoinEvent(badPlayer));
		// Cancelled
		if (event.isCancelled())
		{
			return;
		}
		// KeepAlive as last action
		BungeeTask.keepAlive();
	}
	
}
