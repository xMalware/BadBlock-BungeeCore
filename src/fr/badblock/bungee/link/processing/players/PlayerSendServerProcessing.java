package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
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
		// Get data
		String data = playerPacket.getContent();
		
		String[] info = data.split("\\|");
		
		String serverName = info[0];
		String reason = "";
		
		if (info.length == 2)
		{
			reason = info[1];
		}
		
		// Get the server info
		ServerInfo serverInfo = BungeeCord.getInstance().getServers().get(serverName);

		// If the serverInfo is null
		if (serverInfo == null) {
			// Unknown server
			I19n.sendMessage(proxiedPlayer, "bungee.commands.send.unknownserver", null, serverName);
			// We stop there
			return;
		}

		// Connect the proxied player
		proxiedPlayer.connect(serverInfo);
		
		if (reason.equals("MATCHMAKING"))
		{
			// You've been moved
			I19n.sendMessage(proxiedPlayer, "bungee.commands.send.serverfound", serverName);
			return;
		}
		
		// You've been moved
		I19n.sendMessage(proxiedPlayer, "bungee.commands.send.youvebeenmoved", serverName);
	}

}
