package fr.badblock.bungee.modules.basic.commands;

import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * This command is a reference to My Little Pony.
 * Usage: /brohoof
 * 
 * @author xMalware
 *
 */
public class BrohoofCommand extends BadCommand
{

	/**
	 * Command constructor
	 */
	public BrohoofCommand()
	{
		super("brohoof", "", ")");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// Brohoof!
		I19n.sendMessage(sender, "commands.broohof.message", null, sender.getName());
	}

}