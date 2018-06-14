package fr.badblock.bungee.link.processing.players.abstracts;

import lombok.Getter;

@Getter
/**
 * 
 * Player Packet Type
 * 
 * @author xMalware
 *
 */
public enum PlayerPacketType {

	/**
	 * Send BadPlayer update packet type
	 */
	BADPLAYER_UPDATE(),

	/**
	 * Player kick packet type
	 */
	KICK(),

	/**
	 * Reload data packet type
	 */
	RELOAD_DATA(),

	/**
	 * Send json message packet type
	 */
	SEND_JSON_MESSAGE(),

	/**
	 * Send message packet type
	 */
	SEND_MESSAGE(),

	/**
	 * Send server packet type
	 */
	SEND_SERVER();

	/**
	 * Player processing
	 * 
	 * @param Set
	 *            the player processing
	 * @return Returns the player processing
	 */
	private _PlayerProcessing process;

	/**
	 * Set the process
	 * 
	 * @param process
	 */
	public void setProcess(_PlayerProcessing process) {
		// Set the process
		this.process = process;
	}

}
