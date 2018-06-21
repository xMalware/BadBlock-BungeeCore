package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * @author xMalware
 *
 */
public class GReloadCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public GReloadCommand() {
		super("greload", "bungee.command.greload", "gr");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		long time = System.currentTimeMillis();
		I19n.getMessages(sender, "bungee.commands.greload.reloading", null);
		BadBungee.getInstance().getBungeeLoader().reload();
		I19n.getMessages(sender, "bungee.commands.greload.reloaded", null, System.currentTimeMillis() - time);
	}

}