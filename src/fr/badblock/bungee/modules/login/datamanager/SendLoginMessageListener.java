package fr.badblock.bungee.modules.login.datamanager;

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
public class SendLoginMessageListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (event.isCancelled())
		{
			return;
		}
		
		// We get the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		
		if (badPlayer.isOnlineMode())
		{
			return;
		}
		
		if (badPlayer.getLoginPassword() == null || badPlayer.getLoginPassword().isEmpty())
		{
			badPlayer.sendTranslatedOutgoingMessage("bungee.commands.register.usage", null, badPlayer.getName());
			return;
		}
		
		badPlayer.sendTranslatedOutgoingMessage("bungee.commands.login.usage", null, badPlayer.getName());
	}

}
