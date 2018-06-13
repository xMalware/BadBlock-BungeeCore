package fr.badblock.bungee.link.processing.bungee;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import net.md_5.bungee.api.ProxyServer;

/**
 * 
 * Broadcast BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeBroadcastProcessing extends _BungeeProcessing {

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * Bungee message processing
	 * 
	 * @param Messages
	 *            to broadcast
	 */
	public void done(String message) {
		// For each message
		for (String string : message.split(System.lineSeparator())) {
			// Broadcast it
			ProxyServer.getInstance().broadcast(string);
		}
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.BROADCAST;
	}

}
