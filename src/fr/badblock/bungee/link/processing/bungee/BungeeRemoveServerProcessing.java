package fr.badblock.bungee.link.processing.bungee;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.bungee.BadBungee;
import net.md_5.bungee.BungeeCord;

/**
 * 
 * Remove server BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeRemoveServerProcessing extends _BungeeProcessing {

	@Override
	/**
	 * 
	 * Bungee remove server processing
	 * 
	 * @param Messages
	 *
	 */
	public void done(String message) {
		// Delete a server
		BungeeCord.getInstance().getServers().remove(message);
		// Log
		BadBungee.log("Deleted server: " + message);
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.REMOVE_SERVER;
	}

}
