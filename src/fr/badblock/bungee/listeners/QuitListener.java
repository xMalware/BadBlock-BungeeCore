package fr.badblock.bungee.listeners;

import fr.badblock.bungee.listeners.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class QuitListener extends BadListener
{

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		BadPlayer badPlayer = BadPlayer.get(player);
		if (badPlayer == null)
		{
			return;
		}
		badPlayer.remove();
	}
	
}
