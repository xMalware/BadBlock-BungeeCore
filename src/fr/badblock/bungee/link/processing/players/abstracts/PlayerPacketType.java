package fr.badblock.bungee.link.processing.players.abstracts;

import fr.badblock.bungee.link.processing.players.PlayerForceCommandProcessing;
import fr.badblock.bungee.link.processing.players.PlayerKickProcessing;
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
	 * Send BadPlayer update packet type
	 */
	BADPLAYER_UPDATE(new PlayerSendBadPlayerUpdateProcessing()),

	/**
	 * Player kick packet type
	 */
	KICK(new PlayerKickProcessing()),

	/**
	 * Reload data packet type
	 */
	RELOAD_DATA(new PlayerReloadDataProcessing()),

	/**
	 * Send json message packet type
	 */
	SEND_JSON_MESSAGE(new PlayerSendJsonMessageProcessing()),

	/**
	 * Send message packet type
	 */
	SEND_MESSAGE(new PlayerSendMessageProcessing()),

	/**
	 * Send server packet type
	 */
	SEND_SERVER(new PlayerSendServerProcessing()),

	/**
	 * Force command packet type
	 */
	FORCE_COMMAND(new PlayerForceCommandProcessing());

	/**
	 * Player processing
	 * 
	 * @param Set
	 *            the player processing
	 * @return Returns the player processing
	 */
	private _PlayerProcessing process;

	/**
	 * Player processing constructor
	 * 
	 * @param process
	 */
	PlayerPacketType(_PlayerProcessing process) {
		// Set the process
		this.process = process;
	}

}
