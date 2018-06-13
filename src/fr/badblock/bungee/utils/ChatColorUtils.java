package fr.badblock.bungee.utils;

import net.md_5.bungee.api.ChatColor;

/**
 * 
 * ChatColor utils
 * 
 * @author xMalware
 *
 */
public class ChatColorUtils {

	public static String translateColors(char character, String message) {
		return ChatColor.translateAlternateColorCodes(character, message);
	}

	public static String[] translateColors(char character, String... messages) {
		String[] result = new String[messages.length];
		for (int i = 0; i < messages.length; i++) {
			result[i] = translateColors(character, messages[i]);
		}
		return result;
	}

}
