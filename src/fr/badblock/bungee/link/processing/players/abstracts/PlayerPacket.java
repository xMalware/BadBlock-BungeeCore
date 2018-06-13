package fr.badblock.bungee.link.processing.players.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * 
 * Player Packet
 * 
 * @author xMalware
 *
 */
public class PlayerPacket {

	/**
	 * Player name (username)
	 * 
	 * @param Set
	 *            the username of the playerPacket
	 * @return The username of the playerPacket
	 */
	private String playerName;

	/**
	 * Player packet type
	 * 
	 * @param Set
	 *            the player packet type of the playerPacket
	 * @return The player packet type of the playerPacket
	 */
	private PlayerPacketType type;

	/**
	 * Content
	 * 
	 * @param Set
	 *            the content of the playerPacket
	 * @return The content of the playerPacket
	 */
	private String content;

}
