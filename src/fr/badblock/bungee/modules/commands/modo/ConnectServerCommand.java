package fr.badblock.bungee.modules.commands.modo;

import fr.badblock.bungee.modules.commands.BadCommand;
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
public class ConnectServerCommand extends BadCommand {

	// I18n key prefix
	private String prefix = "bungee.commands.connectserver.";

	/**
	 * Command constructor
	 */
	public ConnectServerCommand() {
		// Super!
		super("connectserver", null, "cos");
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

		String serverName = args[0];

		ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(serverName);

		if (serverInfo == null) {
			I19n.sendMessage(sender, prefix + "unknownserver", null, serverName);
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		if (proxiedPlayer.getServer() != null && proxiedPlayer.getServer().getInfo() != null) {
			if (proxiedPlayer.getServer().getInfo().equals(serverInfo)) {
				I19n.sendMessage(sender, prefix + "alreadyconnected", null, serverName);
				return;
			}
		}

		proxiedPlayer.connect(serverInfo);

		I19n.sendMessage(sender, prefix + "teleported", null, serverInfo.getName());
	}

}