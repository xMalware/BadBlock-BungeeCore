package fr.badblock.bungee._plugins.commands.a_admin;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class AlertCommand extends BadCommand
{

	public AlertCommand()
	{
		super("lalert", "bungee.command.alert", "lalert");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if (args.length == 0) 
		{
			I19n.sendMessage(sender, "commands.alert.usage");
			return;
		}

		String message = StringUtils.join(args, " ");

		if (message.startsWith("&h"))
		{
			message = message.substring(2);
		}
		else
		{
			message = BadBungee.getInstance().getConfig().getAlertPrefix() + message;
		}

		message = ChatColor.stripColor(message);
		BungeeManager.getInstance().broadcast(message);
	}

}