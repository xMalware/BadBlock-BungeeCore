package fr.badblock.bungee.modules.friends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public final class FriendListPlayer
{

    private UUID uuid;
    private FriendListPlayerState state;
    
}
