package fr.badblock.bungee.modules.login.checkers;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
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
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		// We get his/her username
		String username = badPlayer.getName();
		// We'll use our bungee manager to determinate if the player is already
		// connected
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// If the username is already registered in the sync data
		if (bungeeManager.hasUsername(username)) {
			// We cancel the player connection with a specific message
			event.cancel(badPlayer.getTranslatedMessage("bungee.login.alreadyconnected", null));
		}
	}

}