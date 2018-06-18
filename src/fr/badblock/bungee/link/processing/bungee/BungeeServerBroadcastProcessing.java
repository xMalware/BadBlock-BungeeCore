package fr.badblock.bungee.link.processing.bungee;

import java.util.List;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import fr.badblock.api.common.sync.bungee.objects.ServerBroadcastObject;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.api.common.utils.GsonUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.players.BadPlayer;

/**
 * 
 * Server broadcast BungeeCord packet
 * 
 * @author root
 *
 */
public class BungeeServerBroadcastProcessing extends _BungeeProcessing {

	@Override
	/**
	 * Server broadcast processing
	 * 
	 * @param Messages
	 *            to broadcast
	 */
	public void done(String message) {
		// To server broadcast
		ServerBroadcastObject serverBroadcast = GsonUtils.getGson().fromJson(message, ServerBroadcastObject.class);
		// Bungee manager
		BungeeManager bungeeManager = BungeeManager.getInstance();
		// Get logged players by server
		List<BadPlayer> loggedPlayers = bungeeManager.getLoggedPlayers(
				badPlayer -> badPlayer.getCurrentServer().equalsIgnoreCase(serverBroadcast.getServerName()));
		// For each player
		for (BadPlayer badPlayer : loggedPlayers)
		{
			// Send outgoing message
			badPlayer.sendOutgoingMessage(message);
		}
	}

	@Override
	public BungeePacketType getPacketType() {
		return BungeePacketType.SERVER_BROADCAST;
	}

}
