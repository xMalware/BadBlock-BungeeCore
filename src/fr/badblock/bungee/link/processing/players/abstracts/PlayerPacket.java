package fr.badblock.bungee.link.processing.players.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class PlayerPacket {

	private String				playerName;
	private PlayerPacketType	type;
	private String				content;
	
}
