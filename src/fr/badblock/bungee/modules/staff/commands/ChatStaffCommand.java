package fr.badblock.bungee.modules.staff.commands;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.abstracts.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 *	Command to send messages between staff (with a specific permission, the persons in charge can also have it).
 *	This command should be used like this: /chatstaff <message>
 *	
 *	This command broadcasts a message with permission filtering, i.e. it sends a message to all players with specific permission to this command, on all bungees.
 *	The permission required to execute this command is bungee.command.chatstaff.
 *	
 *	i18n Command Implementation :
 *	Key : bungee.commands.chatstaff.message
 *	Value %0 : name of the sender of the message
 * 	Value %1 : message sent
 *	
 *	The colors sent by the user are deleted, to avoid formatting by the user using colors, bold or italics...
 * 
 * 	@author xMalware
 *
 */
public class ChatStaffCommand extends BadCommand
{

	/**
	 * Command constructor
	 */
	public ChatStaffCommand()
	{
		super("chatstaff", "bungee.command.chatstaff", "cs");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// If no argument has been entered, i.e. the user's message
		if (args.length == 0) 
		{
			// A message is sent to him containing the information allowing him to take note of the use of the command.
			I19n.sendMessage(sender, "bungee.commands.chatstaff.usage", null);
			// Nothing has been written from him, no argument. After we explain it to him, we stop there.
			return;
		}

		// The message is retrieved by attaching each word of the message
		String message = StringUtils.join(args, " ");

		// The colors on the message are removed
		message = ChatColor.stripColor(message);

		// Init raw prefix
		String rawChatPrefix = "";
		// Init raw suffix
		String rawChatSuffix = "";

		// If the sender is a player
		if (sender instanceof ProxiedPlayer)
		{
			// Get player
			ProxiedPlayer player = (ProxiedPlayer) sender;
			// Get BadPlayer
			BadPlayer badPlayer = BadPlayer.get(player);
			// Set raw prefix
			rawChatPrefix = badPlayer.getRawChatPrefix();
			// Set raw suffix
			rawChatSuffix = badPlayer.getRawChatSuffix();
		}

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.chatstaff", "bungee.commands.chatstaff.message",
				new int[] { 0,2 }, rawChatPrefix, sender.getName(), rawChatSuffix, message);
	}

}