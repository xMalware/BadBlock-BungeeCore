package fr.badblock.bungee._plugins.commands.a_admin;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * Command to send a player from one server to another from the same network.
 * This command works across the network and therefore through any bungee connected to the same servers and the same production mode.
 * @author root
 *
 */
public class SendCommand extends BadCommand
{

	public SendCommand() {
		super("send", "bungee.command.send", "move");
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length != 2)
		{
			I19n.sendMessage(sender, "commands.send.usage");
			return;
		}

		String playerName = args[0];
		
		// Disconnected
		if (!BungeeManager.getInstance().hasUsername(playerName))
		{
			I19n.sendMessage(sender, "commands.send.disconnected", playerName);
			return;
		}
		
		String serverName = args[1];
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		
		if (serverInfo == null)
		{
			I19n.sendMessage(sender, "commands.send.unknownserver", serverName);
			return;
		}
		
		// TODO hierarchy
		
		BungeeManager.getInstance().sendPacket(new PlayerPacket(playerName, PlayerPacketType.SEND_SERVER, serverName));
		I19n.sendMessage(sender, "commands.send.sended", playerName, serverName);
	}

}