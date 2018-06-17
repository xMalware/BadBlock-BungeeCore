package fr.badblock.bungee.modules.commands.modo;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Connect player command
 *
 * @author xMalware
 *
 */
public class ConnectPlayerCommand extends BadCommand {

	// I18n key prefix
	private String prefix = "bungee.commands.connectplayer.";

	/**
	 * Command constructor
	 */
	public ConnectPlayerCommand() {
		// Super!
		super("connectplayer", null, "cop");
		this.setForPlayersOnly(true);
	}

	/**
	 * Send help to the player
	 * 
	 * @param sender
	 */
	private void help(CommandSender sender) {
		// Send help
		I19n.sendMessage(sender, prefix + "help", null);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If no argument has been entered
		if (args.length <= 0) {
			// We give him help.
			help(sender);
			// We stop there.
			return;
		}

		String playerName = args[0];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(playerName)) {
			I19n.sendMessage(sender, prefix + "offline", null, playerName);
			return;
		}

		BadPlayer badPlayer = bungeeManager.getBadPlayer(playerName);

		if (badPlayer == null) {
			I19n.sendMessage(sender, prefix + "offline", null, playerName);
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		String currentServer = badPlayer.getCurrentServer();

		if (currentServer == null) {
			I19n.sendMessage(sender, prefix + "unknownserver", null);
			return;
		}

		ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(currentServer);

		if (serverInfo == null) {
			I19n.sendMessage(sender, prefix + "unknownserver", null);
			return;
		}

		if (proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null)
		{
			if (proxiedPlayer.getServer().getInfo().equals(serverInfo))
			{
				I19n.sendMessage(sender, prefix + "alreadyconnected", null, currentServer);
				return;
			}
		}

		proxiedPlayer.connect(serverInfo);

		I19n.sendMessage(sender, prefix + "teleported", null, badPlayer.getName(), serverInfo.getName());
	}

}