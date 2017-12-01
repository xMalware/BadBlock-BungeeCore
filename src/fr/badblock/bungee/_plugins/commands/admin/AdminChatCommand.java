package fr.badblock.bungee._plugins.commands.admin;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class AdminChatCommand extends BadCommand
{

	public AdminChatCommand()
	{
		super("adminchat", "bungee.command.adminchat", "ac");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if (args.length == 0) 
		{
			I19n.sendMessage(sender, "commands.adminchat.usage");
			return;
		}

		String message = StringUtils.join(args, " ");

		message = ChatColor.stripColor(message);
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.adminchat", "commands.adminchat.message", sender.getName());
	}

}