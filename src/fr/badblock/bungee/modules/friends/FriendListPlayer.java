package fr.badblock.bungee.modules.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * The friendlist player object
 * 
 * @author xMalware
 *
 */
public final class FriendListPlayer
{

	/**
	 * Unique ID of the player
	 * @param Set the unique ID
	 * @return Returns the unique ID
	 */
    private UUID					uuid;
    
    /**
     * Friendlist player state
     * @param Set the friendlist player state
     * @return Returns the friendlist player state
     */
    private FriendListPlayerState	state;
    
}
