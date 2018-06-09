package fr.badblock.bungee.modules.admin.commands;

import fr.badblock.api.common.utils.permissions.Permissible;
import fr.badblock.api.common.utils.permissions.PermissionUser;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacket;
import fr.badblock.bungee.link.processing.players.abstracts.PlayerPacketType;
import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
			I19n.sendMessage(sender, "bungee.commands.send.usage", null);
			// Nothing has been written from him, no argument. After we explain it to him, we stop there.
			return;
		}

		// We get the nickname to send to another server
		String playerName = args[0];

		// If the player is disconnected from the network
		if (!BungeeManager.getInstance().hasUsername(playerName))
		{
			// In this case, we notify the user who executed this command
			I19n.sendMessage(sender, "bungee.commands.send.disconnected", null, playerName);
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
			I19n.sendMessage(sender, "bungee.commands.send.unknownserver", null, serverName);
			// We stop there.
			return;
		}

		// If the sender is a player
		if (sender instanceof ProxiedPlayer)
		{
			// Get the ProxiedPlayer object of the sender
			ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
			
			// Get the BadPlayer object of the sender
			BadPlayer senderPlayer = BungeeManager.getInstance().getBadPlayer(proxiedPlayer);
			// Get the BadPlayer object of the target
			BadPlayer targetPlayer = BungeeManager.getInstance().getBadPlayer(playerName);

			// If the sender player is null
			if (senderPlayer == null)
			{
				// Send an error message
				I19n.sendMessage(sender, "bungee.commands.send.error", null, serverName);
				// So we stop there
				return;
			}

			// If the target player is null
			if (targetPlayer == null)
			{
				// Send the disconnected message
				I19n.sendMessage(sender, "bungee.commands.send.disconnected", null, playerName);
				// So we stop there
				return;
			}

			// Get the sender permissions
			PermissionUser senderPermissions = senderPlayer.getPermissions();
			// Get the target permisions
			PermissionUser targetPermissions = targetPlayer.getPermissions();

			// If the sender & target permissions aren't null
			if (senderPermissions != null && targetPermissions != null)
			{
				// Get the sender highest rank
				Permissible senderPermissible = senderPermissions.getHighestRank("bungee", false);
				// Get the target highest rank
				Permissible targetPermissible = targetPermissions.getHighestRank("bungee", false);
				
				// If the sender & target highest ranks aren't null
				if (senderPermissible != null && targetPermissible != null)
				{
					// If the sender power is less than the target power
					if (senderPermissible.getPower() < targetPermissible.getPower())
					{
						// No enough permissions to send
						I19n.sendMessage(sender, "bungee.commands.send.notenoughpermissions", null, playerName);
						// So we stop there
						return;
					}
				}
			}
		}

		// We send the player's move with a packet over the network
		BungeeManager.getInstance().sendPacket(new PlayerPacket(playerName, PlayerPacketType.SEND_SERVER, serverName));
		// The requester is told that the action was indeed made
		I19n.sendMessage(sender, "bungee.commands.send.sent", null, playerName, serverName);
	}

}