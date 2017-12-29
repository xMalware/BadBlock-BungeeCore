package fr.badblock.bungee._plugins.listeners.serverConnect;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.toenga.common.utils.data.Callback;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectedReloadDataListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onServerConnected(ServerConnectedEvent event)
	{
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		badPlayer.registerLoadedCallback(new Callback<BadPlayer>()
		{
			@Override
			public void done(BadPlayer result, Throwable error) {
				badPlayer.updateLastServer(proxiedPlayer);	
			}
		});
	}

}