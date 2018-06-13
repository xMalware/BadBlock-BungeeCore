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
/**
 * 
 * Player join event
 * 
 * @author xMalware
 *
 */
public class PlayerJoinEvent extends Event {

	/**
	 * BadPlayer
	 * 
	 * @param Set
	 *            the BadPlayer object
	 * @return Returns the BadPlayer object
	 */
	private BadPlayer badPlayer;

	/**
	 * PreLoginEvent
	 * 
	 * @param Set
	 *            the PreLoginEvent object
	 * @return Returns the PreLoginEvent object
	 */
	private PreLoginEvent preLoginEvent;

	/**
	 * Cancel the message
	 * 
	 * @param message
	 */
	public void cancel(String message) {
		// Cancelled
		preLoginEvent.setCancelled(true);
		// Set the cancel reason
		preLoginEvent.setCancelReason(message);
		// Remove the BadPlayer
		badPlayer.remove();
	}

}