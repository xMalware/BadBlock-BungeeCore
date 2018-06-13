package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * False command that just simulates adding a player to the server operator list
 * 
 * @author xMalware
 *
 */
public class OpCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public OpCommand() {
		super("op");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If the number of arguments of the command is not equal to 1.
		if (args.length != 1) {
			// In this case the way of using the command is returned to the player
			I19n.sendMessage(sender, "bungee.commands.op.usage", null);
			// We stop there
			return;
		}
		// Sending a false operator add message
		I19n.sendMessage(sender, "bungee.commands.op.message", null, args[0]);
	}

}