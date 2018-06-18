package fr.badblock.bungee.modules.login.checkers;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class LoginInvalidUsernameListener extends BadListener {

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
		// If the username isn't valid
		if (!username.matches("^\\w{3,16}$"))
		{
			// We cancel the player connection with a specific message
			event.cancel(badPlayer.getTranslatedMessage("bungee.login.invalidusername", null));
		}
	}
	
}