package fr.badblock.bungee._plugins.commands.s_staff;

import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 *	Command to send messages between staff (with a specific permission, the persons in charge can also have it).
 *	This command should be used like this: /chatstaff <message>
 *	
 *	This command broadcasts a message with permission filtering, i.e. it sends a message to all players with specific permission to this command, on all bungees.
 *	The permission required to execute this command is bungee.command.chatstaff.
 *	
 *	i18n Command Implementation :
 *	Key : commands.chatstaff.message
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
			I19n.sendMessage(sender, "commands.chatstaff.usage");
			// Nothing has been written from him, no argument. After we explain it to him, we stop there.
			return;
		}
		
		// The message is retrieved by attaching each word of the message
		String message = StringUtils.join(args, " ");

		// The colors on the message are removed
		message = ChatColor.stripColor(message);
		
		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.chatstaff", "commands.chatstaff.message", sender.getName(), message);
	}

}