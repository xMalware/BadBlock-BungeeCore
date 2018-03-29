package fr.badblock.bungee._plugins.listeners.serverConnect;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.ThreadRunnable;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerConnectedReloadDataListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onServerConnected(ServerConnectedEvent event)
	{
        ThreadRunnable.run(() ->
        {
            ProxiedPlayer proxiedPlayer = event.getPlayer();
            BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
            badPlayer.updateLastServer(proxiedPlayer);
        });
    }

}