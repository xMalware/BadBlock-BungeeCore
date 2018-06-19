package fr.badblock.bungee.modules.login.datamanager;

import java.lang.reflect.Field;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 *
 * @author xMalware
 *
 */
public class SetUniqueIdListener extends BadListener {

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onServerConnect(ServerConnectEvent event) {
		// We get the player
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		try {
			PendingConnection pendingConnection = proxiedPlayer.getPendingConnection();
			Field uniqueId = pendingConnection.getClass().getDeclaredField("uniqueId");
			uniqueId.setAccessible(true);
			uniqueId.set(pendingConnection, badPlayer.getUniqueId());
		}catch(Exception error) {
			error.printStackTrace();
		}
	}

}