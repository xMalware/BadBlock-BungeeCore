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

	/**
	 * Translate colors
	 * @param character
	 * @param message
	 * @return
	 */
	public static String translateColors(char character, String message) {
		// Use chatcolor API
		return ChatColor.translateAlternateColorCodes(character, message);
	}

	/**
	 * Translate colors of messages
	 * @param character
	 * @param messages
	 * @return
	 */
	public static String[] translateColors(char character, String... messages) {
		// Create a new array
		String[] result = new String[messages.length];
		// For each message
		for (int i = 0; i < messages.length; i++) {
			// Translate the color
			result[i] = translateColors(character, messages[i]);
		}
		// Returns the array
		return result;
	}

}
