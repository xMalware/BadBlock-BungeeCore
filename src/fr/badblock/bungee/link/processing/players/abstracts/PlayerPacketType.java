package fr.badblock.bungee.link.processing.players.abstracts;

import fr.badblock.bungee.link.processing.players.PlayerReloadDataProcessing;
import fr.badblock.bungee.link.processing.players.PlayerSendBadPlayerUpdateProcessing;
import fr.badblock.bungee.link.processing.players.PlayerSendJsonMessageProcessing;
import fr.badblock.bungee.link.processing.players.PlayerSendMessageProcessing;
import fr.badblock.bungee.link.processing.players.PlayerSendServerProcessing;
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
	 * Send message packet type
	 */
	SEND_MESSAGE(new PlayerSendMessageProcessing()),

	/**
	 * Send json message packet type
	 */
	SEND_JSON_MESSAGE(new PlayerSendJsonMessageProcessing()),

	/**
	 * Send server packet type
	 */
	SEND_SERVER(new PlayerSendServerProcessing()),

	/**
	 * Reload data packet type
	 */
	RELOAD_DATA(new PlayerReloadDataProcessing()),

	/**
	 * Send BadPlayer update packet type
	 */
	BADPLAYER_UPDATE(new PlayerSendBadPlayerUpdateProcessing());

	/**
	 * Player processing
	 * 
	 * @param Set
	 *            the player processing
	 * @return Returns the player processing
	 */
	private _PlayerProcessing process;

	/**
	 * Constructor of a player packet type
	 * 
	 * @param The
	 *            Player Processing
	 */
	PlayerPacketType(_PlayerProcessing process) {
		// Set the processing
		this.process = process;
	}

}
