package fr.badblock.bungee.modules.punishments;

import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * The purpose of this class is to prevent muted
 * players from being able to send the message
 * 
 * @author xMalware
 *
 */
public class PunishmentMutedListener extends BadListener
{

	/**
	 * When a message in the chat is sent
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onChatEventEvent(ChatEvent event)
	{
		// We get the sender of the message
		Connection sender = event.getSender();
	
		// If the sender is not set
		if (sender == null)
		{
			// We stop there
			return;
		}
		
		// If the sender is not a player
		if (!(sender instanceof ProxiedPlayer))
		{
			// Then we stop here.
			return;
		}
		
		// We get the player
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		// We get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// We get punishment information
		Punished punished = badPlayer.getPunished();
		
		// If there is no punishment data
		if (punished == null)
		{
			// We stop there
			return;
		}
		
		// We check if the player is still muted, we stop his mute if it is finished
		punished.checkEnd();
		
		// If the player is still muted
		if (punished.isMute())
		{
			// So we cancel the message
			event.setCancelled(true);
			// For each message in the mute notification
			for (String string : badPlayer.getTranslatedMessages("punishments.muted", null,
					punished.buildMuteTime(badPlayer.getLocale()),
					punished.getMuteReason(), punished.getMuter()))
			{
				// So we send the line to the player
                badPlayer.sendOutgoingMessage(string);
			}
		}
	}

}