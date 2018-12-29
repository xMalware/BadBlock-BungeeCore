package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.chat.TabCompleteListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class ServerConnectSendDataListener extends BadListener {

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnect(ServerConnectEvent event) {
		if (event.isCancelled()) {
			return;
		}
		// We get the player
		ProxiedPlayer proxiedPlayer = event.getPlayer();
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer == null) {
			return;
		}

		if (!badPlayer.isLoginStepOk())
		{
			return;
		}
		
		TabCompleteListener.put(proxiedPlayer.getName());
		badPlayer.sendDataToBukkit(event.getTarget().getName());
	}

}