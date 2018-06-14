package fr.badblock.bungee.modules.commands.modo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.BanCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.KickCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.MuteCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.TempBanCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.TempMuteCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.UnbanCommand;
import fr.badblock.bungee.modules.commands.modo.subcommands.UnmuteCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Moderation command
 *
 * @author xMalware
 *
 */
public class MCommand extends BadCommand {

	private List<AbstractModCommand> moderationCommands;

	// I18n key prefix
	private String prefix = "bungee.commands.mod.";

	/**
	 * Command constructor
	 */
	public MCommand() {
		// Super!
		super("m", null, "md");

		moderationCommands = new ArrayList<>();
		moderationCommands.add(new BanCommand());
		moderationCommands.add(new TempBanCommand());
		moderationCommands.add(new KickCommand());
		moderationCommands.add(new TempMuteCommand());
		moderationCommands.add(new MuteCommand());
		moderationCommands.add(new UnmuteCommand());
		moderationCommands.add(new UnbanCommand());
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

		String subcommand = args[0];

		switch (subcommand) {
		case "help":
		case "h":
		case "?":
			help(sender);
			break;

		default:
			AbstractModCommand command = null;
			for (AbstractModCommand aCommand : moderationCommands) {
				if (aCommand.getName().equalsIgnoreCase(subcommand)) {
					command = aCommand;
					break;
				}
				if (aCommand.getArgs() != null
						&& Arrays.asList(aCommand.getArgs()).contains(subcommand.toLowerCase())) {
					command = aCommand;
					break;
				}
			}
			if (command != null) {
				if (sender.hasPermission(command.getPermission())) {
					command.run(sender, args);
				} else {
					I19n.sendMessage(sender, prefix + "notenoughpermissions", null);
				}
			} else {
				unknownCommand(sender);
			}
			break;
		}
	}

	/**
	 * Send unknown command to the player
	 * 
	 * @param sender
	 */
	private void unknownCommand(CommandSender sender) {
		// Send help
		I19n.sendMessage(sender, prefix + "unknowncommand", null);
	}

}