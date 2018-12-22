package fr.badblock.bungee.link.processing.bungee;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Force kick BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeForceKickProcessing extends _BungeeProcessing {

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * 
	 * Bungee bad player processing
	 * 
	 * @param Messages
	 *
	 */
	public void done(String message) {
		// Get the BadPlayer object
		BadPlayer badPlayer = BadPlayer.get(message);
		// If the bad player is null
		if (badPlayer == null) {
			// Returns null
			return;
		}
		ProxiedPlayer proxiedPlayer = BungeeCord.getInstance().getPlayer(badPlayer.getName());
		if (proxiedPlayer != null) {
			proxiedPlayer
					.disconnect(I19n.getMessage(proxiedPlayer, "bungee.commands.forcekick.youhavebeenkicked", null));
		}
		// Remove the bad player
		badPlayer.remove();
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.LOG;
	}

}
