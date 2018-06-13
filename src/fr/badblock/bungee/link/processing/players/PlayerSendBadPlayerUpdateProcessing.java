package fr.badblock.bungee.link.processing.players;

import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * BadPlayer update processing
 * 
 * @author xMalware
 *
 */
public class PlayerSendBadPlayerUpdateProcessing extends _PlayerProcessing {

	/**
	 * Player processing
	 */
	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
		// If the proxied player is null
		if (proxiedPlayer == null) {
			// So we stop there
			return;
		}

		// Get the BadPlayer object
		BadPlayer badPlayer = GsonUtils.getGson().fromJson(playerPacket.getContent(), BadPlayer.class);
		// Put the BadPlayer object
		BadPlayer.put(badPlayer);
	}

}
