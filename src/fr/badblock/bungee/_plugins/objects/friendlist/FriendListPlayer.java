package fr.badblock.bungee._plugins.objects.friendlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class FriendListPlayer
{

    private UUID uuid;
    private FriendListPlayerState state;
    
}
