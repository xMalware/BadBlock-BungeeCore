package fr.badblock.bungee.modules.commands.basic.friends;

import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
/**
 * 
 * Event called when the friendlistable status changes
 * 
 * @author xMalware
 *
 */
public class FriendListableChangeEvent extends CancellableEvent {

	/**
	 * The BadPlayer object
	 * 
	 * @param Set
	 *            the BadPlayer object
	 * @return Returns the BadPlayer object
	 */
	private final BadPlayer player;

	/**
	 * The old status
	 * 
	 * @param Set
	 *            the old status
	 * @return Returns the old status
	 */
	private final FriendListable oldStatus;

	/**
	 * The new status
	 * 
	 * @param Set
	 *            the new status
	 * @return Returns the new status
	 */
	private FriendListable newStatus;

}
