package fr.badblock.bungee.modules.friends.events;

import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.modules.friends.FriendListRequestStatus;
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
 * Event of friendlist request
 * 
 * @author xMalware
 *
 */
public class FriendListRequestEvent extends CancellableEvent
{
	
	/**
	 * The want player
	 * @param Set the want player
	 * @return Returns the want player
	 */
    private final BadPlayer			wantPlayer;
    
    /**
     * The wanted player
     * @param Set the wanted player
     * @return Returns the wanted player
     */
    private final BadOfflinePlayer	wantedPlayer;
    
    /**
     * The friendlist request status
     * @param Set the friendlist request status
     * @return Returns the friendlist request status
     */
    private FriendListRequestStatus	status;
    
}
