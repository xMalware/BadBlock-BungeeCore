
package fr.badblock.bungee.link.processing.bungee.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class BungeePacket {

	private BungeePacketType	type;
	private String				content;
	
}
