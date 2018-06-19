package fr.badblock.bungee.modules.commands.basic.party;

import fr.badblock.bungee.modules.abstracts.BadListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class PartyMessageListener extends BadListener {

	/**
	 * When a message in the chat is sent
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChatEvent(ChatEvent event) {
		// We get the sender of the message
		Connection sender = event.getSender();

		// If the sender is not set
		if (sender == null) {
			// We stop there
			return;
		}

		// If the sender is not a player
		if (!(sender instanceof ProxiedPlayer)) {
			// Then we stop here.
			return;
		}

		// We get the player
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		String message = event.getMessage();

		if (message == null)
		{
			return;
		}

		if (message.startsWith("%") && message.length() > 1)
		{
			event.setCancelled(true);
			if (message.length() > 1)
			{
				// Send the message
				BungeeCord.getInstance().getPluginManager().dispatchCommand(proxiedPlayer, "/party msg " + 
						event.getMessage().substring(1, event.getMessage().length() - 1));
			}
			else
			{
				// Usage
				BungeeCord.getInstance().getPluginManager().dispatchCommand(proxiedPlayer, "/party msg");
			}
		}
	}

}