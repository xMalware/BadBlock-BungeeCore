package fr.badblock.bungee.utils.i18n;

import fr.badblock.bungee.players.BadPlayer;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Utils for i18N
 * @author xMalware
 *
 */
public class I19n
{

	public static String[] getMessages(CommandSender commandSender, String key, Object... args)
	{
		if (commandSender instanceof ProxiedPlayer)
		{
			Locale locale = BadPlayer.get((ProxiedPlayer) commandSender).getLocale();
			return I18n.getInstance().get(locale, key, args);
		}
		return I18n.getInstance().get(key, args);
	}

	public static String getMessage(CommandSender commandSender, String key, Object... args)
	{
		return getMessages(commandSender, key, args)[0];
	}
	
	@SuppressWarnings("deprecation")
	public static void sendMessage(CommandSender commandSender, String key, Object... args)
	{
		commandSender.sendMessages(getMessages(commandSender, key, args));
	}
	
}
