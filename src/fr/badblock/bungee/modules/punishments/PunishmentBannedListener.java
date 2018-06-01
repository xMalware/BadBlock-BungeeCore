package fr.badblock.bungee.modules.punishments;

import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import fr.toenga.common.utils.bungee.Punished;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to block players from logging in if they are banned
 * 
 * @author xMalware
 *
 */
public class PunishmentBannedListener extends BadListener
{

	/**
	 * When a player joins the server
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		// Getting the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		// Getting punishment information
		Punished punished = badPlayer.getPunished();
		
		// If there is no information of punishments
		if (punished == null)
		{
			// So we stop there
			return;
		}
		
		// If there is no information of punishment
		punished.checkEnd();
		
		// If the player is still banned
		if (punished.isBan())
		{
			// We create an empty ban message
			String result = "";
			// For each line of the ban message
			for (String string : badPlayer.getTranslatedMessages("punishments.ban", null,
					punished.buildBanTime(badPlayer.getLocale()),
					punished.getBanReason()))
			{
				// We add it to the final ban message
				result += string + System.lineSeparator();
			}
			// We cancel the player's connection with the ban message
			event.cancel(result);
		}
	}

}