package fr.badblock.bungee._plugins.listeners.logins.loadPlayer;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginLoadPlayerListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onPostLogin(PostLoginEvent event)
	{
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		badPlayer.load(proxiedPlayer);
	}
	
}
