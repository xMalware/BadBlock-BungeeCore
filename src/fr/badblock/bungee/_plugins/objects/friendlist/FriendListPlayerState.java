package fr.badblock.bungee._plugins.objects.friendlist;

/**
 * States of a friendship
 * 
 * @author xMalware
 *
 */
public enum FriendListPlayerState
{
	
    /**
     * The player request another player
     */
    REQUESTED,
    /**
     * An other player requested this player
     */
    WAITING,
    /**
     * The players are friends
     */
    ACCEPTED

}
