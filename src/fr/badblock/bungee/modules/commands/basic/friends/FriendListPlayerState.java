package fr.badblock.bungee.modules.commands.basic.friends;

/**
 * States of a friendship
 * 
 * @author xMalware
 *
 */
public enum FriendListPlayerState {

	/**
	 * The players are friends
	 */
	ACCEPTED,

	/**
	 * The player request another player
	 */
	REQUESTED,

	/**
	 * An other player requested this player
	 */
	WAITING

}
