package fr.badblock.bungee.api.events.objects.friendlist;

import fr.badblock.bungee._plugins.objects.friendlist.FriendListable;
import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class FriendListableChangeEvent extends CancellableEvent {
    private final BadPlayer player;
    private final FriendListable oldStatus;
    private FriendListable newStatus;
}
