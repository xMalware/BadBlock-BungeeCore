package fr.badblock.bungee.modules.admin.commands;

import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

/**
 * 
 * Command to stop the proxy server where this command is executed.
 * It is usable by players with the following permission: bungee.command.end
 *
 * A trace is kept in the logs.
 * 
 * @author xMalware
 *
 */
public class EndCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public EndCommand()
	{
		super("end", "bungee.command.end", "end");
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Tell the player that his action has been taken into account
		I19n.sendMessage(sender, "commands.end.message", null);
		// Stop the proxy server justifying the reason and the requester
		ProxyServer.getInstance().stop("Requested by " + sender.getName());
	}

}