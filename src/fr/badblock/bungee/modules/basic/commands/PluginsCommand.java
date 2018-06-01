package fr.badblock.bungee.modules.basic.commands;

import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Displays a response to /plugins
 * 
 * @author xMalware
 *
 */
public class PluginsCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public PluginsCommand()
	{
		super("plugins", "", "pl", "bukkit:plugins", "bukkit:pl");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Sending the message
		I19n.sendMessage(sender, "commands.plugins.message", null, sender.getName());
	}

}