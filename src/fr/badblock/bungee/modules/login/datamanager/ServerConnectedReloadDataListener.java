package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.time.ThreadRunnable;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to modify in the data of the player the name of
 * the server on which he is, when he moves from server to server.
 * 
 * @author xMalware
 *
 */
public class ServerConnectedReloadDataListener extends BadListener {

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnected(ServerConnectedEvent event) {
		System.out.println(event.getPlayer().getName() + " ServerConnectedReloadDataListener: ServerConnectedEvent");
		// Server
		Server server = event.getServer();

		if (server == null) {
			return;
		}

		ServerInfo serverInfo = server.getInfo();

		if (serverInfo == null) {
			return;
		}

		// We create a new thread
		ThreadRunnable.run(() -> {
			// We get the player
			ProxiedPlayer proxiedPlayer = event.getPlayer();
			// We get the BadPlayer object
			BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
			
			if (badPlayer == null)
			{
				return;
			}
			
			if (serverInfo != null && serverInfo.getName() != null && !serverInfo.getName().startsWith("login"))
			{
				badPlayer.setPassedServer(true);
			}
			
			// Set the last server
			badPlayer.setLastServer(serverInfo.getName());
			// We update the last server in the player data
			badPlayer.updateLastServer(proxiedPlayer, serverInfo.getName());
			
			BadBungee.log("§a<=> " + proxiedPlayer.getName() + " has been moved to " + serverInfo.getName() + ".");
		});
	}

}