package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.utils.ChatColorUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Player message processing
 * 
 * @author xMalware
 *
 */
public class PlayerSendMessageProcessing extends _PlayerProcessing {

	@SuppressWarnings("deprecation")
	/**
	 * Player processing
	 */
	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
		// Send messages
		proxiedPlayer.sendMessages(
				ChatColorUtils.translateColors('&', playerPacket.getContent().split(System.lineSeparator())));
	}

}
