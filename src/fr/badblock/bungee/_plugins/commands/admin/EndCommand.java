package fr.badblock.bungee._plugins.commands.admin;

import fr.badblock.bungee.listeners.abstracts.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class EndCommand extends BadCommand
{

	public EndCommand()
	{
		super("end", "bungee.command.end", "end");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		I19n.sendMessage(sender, "commands.end.message");
		ProxyServer.getInstance().stop("Requested by " + sender.getName());
	}

}