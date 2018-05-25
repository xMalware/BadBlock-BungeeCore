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
 * 
 * Command to send a player from one server to another from the same network.
 * This command works across the network and therefore through any bungee connected to the same servers and the same production mode.
 * 
 * The required permission to execute this command is: bungee.command.send
 * 
 * @author xMalware
 *
 */
public class SendCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public SendCommand()
	{
		super("send", "bungee.command.send", "move");
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{

		// If no argument has been entered, i.e. the user name and the server name
		if (args.length != 2)
		{
			// A message is sent to him containing the information allowing him to take note of the use of the command.
			I19n.sendMessage(sender, "commands.send.usage");
			// Nothing has been written from him, no argument. After we explain it to him, we stop there.
			return;
		}

		// We get the nickname to send to another server
		String playerName = args[0];
		
		// If the player is disconnected from the network
		if (!BungeeManager.getInstance().hasUsername(playerName))
		{
			// In this case, we notify the user who executed this command
			I19n.sendMessage(sender, "commands.send.disconnected", playerName);
			// We stop there.
			return;
		}
		
		// We get the name of the server
		String serverName = args[1];
		// We get the information of the server
		ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);
		
		// If there is no information about the server
		if (serverInfo == null)
		{
			// In this case, we notify the user who executed this command
			I19n.sendMessage(sender, "commands.send.unknownserver", serverName);
			// We stop there.
			return;
		}
		
		// TODO Establish a hierarchy system. Not being able to send a player with higher permissions
		
		// We send the player's move with a packet over the network
		BungeeManager.getInstance().sendPacket(new PlayerPacket(playerName, PlayerPacketType.SEND_SERVER, serverName));
		// The requester is told that the action was indeed made
		I19n.sendMessage(sender, "commands.send.sended", playerName, serverName);
	}

}