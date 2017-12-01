package fr.badblock.bungee._plugins.commands.c_friend;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ChatFriendCommand extends BadCommand
{

	public ChatFriendCommand()
	{
		super("chatfriend", "bungee.command.chatfriend", "cf");
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		if (args.length == 0) 
		{
			I19n.sendMessage(sender, "commands.chatfriend.usage");
			return;
		}

		String message = StringUtils.join(args, " ");

		message = ChatColor.stripColor(message);
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.chatfriend", "commands.chatfriend.message", sender.getName());
	}

}