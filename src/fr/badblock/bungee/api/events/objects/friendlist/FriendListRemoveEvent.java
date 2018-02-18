package fr.badblock.bungee.api.events.objects.friendlist;

import fr.badblock.bungee.api.CancellableEvent;
import fr.badblock.bungee.players.BadOfflinePlayer;
import fr.badblock.bungee.players.BadPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class FriendListRemoveEvent extends CancellableEvent {
    private final BadPlayer wantPlayer;
    private final BadOfflinePlayer wantedPlayer;
    private FriendListRemoveStatus status;

    public enum FriendListRemoveStatus {
        PLAYER_SCHIZOPHRENIA,
        PLAYER_REMOVED_FROM_LIST,
        PLAYER_REQUEST_DECLINED,
        REQUEST_TO_PLAYER_CANCELLED,
        NOT_REQUESTED_OR_FRIEND_WITH_PLAYER,
        UNKNOWN_ERROR
    }
}
