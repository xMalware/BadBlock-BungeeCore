package fr.badblock.bungee._plugins.checkLogin;

import fr.badblock.bungee.events.PlayerJoinEvent;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.listeners.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginFullServerListener extends BadListener
{

	@EventHandler (priority = EventPriority.HIGHEST)
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