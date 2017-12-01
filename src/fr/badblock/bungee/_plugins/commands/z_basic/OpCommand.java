package fr.badblock.bungee._plugins.commands.z_basic;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class OpCommand extends BadCommand
{

	public OpCommand()
	{
		super("op");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if (args.length != 1)
		{
			I19n.sendMessage(sender, "commands.op.usage");
		}
		I19n.sendMessage(sender, "commands.op.message", args[0]);
	}

}