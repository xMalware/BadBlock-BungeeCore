package fr.badblock.bungee._plugins.listeners.serverConnect;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.ThreadRunnable;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to modify in the data of the player the name
 * of the server on which he is, when he moves from server to server.
 * 
 * @author xMalware
 *
 */
public class ServerConnectedReloadDataListener extends BadListener
{

	/**
	 * When someone connects to a server
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOW)
	public void onServerConnected(ServerConnectedEvent event)
	{
		// We create a new thread
        ThreadRunnable.run(() ->
        {
        	// We get the player
            ProxiedPlayer proxiedPlayer = event.getPlayer();
            // We get the BadPlayer object
            BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
            // We update the last server in the player data
            badPlayer.updateLastServer(proxiedPlayer);
        });
    }

}