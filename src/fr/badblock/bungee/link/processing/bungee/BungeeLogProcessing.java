package fr.badblock.bungee.link.processing.bungee;

import java.util.logging.Level;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.link.processing.bungee.abstracts._BungeeProcessing;

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

}
