package fr.badblock.bungee.modules.commands.modo.subcommands;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.modo.AbstractModCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * BadCommand
 * 
 * @author xMalware
 *
 */
public class TrackCommand extends AbstractModCommand {

	/**
	 * Constructor
	 */
	public TrackCommand() {
		// Super!
		super("track", new String[] { "t" });
	}

	/**
	 * Run
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If arg length != 2
		if (args.length != 2) {
			// Send the message
			I19n.sendMessage(sender, getPrefix("usage"), null);
			// So we stop there
			return;
		}

		// Get the player name
		String playerName = args[1];

		BungeeManager bungeeManager = BungeeManager.getInstance();

		if (!bungeeManager.hasUsername(playerName)) {
			I19n.sendMessage(sender, getPrefix("offline"), null);
			return;
		}

		// Get the online target player
		BadPlayer badOnlinePlayer = bungeeManager.getBadPlayer(playerName);

		if (badOnlinePlayer == null) {
			I19n.sendMessage(sender, getPrefix("offline"), null);
			return;
		}

		
	}

}