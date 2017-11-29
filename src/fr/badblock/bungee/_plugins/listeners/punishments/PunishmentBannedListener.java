package fr.badblock.bungee._plugins.listeners.punishments;

import fr.badblock.bungee._plugins.listeners.BadListener;
import fr.badblock.bungee.api.events.PlayerJoinEvent;
import fr.badblock.bungee.players.BadPlayer;
import fr.toenga.common.utils.bungee.Punished;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PunishmentBannedListener extends BadListener
{

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		BadPlayer badPlayer = event.getBadPlayer();
		Punished punished = badPlayer.getPunished();
		// Check end
		punished.checkEnd();
		if (punished.isBan())
		{
			String result = "";
			for (String string : badPlayer.getTranslatedMessages("punishments.ban", 
					punished.buildBanTime(badPlayer.getLocale()),
					punished.getBanReason()))
			{
				result += string;
			}
			event.cancel(result);
		}
	}

}