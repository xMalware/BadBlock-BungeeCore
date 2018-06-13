package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Reload data player packet
 * 
 * @author xMalware
 *
 */
public class PlayerReloadDataProcessing extends _PlayerProcessing {

	@Override
	/**
	 * Processing
	 */
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
		// Get the local bad player
		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);
		// Reload the bad player data
		badPlayer.reload();
	}

}
