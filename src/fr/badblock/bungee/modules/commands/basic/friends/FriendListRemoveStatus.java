package fr.badblock.bungee.modules.commands.basic.friends;

/**
 * 
 * Friend list remove status
 * 
 * @author xMalware
 *
 */
public enum FriendListRemoveStatus {

	/**
	 * Not requested/friend with player
	 */
	NOT_REQUESTED_OR_FRIEND_WITH_PLAYER,

	/**
	 * Removed from the list
	 */
	PLAYER_REMOVED_FROM_LIST,

	/**
	 * Request declind
	 */
	PLAYER_REQUEST_DECLINED,

	/**
	 * When the player tries to act on himself
	 */
	PLAYER_SCHIZOPHRENIA,

	/**
	 * Request to player cancelled
	 */
	REQUEST_TO_PLAYER_CANCELLED,

	/**
	 * Unknown error
	 */
	UNKNOWN_ERROR

}