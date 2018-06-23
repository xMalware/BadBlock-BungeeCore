package fr.badblock.bungee.modules.login.datamanager;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.commands.login.HashLogin;
import fr.badblock.bungee.modules.login.events.PlayerCreatedEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class SendLoginMessageListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCreated(PlayerCreatedEvent event) {

		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();

		if (badPlayer.isOnlineMode()) {
			HashLogin.log(badPlayer);
			return;
		}

		HashLogin.sendLoginMessage(badPlayer);
	}

}
