package fr.badblock.bungee._plugins.objects.friendlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class FriendListPlayer {
    private String name;
    private FriendListPlayerState state;
}
