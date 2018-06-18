package fr.badblock.bungee.modules.login.antibot;

import java.util.List;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to check if the player has too many accounts
 * connected
 * 
 * @author xMalware
 *
 */
public class TooManyAccountsConnectedListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		// We get his/her IP
		String lastIp = badPlayer.getLastIp();
		// We'll use our bungee manager to determinate if the player is already
		// connected
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Logged players
		List<BadPlayer> loggedPlayers = bungeeManager.getLoggedPlayers(lastIp);
		// Check how many players connected
		if (loggedPlayers.size() > 3) {
			// Kick the players
			loggedPlayers.forEach(loggedPlayer -> loggedPlayer.kick("tooManyAccountsConnected"));
			// Kick the player
			badPlayer.kick("tooManyAccountsConnected");
		}
	}

}