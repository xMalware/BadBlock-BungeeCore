package fr.badblock.bungee._plugins.commands.admin;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;

public class GListCommand extends BadCommand
{

	public GListCommand()
	{
		super("glist", "bungee.command.glist");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		I19n.sendMessage(sender, "commands.glist.message", BungeeManager.getInstance().getRealTimeOnlinePlayers());
	}

}