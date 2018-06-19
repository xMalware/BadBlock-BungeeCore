package fr.badblock.bungee.modules.login.checkers;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to check if the server is full when a player
 * joins
 * 
 * @author xMalware
 *
 */
public class LoginFullServerListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (event.isCancelled())
		{
			return;
		}
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		// We'll use our bungee manager to determinate if the network is full
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// If the player has a bypass to join the network even if it is full
		if (badPlayer.hasPermission("bungee.bypass.full")) {
			// So we stop there
			return;
		}
		// If the number of players connected is equal to or greater than the number of
		// slots of the network
		if (bungeeManager.getOnlinePlayers() >= bungeeManager.getSlots()) {
			// We cancel his connection to the server
			event.cancel(StringUtils.join(badPlayer.getTranslatedMessages("bungee.login.full", null), System.lineSeparator()));
		}
	}

}