package fr.badblock.bungee.modules.punishments;

import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadIP;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to block players from logging in if they are
 * banned
 * 
 * @author xMalware
 *
 */
public class PunishmentBannedIPListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		// Getting the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		BadIP badIp = BadIP.get(badPlayer.getLastIp());
		// Getting punishment information
		Punished punished = badIp.getPunished();

		// If there is no information of punishments
		if (punished == null) {
			// So we stop there
			return;
		}

		// If the player is still banned
		if (punished.isBan()) {
			// We cancel the player's connection with the ban message
			event.cancel(badIp.getBanIpMessage(badPlayer));
		}
	}

}