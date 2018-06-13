package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Player send server packet processing
 * 
 * @author xMalware
 *
 */
public class PlayerSendServerProcessing extends _PlayerProcessing {

	/**
	 * Player processing
	 */
	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket) {
		// Get the server name
		String serverName = playerPacket.getContent();
		// Get the server info
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);

		// If the serverInfo is null
		if (serverInfo == null) {
			// Unknown server
			I19n.sendMessage(proxiedPlayer, "bungee.commands.send.unknownserver", null, serverName);
			// We stop there
			return;
		}

		// Connect the proxied player
		proxiedPlayer.connect(serverInfo);
		// You've been moved
		I19n.sendMessage(proxiedPlayer, "bungee.commands.send.youvebeenmoved", null);
	}

}
