package fr.badblock.bungee.modules.login.events;

import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class PlayerLoggedEvent extends Event {

	/**
	 * BadPlayer
	 * 
	 * @param Set
	 *            the BadPlayer object
	 * @return Returns the BadPlayer object
	 */
	private BadPlayer badPlayer;

}