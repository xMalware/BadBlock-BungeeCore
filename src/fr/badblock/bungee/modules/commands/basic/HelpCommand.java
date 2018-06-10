package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Allows you to send help to players regarding basic commands on the server.
 * 
 * @author xMalware
 *
 */
public class HelpCommand extends BadCommand
{
	
	/**
	 * Command constructor
	 */
	public HelpCommand()
	{
		super("help", "", "?", "bukkit:help", "minecraft:help", "craftbukkit:help", "bukkit:?", "minecraft:?", "craftbukkit:?");
	}
	
	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Sending help
		I19n.sendMessage(sender, "bungee.commands.help.message", null, sender.getName());
	}

}