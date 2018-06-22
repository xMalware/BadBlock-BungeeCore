package fr.badblock.bungee.modules.punishments;

import fr.badblock.api.common.utils.bungee.Punished;
import fr.badblock.api.common.utils.bungee.Punishment;
import fr.badblock.bungee.modules.abstracts.BadListener;
import fr.badblock.bungee.modules.login.events.PlayerLoggedEvent;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * 
 * @author xMalware
 *
 */
public class PunishmentWarnReconnectListener extends BadListener {

	/**
	 * When a player joins the server
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerLoggedEvent event) {
		// Getting the BadPlayer object
		BadPlayer badPlayer = event.getBadPlayer();
		
		Punished punished = badPlayer.getPunished();
		
		if (punished == null)
		{
			return;
		}
		
		if (punished.getWarn() == null)
		{
			return;
		}
		
		Punishment punishment = punished.getWarn();
		
		if (punishment == null)
		{
			return;
		}
		
		String reason = punishment.getReason();
		
		badPlayer.warn(reason);
		
		badPlayer.getPunished().setWarn(null);
		badPlayer.updatePunishments();
	}

}