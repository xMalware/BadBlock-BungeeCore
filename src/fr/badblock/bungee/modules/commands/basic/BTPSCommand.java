package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.badblock.bungee.utils.time.TPS;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Get the TPS.
 * Usage: /btps
 * 
 * @author xMalware
 *
 */
public class BTPSCommand extends BadCommand
{

	/**
	 * Command constructor
	 */
	public BTPSCommand()
	{
		super("btps", "", ")");
		// New tps monitor
		new TPS();
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// TPS!
		I19n.sendMessage(sender, "bungee.commands.btps.message", null, TPS.getTps());
	}

}