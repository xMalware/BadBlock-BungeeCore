package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.bungee.link.bungee.PeakTask;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * @author xMalware
 *
 */
public class PeakCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public PeakCommand() {
		super("peak", "bungee.command.peak", "pic");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		I19n.sendMessage(sender, "bungee.commands.peak.message", null, PeakTask.todayPeak, PeakTask.totalPeak);
	}

}