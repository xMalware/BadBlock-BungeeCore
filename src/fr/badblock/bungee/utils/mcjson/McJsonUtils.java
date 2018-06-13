package fr.badblock.bungee.utils.mcjson;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;

/**
 * Util Class for working with the minecraft jsons
 *
 * @author RedSpri
 */
public class McJsonUtils {

	/**
	 * Convert an undefined string array (of minecraft jsons) into a bungeecord
	 * BaseComponent[] array.
	 *
	 * @param jsons
	 *            the undefined string array
	 * @return the bungeecord BaseComponent[] array
	 */
	public static BaseComponent[][] parseMcJsons(String... jsons) {
		// Create new base componsent array
		BaseComponent[][] baseComponents = new BaseComponent[jsons.length][jsons.length];

		// For each json
		for (int i = 0; i < jsons.length; i++) {
			// Get base component
			BaseComponent[] s = ComponentSerializer.parse(jsons[i]);
			// Set in array
			baseComponents[i] = s;
		}

		// Returns the base component
		return baseComponents;
	}

	/**
	 * Send a bungeecord BaseComponent[] array to a bungeecord ProxyiedPlayer
	 *
	 * @param p
	 *            the bungeecord ProxyiedPlayer
	 * @param baseComponents
	 *            the bungeecord BaseComponent[] array
	 */
	public static void sendJsons(ProxiedPlayer p, BaseComponent[]... baseComponents) {
		// Send message
		Arrays.asList(baseComponents).forEach(p::sendMessage);
	}

}
