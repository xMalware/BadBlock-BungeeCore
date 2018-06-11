package fr.badblock.bungee.modules.commands.basic.msg;

import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.ArraysUtils;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Send a message
 *
 * No permission is required to execute this command.
 * 
 * @author xMalware
 *
 */
public class MsgCommand extends BadCommand
{

	// I18n key prefix
	private String prefix = "bungee.commands.msg.";

	/**
	 * Command constructor
	 */
	public MsgCommand()
	{
		super("msg", null, "whisper", "m", "mp", "w", "tellraw", "tell", "minecraft:tell", "minecraft:tellraw",
				"minecraft:whisper", "minecraft:w");
		// Allow access to the command for players only
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args)
	{
		// We get the player from the sender
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		// If no argument has been entered
		if (args.length < 2)
		{
			// We give him help.
			help(sender);
			// We stop there.
			return;
		}

		// Send a message
		msg(proxiedPlayer, args);
		
	}

	/**
	 * Sending help to the player
	 * @param sender
	 */
	private void help(CommandSender sender)
	{
		I19n.sendMessage(sender, prefix + "help", null);
	}

	/**
	 * Send a message
	 * @param sender
	 * @param args
	 */
	private void msg(ProxiedPlayer sender, String[] args)
	{
		String playerName = args[0];
		String message = getMessage(args);
		
	}
	
	private String getMessage(String[] args)
	{
		String message = "";
		int i = 0;
		for (String arg : args) {
			i++;
			if (i == 1)
				continue;
			String spacer = " ";
			if (args.length == i)
				spacer = "";
			message += arg + spacer;
		}
		return message;
	}

}
