package fr.badblock.bungee._plugins.checkLogin;

import fr.badblock.bungee.events.PlayerJoinEvent;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.listeners.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginAlreadyConnectedListener extends BadListener
{
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		BadPlayer badPlayer = event.getBadPlayer();
		String username = badPlayer.getPendingConnection().getName();
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Already connected
		if (bungeeManager.hasUsername(username))
		{
			event.cancel(badPlayer.getTranslatedMessages("bungee.login.alreadyconnected")[0]);
			return;
		}
	}
	
}