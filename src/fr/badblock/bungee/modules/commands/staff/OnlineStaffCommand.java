package fr.badblock.bungee.modules.commands.staff;

import fr.badblock.bungee.modules.commands.BadCommand;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Online staff
 * 
 * @author xMalware
 *
 */
public class OnlineStaffCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public OnlineStaffCommand() {
		super("plugins", "bungee.command.onlinestaff", "os");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
	}

}