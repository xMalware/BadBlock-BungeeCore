package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
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
public class LogDisconnectListener extends BadListener {

	/**
	 * When someone connects to a server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(event.getPlayer());

		if (badPlayer == null)
		{
			return;
		}


	}

}