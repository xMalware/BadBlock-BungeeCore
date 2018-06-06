package fr.badblock.bungee.modules.friends;

/**
 * 
 * Friend list request status
 * 
 * @author xMalware
 *
 */
public enum FriendListRequestStatus
{

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
	 * Already requested
	 */
	PLAYER_ALREADY_REQUESTED,
	
	/**
	 * Receive request
	 */
	PLAYER_RECEIVE_REQUEST,
	
	/**
	 * Don't accept request
	 */
	PLAYER_DO_NOT_ACCEPT_REQUEST,
	
	/**
	 * Unknown error
	 */
	UNKNOWN_ERROR

}