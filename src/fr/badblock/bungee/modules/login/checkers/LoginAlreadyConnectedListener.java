package fr.badblock.bungee.modules.login.checkers;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadOfflinePlayer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to check if the player is already connected to
 * the server.
 * 
 * @author xMalware
 *
 */
public class LoginAlreadyConnectedListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(PreLoginEvent event) {
		if (event.isCancelled())
		{
			return;
		}
		// We get the PendingConnection object
		PendingConnection connection = event.getConnection();
		// We get the BadOfflinePlayer object
		BadOfflinePlayer badOfflinePlayer = BadOfflinePlayer.get(connection.getName());
		// We'll use our bungee manager to determinate if the player is already
		// connected
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// If the username is already registered in the sync data
		if (bungeeManager.hasUsername(connection.getName())) {
			// We cancel the player connection with a specific message
			event.setCancelled(true);
			event.setCancelReason(StringUtils.join(badOfflinePlayer.getTranslatedMessages("bungee.login.alreadyconnected", null), System.lineSeparator()));
		}
	}

}