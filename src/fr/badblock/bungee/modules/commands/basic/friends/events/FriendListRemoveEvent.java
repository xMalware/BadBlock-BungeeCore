package fr.badblock.bungee.modules.commands.basic.friends.events;

import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.modules.commands.basic.friends.FriendListRemoveStatus;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * Event of friendlist remove
 * 
 * @author xMalware
 *
 */
public class FriendListRemoveEvent extends CancellableEvent {

	/**
	 * The want player
	 * 
	 * @param Set
	 *            the want player
	 * @return Get the want player
	 */
	private final BadPlayer wantPlayer;
	/**
	 * The wanted player
	 * 
	 * @param Set
	 *            the wanted player
	 * @return Get the wanted player
	 */
	private final BadOfflinePlayer wantedPlayer;
	/**
	 * Get the friendlist remove status
	 * 
	 * @param Set
	 *            the friendlist remove status
	 * @return Get the friendlist remove status
	 */
	private FriendListRemoveStatus status;

}
