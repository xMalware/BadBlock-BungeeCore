package fr.badblock.bungee.utils;

import net.md_5.bungee.api.ChatColor;

public class ChatColorUtils {

	public static String[] translateColors(char character, String... messages) {
		String[] result = new String[messages.length];
		for (int i = 0; i < messages.length; i++) {
			result[i] = ChatColor.translateAlternateColorCodes(character, messages[i]);
		}
		return result;
	}

}
