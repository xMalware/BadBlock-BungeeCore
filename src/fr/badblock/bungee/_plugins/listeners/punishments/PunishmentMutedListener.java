package fr.badblock.bungee._plugins.listeners.punishments;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.players.BadPlayer;
import fr.toenga.common.utils.bungee.Punished;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PunishmentMutedListener extends BadListener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void onChatEventEvent(ChatEvent event)
	{
		Connection sender = event.getSender();
		if (!(sender instanceof ProxiedPlayer))
		{
			return;
		}
		
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		Punished punished = badPlayer.getPunished();
		
		// Null punish
		if (punished == null)
		{
			return;
		}
		
		// Check end
		punished.checkEnd();
		if (punished.isMute())
		{
			event.setCancelled(true);
			for (String string : badPlayer.getTranslatedMessages("punishments.muted", 
					punished.buildMuteTime(badPlayer.getLocale()),
					punished.getMuteReason(), punished.getMuter()))
			{
                badPlayer.sendOutgoingMessage(string);
			}
		}
	}

}