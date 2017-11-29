package fr.badblock.bungee._plugins.listeners.logins.checkLogin;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.api.events.PlayerJoinEvent;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginFullServerListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		BadPlayer badPlayer = event.getBadPlayer();
		BungeeManager bungeeManager = BungeeManager.getInstance();
		if (bungeeManager.getOnlinePlayers() > bungeeManager.getSlots())
		{
			// Permission check
			event.cancel(badPlayer.getTranslatedMessages("bungee.login.full")[0]);
			return;
		}
	}
	
}