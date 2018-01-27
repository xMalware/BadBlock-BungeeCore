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
     * @param jsons the undefined string array
     * @return the bungeecord BaseComponent[] array
     */
    public static BaseComponent[][] parseMcJsons(String... jsons){
        BaseComponent[] [] baseComponents = new BaseComponent[] []{};
        for (int i = 0; i < jsons.length; i++) baseComponents[i] = ComponentSerializer.parse(jsons[i]);
        return baseComponents;
    }

    /**
     * Send a bungeecord BaseComponent[] array to a bungeecord ProxyiedPlayer
     *
     * @param p the bungeecord ProxyiedPlayer
     * @param baseComponents the bungeecord BaseComponent[] array
     */
    public static void sendJsons(ProxiedPlayer p, BaseComponent[]... baseComponents) {
        Arrays.asList(baseComponents).forEach(p::sendMessage);
    }
}
