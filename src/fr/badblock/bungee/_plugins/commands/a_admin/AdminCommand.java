package fr.badblock.bungee._plugins.commands.a_admin;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class AdminCommand extends BadCommand
{

	public AdminCommand()
	{
		super("admin", "bungee.command.admin", "adm");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if (args.length == 0) 
		{
			I19n.sendMessage(sender, "commands.admin.usage");
			return;
		}

		String message = StringUtils.join(args, " ");

		if (message.startsWith("&h"))
		{
			message = message.substring(2);
		}
		else
		{
			message = I19n.getMessages(sender, "commands.admin.pattern", sender.getName(), message)[0];
		}

		message = ChatColor.stripColor(message);
		BungeeManager.getInstance().broadcast(message);
	}

}