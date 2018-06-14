package fr.badblock.bungee.modules.commands.basic.friends;

/**
 * 
 * Friend list request status
 * 
 * @author xMalware
 *
 */
public enum FriendListRequestStatus {

	/**
	 * Already requested
	 */
	PLAYER_ALREADY_REQUESTED,

	/**
	 * Don't accept request
	 */
	PLAYER_DO_NOT_ACCEPT_REQUEST,

	/**
	 * Receive request
	 */
	PLAYER_RECEIVE_REQUEST,

	/**
	 * Can't act on himself
	 */
	PLAYER_SCHIZOPHRENIA,

	/**
	 * Already friends
	 */
	PLAYERS_ALREADY_FRIENDS,

	/**
	 * They're now friends
	 */
	PLAYERS_NOW_FRIENDS,

	/**
	 * Unknown error
	 */
	UNKNOWN_ERROR

}