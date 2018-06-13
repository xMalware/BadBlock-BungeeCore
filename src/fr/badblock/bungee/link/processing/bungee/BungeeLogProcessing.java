package fr.badblock.bungee.link.processing.bungee;

import java.util.logging.Level;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.bungee.BadBungee;

/**
 * 
 * Log BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeLogProcessing extends _BungeeProcessing {

	@Override
	/**
	 * 
	 * Bungee message processing
	 * 
	 * @param Messages
	 *            to log
	 *
	 */
	public void done(String message) {
		// For each message
		for (String string : message.split(System.lineSeparator())) {
			// Log it
			BadBungee.getInstance().getLogger().log(Level.INFO, string);
		}
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.LOG;
	}

}
