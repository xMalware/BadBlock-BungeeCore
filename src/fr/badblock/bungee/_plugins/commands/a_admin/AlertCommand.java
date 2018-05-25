package fr.badblock.bungee._plugins.commands.a_admin;

import fr.badblock.bungee.BadBungee;
import fr.badblock.bungee._plugins.commands.BadCommand;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.utils.i18n.I19n;
import fr.toenga.common.utils.general.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
/**
 * 
 *	Command to send a message to all players.
 *	This command should be used like this: /lalert <message>
 *	It can only be used with this permission: bungee.command.alert
 *
 *	The message is sent to all players on the server, with an i18n pattern message.
 *
 *	Information about the i18n message :
 *
 *	Key: commands.admin.pattern
 *	Value %0: Sender's name
 *	Value %1: Message
 *
 * The colors of the message are automatically removed and the message is broadcast to all players on all bungees in the same production mode.
 *
 * This command also allows you to send a message without putting the sender's name on it and without using a pattern.
 *
 * @author xMalware
 *
 */
public class AlertCommand extends BadCommand
{

	/**
	 * Command constructor
	 */
	public AlertCommand()
	{
		super("lalert", "bungee.command.alert", "lalert");
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
			I19n.sendMessage(sender, "commands.alert.usage");
			// Nothing has been written from him, no argument. After we explain it to him, we stop there.
			return;
		}

		// The message is retrieved by attaching each word of the message
		String message = StringUtils.join(args, " ");
		// We format the colors
		message = ChatColor.translateAlternateColorCodes('&', message);

		// If the player has put &h in front of his message
		if (message.startsWith("&h"))
		{
			// We remove &h from the message
			message = message.substring(2);
		}
		// If the player has not used &h
		else
		{
			// We get the pattern to put it in the message
			message = BadBungee.getInstance().getConfig().getAlertPrefix() + message;
		}

		// We send the message after processing
		BungeeManager.getInstance().broadcast(message);
	}

}