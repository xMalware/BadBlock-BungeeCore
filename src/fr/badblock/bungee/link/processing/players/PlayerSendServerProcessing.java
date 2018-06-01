package fr.badblock.bungee.link.processing.players;

import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts._PlayerProcessing;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerSendServerProcessing extends _PlayerProcessing
{

	@Override
	public void done(ProxiedPlayer proxiedPlayer, PlayerPacket playerPacket)
	{
		String serverName = playerPacket.getContent();
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		
		if (serverInfo == null)
		{
			I19n.sendMessage(proxiedPlayer, "commands.send.unknownserver", null, serverName);
			return;
		}
		
		proxiedPlayer.connect(serverInfo);
		I19n.sendMessage(proxiedPlayer, "commands.send.youvebeenmoved", null);
	}
	
}
