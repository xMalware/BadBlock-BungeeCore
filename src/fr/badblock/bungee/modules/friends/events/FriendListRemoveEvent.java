package fr.badblock.bungee.modules.friends.events;

import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.modules.friends.FriendListRemoveStatus;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class FriendListRemoveEvent extends CancellableEvent
{
	
    private final BadPlayer			wantPlayer;
    private final BadOfflinePlayer	wantedPlayer;
    private FriendListRemoveStatus	status;
    
}
