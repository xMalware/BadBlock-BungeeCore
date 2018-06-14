package fr.badblock.bungee.modules.commands.basic;

import fr.badblock.api.common.utils.StringUtils;
import fr.badblock.bungee.link.bungee.BungeeManager;
import fr.badblock.bungee.modules.commands.BadCommand;
import fr.badblock.bungee.players.BadPlayer;
import fr.badblock.bungee.utils.i18n.I19n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * Modo command
 * 
 * @author xMalware
 *
 */
public class ModoCommand extends BadCommand {

	private static String prefix = "bungee.commands.modo.";

	/**
	 * Command constructor
	 */
	public ModoCommand() {
		super("modo", "");
		this.setForPlayersOnly(true);
	}

	/**
	 * Method called when using the command
	 */
	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length < 1)
		{
			I19n.sendMessage(sender, prefix + "usage", null, sender.getName());
			return;
		}

		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;

		BadPlayer badPlayer = BadPlayer.get(proxiedPlayer);

		if (badPlayer == null)
		{
			I19n.sendMessage(sender, prefix + "erroroccurred", null, 1);
			return;
		}

		if (badPlayer.getFlags().has("modomessage"))
		{
			I19n.sendMessage(sender, prefix + "pleasewait", null);
			return;
		}

		String message = StringUtils.join(args, " ");

		// We send the message and the sender to all concerned
		BungeeManager.getInstance().targetedTranslatedBroadcast("bungee.command.modo", prefix + "modomessage", new int[] { 0, 2 }, 
				badPlayer.getRawChatPrefix(), sender.getName(), badPlayer.getRawChatSuffix(), message);

		// Send message
		I19n.sendMessage(sender, prefix + "asked", null, sender.getName());
	}

}