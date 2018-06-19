package fr.badblock.bungee.utils.i18n;

import fr.badblock.api.common.utils.i18n.I18n;
import fr.badblock.api.common.utils.i18n.Locale;
import fr.badblock.bungee.players.BadPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Utils for i18N
 * 
 * @author xMalware
 *
 */
public class I19n {

	/**
	 * Get a message
	 * 
	 * @param with
	 *            the Command sender
	 * @param The
	 *            message key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 * @return Returns the message
	 */
	public static String getMessage(CommandSender commandSender, String key, int[] indexesToTranslate, Object... args) {
		// Get all messages and only the first in the array
		return getMessages(commandSender, key, indexesToTranslate, args)[0];
	}

	/**
	 * Get messages
	 * 
	 * @param with
	 *            the CommandSender
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 * @return Returns the translated messages
	 */
	public static String[] getMessages(CommandSender commandSender, String key, int[] indexesToTranslate,
			Object... args) {
		// If the sender is a player
		if (commandSender instanceof ProxiedPlayer) {
			// Get the BadPlayer object
			BadPlayer badPlayer = BadPlayer.get((ProxiedPlayer) commandSender);
			// Create a locale var
			Locale locale = null;
			// If the player isn't null
			if (badPlayer != null) {
				// So we set the locale
				locale = badPlayer.getLocale();
			} else {
				// Set the default locale
				locale = Locale.FRENCH_FRANCE;
			}
			// Returns the messages
			return getMessages(locale, key, indexesToTranslate, args);
		}
		// Returns the messages
		return getMessages(I18n.getDefaultLocale(), key, indexesToTranslate, args);
	}

	/**
	 * Get messages
	 * 
	 * @param Locale
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Args
	 * @return Returns the messages
	 */
	public static String[] getMessages(Locale locale, String key, int[] indexesToTranslate, Object... args) {
		// Create a new arg array
		Object[] resultArgs = new Object[args.length];
		// create an array copy
		System.arraycopy(args, 0, resultArgs, 0, args.length);
		// If we have indexes to translate
		if (indexesToTranslate != null && indexesToTranslate.length != 0) {
			// For each indexes to translate
			for (int indexToTranslate : indexesToTranslate) {
				// Check if they're out of list
				if (indexToTranslate > resultArgs.length - 1) {
					// So we don't care
					continue;
				}
				// Set the result arg
				resultArgs[indexToTranslate] = ChatColor.translateAlternateColorCodes('&',
						I18n.getInstance().get(locale, resultArgs[indexToTranslate].toString())[0]);
			}
		}
		// Get the message
		return I18n.getInstance().get(locale, key, resultArgs);
	}
	
	/**
	 * Get message
	 * 
	 * @param Locale
	 * @param Message
	 *            key
	 * @param Indexes
	 *            to translate
	 * @param Args
	 * @return Returns the messages
	 */
	public static String getMessage(Locale locale, String key, int[] indexesToTranslate, Object... args) {
		// Create a new arg array
		Object[] resultArgs = new Object[args.length];
		// create an array copy
		System.arraycopy(args, 0, resultArgs, 0, args.length);
		// If we have indexes to translate
		if (indexesToTranslate != null && indexesToTranslate.length != 0) {
			// For each indexes to translate
			for (int indexToTranslate : indexesToTranslate) {
				// Check if they're out of list
				if (indexToTranslate > resultArgs.length - 1) {
					// So we don't care
					continue;
				}
				// Set the result arg
				resultArgs[indexToTranslate] = ChatColor.translateAlternateColorCodes('&',
						I18n.getInstance().get(locale, resultArgs[indexToTranslate].toString())[0]);
			}
		}
		// Get the message
		return I18n.getInstance().get(locale, key, resultArgs)[0];
	}

	/**
	 * Send a message
	 * 
	 * @param to
	 *            the Command sender
	 * @param The
	 *            message key
	 * @param Indexes
	 *            to translate
	 * @param Arguments
	 */
	@SuppressWarnings("deprecation")
	public static void sendMessage(CommandSender commandSender, String key, int[] indexesToTranslate, Object... args) {
		// Send a message
		commandSender.sendMessages(getMessages(commandSender, key, indexesToTranslate, args));
	}

}
