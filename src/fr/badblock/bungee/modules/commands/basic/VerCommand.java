package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * View the server version
 * 
 * @author xMalware
 *
 */
public class VerCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public VerCommand()
	{
		super("ver", "", "version", "spigot", "badblock");
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Sending the message
		I19n.sendMessage(sender, "bungee.commands.ver.message", null, sender.getName(), BadBungee.getInstance().getDescription().getVersion());
	}

}