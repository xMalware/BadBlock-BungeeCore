package fr.badblock.bungee.modules.admin.commands;

import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * This command allows to know the total number of players currently connected on the network.
 *
 * It is available to players with the following permission: bungee.command.glist
 *
 * Response to command input:
 * Key: commands.glist.message
 * Value %0: Number of players currently connected to the network
 * 
 * @author xMalware
 *
 */
public class GListCommand extends BadCommand
{

	/**
	 * Command constructor
	 */
	public GListCommand()
	{
		super("glist", "bungee.command.glist");
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Sending the answer to the command with the requested information
		I19n.sendMessage(sender, "commands.glist.message", null, BungeeManager.getInstance().getRealTimeOnlinePlayers());
	}

}