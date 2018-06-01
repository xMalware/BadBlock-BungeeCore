package fr.badblock.bungee.modules.login.events;

import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Event;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class PlayerJoinEvent extends Event
{

	private BadPlayer 	  badPlayer;
	private PreLoginEvent preLoginEvent;

	public void cancel(String message)
	{
		preLoginEvent.setCancelled(true);
		preLoginEvent.setCancelReason(message);
		badPlayer.remove();
	}
	
	
}
