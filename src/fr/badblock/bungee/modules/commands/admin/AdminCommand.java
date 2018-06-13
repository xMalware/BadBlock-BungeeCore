package fr.badblock.bungee.modules.commands.admin;

import fr.badblock.api.common.utils.general.StringUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

/**
 * 
 * Command to send a message to all players using the nickname of the
 * administrator in question. It's like a general alert command on the server.
 * This command should be used like this: /admin <message> It can only be used
 * with this permission: bungee.command.admin
 *
 * The message is sent with the sender's nickname to all players on the server,
 * with an i18n pattern message.
 *
 * Information about the i18n message :
 *
 * Key: bungee.commands.admin.pattern Value %0: Sender's name Value %1: Message
 *
 * The colors of the message are automatically removed and the message is
 * broadcast to all players on all bungees in the same production mode.
 *
 * This command also allows you to send a message without putting the sender's
 * name on it and without using a pattern. By putting &h in front of his
 * message, the player can send a complete message. In this case, he can use the
 * colors.
 *
 * @author xMalware
 *
 */

public class AdminCommand extends BadCommand {

	/**
	 * Command constructor
	 */
	public AdminCommand() {
		super("admin", "bungee.command.admin", "adm");
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		// If no argument has been entered, i.e. the user's message
		if (args.length == 0) {
			// A message is sent to him containing the information allowing him to take note
			// of the use of the command.
			I19n.sendMessage(sender, "bungee.commands.admin.usage", null);
			// Nothing has been written from him, no argument. After we explain it to him,
			// we stop there.
			return;
		}

		// The message is retrieved by attaching each word of the message
		String message = StringUtils.join(args, " ");

		// If the player has put &h in front of his message
		if (message.startsWith("&h")) {
			// In this case, we format the colors
			message = ChatColor.translateAlternateColorCodes('&', message);
			// We remove &h from the message
			message = message.substring(2);
		}
		// If the player has not used &h
		else {
			// We remove the colors of the message
			message = ChatColor.stripColor(message);
			// We get the pattern to put it in the message
			message = I19n.getMessages(sender, "bungee.commands.admin.pattern", null, sender.getName(), message)[0];
		}

		// We send the message after processing
		BungeeManager.getInstance().broadcast(message);
	}

}