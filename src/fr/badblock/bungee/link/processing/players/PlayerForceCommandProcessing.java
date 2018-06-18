package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Player force command processing
 * 
 * @author xMalware
 *
 */
public class PlayerForceCommandProcessing extends _PlayerProcessing {

	/**
	 * Player processing
	 */
	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
		// Force command
		BungeeCord.getInstance().getPluginManager().dispatchCommand(proxiedPlayer, playerPacket.getContent());
	}

}
