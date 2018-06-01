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

	public static String[] getMessages(Locale locale, String key, int[] indexesToTranslate, Object... args)
	{
		Object[] resultArgs = new Object[args.length];
		System.arraycopy(args, 0, resultArgs, 0, args.length);
		System.out.print("A");
		if (indexesToTranslate != null && indexesToTranslate.length != 0)
		{
			System.out.print("B");
			for (int indexToTranslate : indexesToTranslate)
			{
				System.out.print("C");
				if (indexToTranslate > resultArgs.length - 1)
				{
					continue; // something gone wrong
				}
				System.out.print("D");
				resultArgs[indexToTranslate] = I18n.getInstance().get(locale, resultArgs[indexToTranslate].toString())[0];
			}
		}
		return I18n.getInstance().get(locale, key, resultArgs);
	}
	
	public static String[] getMessages(CommandSender commandSender, String key, int[] indexesToTranslate, Object... args)
	{
		if (commandSender instanceof ProxiedPlayer)
		{
			Locale locale = BadPlayer.get((ProxiedPlayer) commandSender).getLocale();
			return getMessages(locale, key, indexesToTranslate, args);
		}
		return getMessages(I18n.getDefaultLocale(), key, indexesToTranslate, args);
	}

	public static String getMessage(CommandSender commandSender, String key, int[] indexesToTranslate, Object... args)
	{
		return getMessages(commandSender, key, indexesToTranslate, args)[0];
	}
	
	@SuppressWarnings("deprecation")
	public static void sendMessage(CommandSender commandSender, String key, int[] indexesToTranslate, Object... args)
	{
		commandSender.sendMessages(getMessages(commandSender, key, indexesToTranslate, args));
	}
	
}
